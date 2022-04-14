package io.quarkus.benchmark.repository;

import io.quarkus.benchmark.model.Fortune;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FortuneRepository {

    @Inject
    PgClients clients;

    private static final String createTableSQL = "CREATE TABLE fortune " +
            "(ID INT PRIMARY KEY ," +
            " MESSAGE TEXT)";
    private static final String INSERT_USERS_SQL = "INSERT INTO fortune" +
            "  (id, message) VALUES " +
            " ($1, $2);";


    public void prepareDB(){
        var fort = new ArrayList<Fortune>();
        for (int i = 0;i<15;i++){
            fort.add(new Fortune(i, String.valueOf(i)+" is my msg"));
        }
        try{
            clients.getPgPool().query("DROP TABLE fortune").execute().await().indefinitely();
        }catch(Exception e){
            System.out.println(e);
        }
        clients.getPgPool().query(createTableSQL).execute().await().indefinitely();
        for(Fortune fortune : fort){
            var result = clients.getPgPool().preparedQuery(INSERT_USERS_SQL)
                    .execute(Tuple.of(fortune.getId(), fortune.getMessage())).await().indefinitely();
        }
        return;
    }
    public Uni<List<Fortune>> findAll() {
        return clients.getPgPool().preparedQuery("SELECT * FROM Fortune" )
                .execute()
                .map(rowset -> {
                    List<Fortune> ret = new ArrayList<>(rowset.size()+1);
                    for(Row r : rowset) {
                        ret.add(new Fortune(r.getInteger("id"), r.getString("message")));
                    }
                    return ret;
                });
    }

    public List<Fortune> findAllJdbc() {
        List<Fortune> result = new ArrayList<>();
        Connection conn = null;
        try {
            conn = clients.getConnection();
            var preparedStatement = conn.prepareStatement("SELECT * FROM Fortune");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                result.add(new Fortune(rs.getInt("id"), rs.getString("message")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
