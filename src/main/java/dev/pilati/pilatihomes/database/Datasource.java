package dev.pilati.pilatihomes.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.flywaydb.core.Flyway;

import com.zaxxer.hikari.HikariDataSource;

import dev.pilati.pilatihomes.PilatiHomes;
import dev.pilati.pilatihomes.database.h2.H2Datasource;
import dev.pilati.pilatihomes.database.mysql.MysqlDatasource;

public abstract class Datasource {

    protected abstract String getJDBCUrl();

    protected abstract String getUsername();

    protected abstract String getPassword();

    protected HikariDataSource dataSource;

    protected void migrate() {
        Flyway.configure(this.getClass().getClassLoader())
            .dataSource(getJDBCUrl(), getUsername(), getPassword())
            .locations("db/migration")
            .validateMigrationNaming(true)
            .load()
            .migrate();
    }

    protected void connect() {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(getJDBCUrl());
        dataSource.setUsername(getUsername());
        dataSource.setPassword(getPassword());
        dataSource.setMaximumPoolSize(getMaximumPoolSize());
    }

    public static Datasource createDataSource() {
        Datasource connection = switch (getDriver()) {
            case "mysql" -> new MysqlDatasource();
            case "h2" -> new H2Datasource();
            default -> throw new IllegalArgumentException("Invalid driver: " + getDriver());
        };

        connection.connect();
        connection.migrate();
        return connection;
    }

    public void closeDataSource() {
        dataSource.close();
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    protected int getMaximumPoolSize() {
        return PilatiHomes.getInstance().getConfig().getInt("database.maximum-pool-size");
    }

    protected static String getDriver() {
        return PilatiHomes.getInstance().getConfig().getString("database.driver");
    }
}
