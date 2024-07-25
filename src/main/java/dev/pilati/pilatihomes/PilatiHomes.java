package dev.pilati.pilatihomes;

import org.bukkit.plugin.java.JavaPlugin;

import dev.pilati.pilatihomes.command.DelHomeCommand;
import dev.pilati.pilatihomes.command.HomeCommand;
import dev.pilati.pilatihomes.command.HomesCommand;
import dev.pilati.pilatihomes.command.SetHomeCommand;
import dev.pilati.pilatihomes.database.Datasource;

public class PilatiHomes extends JavaPlugin {
    private static PilatiHomes instance;

    private Datasource databaseDatasource;

    public static PilatiHomes getInstance() {
        return instance;
    }
    
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        getCommand("home").setExecutor(new HomeCommand());
        getCommand("home").setTabCompleter(new HomeCommand());

        getCommand("homes").setExecutor(new HomesCommand());
        
        getCommand("sethome").setExecutor(new SetHomeCommand());
        getCommand("sethome").setTabCompleter(new SetHomeCommand());
        
        getCommand("delhome").setExecutor(new DelHomeCommand());
        getCommand("delhome").setTabCompleter(new DelHomeCommand());

        databaseDatasource = Datasource.createDataSource();
    }

    @Override
    public void onDisable() {
        databaseDatasource.closeDataSource();
        
        instance = null;
    }

    public Datasource getDatasource() {
        return databaseDatasource;
    }
}
