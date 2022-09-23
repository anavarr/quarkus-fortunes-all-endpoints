package io.quarkus.fortune.repository;

import io.quarkus.fortune.model.Fortune;
import io.quarkus.fortune.model.Pirates;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static io.quarkus.fortune.repository.FortuneStatements.*;

@ApplicationScoped
public class FortuneRepository {

    @Inject
    Databases db;

    @Inject
    Logger logger;

    // Use a startup event and not a constructor to be sure to be called
    // from a worker thread.
    public void init(@Observes StartupEvent ev) {
        db.getConnection()
                .chain(connection ->
                        connection.query(CREATE_TABLE).execute()
                            .chain(() -> findAllAsync(connection))
                            .chain(content -> insertIfEmpty(connection, content))
                            .onTermination().call(connection::close)
                )
                .await().indefinitely();
    }

    public Uni<List<Fortune>> findAllAsync() {
        return db.getPool()
                .preparedQuery(SELECT_ALL).execute()
                .map(this::createListOfFortunes);

    }

    public Uni<List<Fortune>> findAllDeadLock() {
        return db.getPool().preparedQuery(SELECT_ALL)
                .execute()
                .map(item -> {
                    return createListOfFortunes(item);
                });
    }

    public Uni<List<Fortune>> findAllDeadLockPrint() {
        return db.getPool().preparedQuery(SELECT_ALL)
                .execute()
                .map(item -> {
                    System.out.println("inner - "+Thread.currentThread());
                    return createListOfFortunes(item);
                });
    }

    public List<Fortune> findAllAsyncAndAwait() {
        var rows = db.getPool().preparedQuery(SELECT_ALL)
                .executeAndAwait();
        return createListOfFortunes(rows);
    }

    public List<Fortune> findAllBlocking() {
        List<Fortune> fortunes = new ArrayList<>();
        Connection conn = null;
        try {
            conn = db.getJdbcConnection();
            var preparedStatement = conn.prepareStatement(SELECT_ALL);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                fortunes.add(create(rs));
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.warn("Unable to retrieve fortunes from the database", e);
        } finally {
           close(conn);
        }
        return fortunes;
    }

    private Fortune create(ResultSet rs) throws SQLException {
        return new Fortune(rs.getInt("id"), rs.getString("message"));
    }

    private Fortune create(Row row) {
        return new Fortune(row.getInteger("id"), row.getString("message"));
    }

    private List<Fortune> createListOfFortunes(RowSet<Row> rowset) {
        List<Fortune> ret = new ArrayList<>();
        for (Row r : rowset) {
            ret.add(create(r));
        }
        return ret;
    }

    private Uni<List<Fortune>> findAllAsync(SqlConnection connection) {
        return connection.preparedQuery(SELECT_ALL).execute()
                .map(this::createListOfFortunes);
    }

    private Uni<?> insertIfEmpty(SqlConnection connection, List<Fortune> content) {
        if (content.isEmpty()) {
            List<Uni<RowSet<Row>>> unis = new ArrayList<>();
            for (Fortune fortune : Pirates.fortunes()) {
                unis.add(connection.preparedQuery(INSERT_USERS_SQL)
                        .execute(Tuple.of(fortune.id(), fortune.message())));
            }
            return Uni.join().all(unis).andFailFast();
        } else {
            return Uni.createFrom().voidItem();
        }
    }

    private void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Ignore me.
            }
        }
    }
}
