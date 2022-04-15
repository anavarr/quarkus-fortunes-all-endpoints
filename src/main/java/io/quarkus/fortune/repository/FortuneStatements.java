package io.quarkus.fortune.repository;

public class FortuneStatements {

    private FortuneStatements() {
        // Avoid direct instantiation
    }

    static final String DROP_TABLE = "DROP TABLE IF EXISTS fortune";

    static final String SELECT_ALL = "SELECT * FROM Fortune";

    static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS fortune " +
            "(ID INT PRIMARY KEY ," +
            " MESSAGE TEXT)";

    static final String INSERT_USERS_SQL = "INSERT INTO fortune" +
            "  (id, message) VALUES " +
            " ($1, $2);";
}
