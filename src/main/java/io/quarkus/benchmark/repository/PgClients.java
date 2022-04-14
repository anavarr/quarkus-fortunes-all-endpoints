package io.quarkus.benchmark.repository;

import io.vertx.mutiny.pgclient.PgPool;

import java.sql.Connection;

class PgClients {

    private PgClientFactory pgClientFactory;
	// for ArC
	public PgClients() {
	}

	public PgClients(PgClientFactory pgClientFactory) {
	    this.pgClientFactory = pgClientFactory;
    }

    public PgPool getPgPool() {
        return pgClientFactory.getPgPool();
    }

    public Connection getConnection() {
            return pgClientFactory.getHikariConnection();
    }
}