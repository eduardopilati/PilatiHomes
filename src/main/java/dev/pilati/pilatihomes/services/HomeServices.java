package dev.pilati.pilatihomes.services;

import java.sql.SQLException;
import java.util.List;

import org.bukkit.entity.Player;

import dev.pilati.pilatihomes.model.Home;
import dev.pilati.pilatihomes.repository.HomeRepository;

public class HomeServices {
    public static final String DEFAULT_HOME_NAME = "home";
    
    HomeRepository repository = new HomeRepository();

    public void setHome(Home home) throws SQLException { 
        Home previousHome = repository.find(home.getName(), home.getOwner());

        if(previousHome != null) {
            home.setId(previousHome.getId());
        }
        
        repository.save(home);
    }

    public List<Home> getHomes(Player owner) throws SQLException {
        return repository.find(owner.getUniqueId());
    }

    public Home getHome(String nome, Player player) throws SQLException {
        return repository.find(nome, player.getUniqueId());
    }

    public void deleteHome(Home home) throws SQLException {
        repository.delete(home);
    }
}
