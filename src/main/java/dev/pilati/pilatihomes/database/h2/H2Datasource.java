package dev.pilati.pilatihomes.database.h2;

import java.io.File;

import dev.pilati.pilatihomes.PilatiHomes;
import dev.pilati.pilatihomes.database.Datasource;

public class H2Datasource extends Datasource{
    
    @Override
    protected String getJDBCUrl() {
        File database = new File(
            PilatiHomes.getInstance().getDataFolder(), 
            "database"
        );

        return "jdbc:h2:" + database.getAbsolutePath() + ";MODE=MySQL";
    }

    @Override
    protected String getUsername() {
        return "sa";
    }

    @Override
    protected String getPassword() {
        return null;
    }
}
