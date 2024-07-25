package dev.pilati.pilatihomes.database.mysql;

import dev.pilati.pilatihomes.PilatiHomes;
import dev.pilati.pilatihomes.database.Datasource;

public class MysqlDatasource extends Datasource {

    @Override
    protected String getJDBCUrl() {
        String host = PilatiHomes.getInstance().getConfig().getString("database.mysql.host");
        String port = PilatiHomes.getInstance().getConfig().getString("database.mysql.port");
        String database = PilatiHomes.getInstance().getConfig().getString("database.mysql.database");
        return "jdbc:mysql://" + host + ":" + port + "/" + database;
    }

    @Override
    protected String getUsername() {
        return PilatiHomes.getInstance().getConfig().getString("database.mysql.username");
    }

    @Override
    protected String getPassword() {
        return PilatiHomes.getInstance().getConfig().getString("database.mysql.password");
    }
}
