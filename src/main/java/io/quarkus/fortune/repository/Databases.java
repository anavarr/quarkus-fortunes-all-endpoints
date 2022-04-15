package io.quarkus.fortune.repository;

import io.agroal.api.AgroalDataSource;
import io.quarkus.reactive.datasource.ReactiveDataSource;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.SqlConnection;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;

@ApplicationScoped
public class Databases {

    @Inject
    Logger logger;

    private final PgPool pool;
    private final AgroalDataSource ds;

    public Databases(@ReactiveDataSource("fortunes-reactive") PgPool pool,
                     @io.quarkus.agroal.DataSource("fortunes-jdbc") AgroalDataSource ds) {
        this.pool = pool;
        this.ds = ds;
    }

    public Uni<SqlConnection> getConnection() {
        return pool.getConnection();
    }

    public PgPool getPool() {
        return pool;
    }

    public Connection getJdbcConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            logger.warn("Unable to get a connection from the Agroal datasource", e);
            return null;
        }
    }
}