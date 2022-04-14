package io.quarkus.benchmark.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.hibernate.HikariConnectionProvider;
import com.zaxxer.hikari.pool.HikariPool;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.SqlClient;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.PoolOptions;
import org.postgresql.ds.PGConnectionPoolDataSource;
import org.postgresql.ds.PGPoolingDataSource;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@ApplicationScoped
public class PgClientFactory {

	// vertx-reactive:postgresql://tfb-database:5432/hello_world
	private static final String PG_URI_MATCHER = "vertx-reactive:postgresql://([-a-zA-Z]+):([0-9]+)/(.*)";
	volatile private PgPool pool;
	volatile private Connection connection;
	volatile private HikariDataSource hikariConnection;
	PGPoolingDataSource connectionPool;
	String urlJdbc = "jdbc:postgresql://192.168.1.37/quarkus_test";
	String user="quarkus_test";
	String pass="quarkus_test";

	static final int MAX_CONNECTIONS = 100;

	@Inject
	Vertx vertx;

	@Produces
	@ApplicationScoped
	public PgClients pgClients() {
		return new PgClients(this);
	}



	public PgPool getPgPool() {
		if(pool == null){
			System.out.println("The pool is null");
			pool = createPool();
		}
		return pool;
	}

	public Connection getConnection() {
		if(connection == null){
			System.out.println("The connection is null");
			connection = createConnection();
		}
		return connection;
	}

	public Connection getHikariConnection(){
		if(hikariConnection == null){
			System.out.println("the hikari pool is null");
			hikariConnection = createHikariConnection();
		}
		Connection hc = null;
		try {
			hc =  hikariConnection.getConnection();
			return hc;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Connection getConnectionPool(){
		if(connectionPool == null){
			System.out.println("the connection pool is null");
			createConnectionPool();
		}
		try {
			return connectionPool.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void createConnectionPool() {
		PGPoolingDataSource source = new PGPoolingDataSource();
		source.setDataSourceName("A Data Source");
		source.setServerNames(new String[] {"localhost"});
		source.setDatabaseName(user);
		source.setUser(user);
		source.setPassword(pass);
		source.setMaxConnections(MAX_CONNECTIONS);

		this.connectionPool = source;
	}

	private PgPool createPool(){
		PgConnectOptions connectOptions = new PgConnectOptions()
				.setPort(5432)
				.setHost("192.168.1.37")
				.setDatabase("quarkus_test")
				.setUser(user)
				.setPassword(pass);

		// Pool options
		PoolOptions poolOptions = new PoolOptions()
				.setMaxSize(MAX_CONNECTIONS);

		return PgPool.pool(vertx, connectOptions, poolOptions);
	}

	private HikariDataSource createHikariConnection(){
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl( urlJdbc );
		config.setMaximumPoolSize(100);
		config.setUsername( user );
		config.setPassword( pass );
		HikariDataSource ds = new HikariDataSource(config);
		return ds;
	}

	private Connection createConnection() {
		try {
			var connection = DriverManager.getConnection(urlJdbc, user, pass);
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}