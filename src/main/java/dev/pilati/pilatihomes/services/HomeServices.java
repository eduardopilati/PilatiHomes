package dev.pilati.pilatihomes.services;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import dev.pilati.pilatihomes.PilatiHomes;
import dev.pilati.pilatihomes.model.Home;
import dev.pilati.pilatihomes.particles.Pattern;
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

    protected Pattern getBefforeAnimationPattern () {
        String pattern = PilatiHomes.getInstance().getConfig().getString("teleport.before.pattern");
        return Pattern.getPattern(pattern);
    }

    protected Pattern getAfterAnimationPattern () {
        String pattern = PilatiHomes.getInstance().getConfig().getString("teleport.after.pattern");
        return Pattern.getPattern(pattern);
    }

    protected void beforeAnimation(Location location) {
        Optional
            .ofNullable(getBefforeAnimationPattern())
            .ifPresent(p -> p.play(location));
    }

    protected void afterAnimation(Location location) {
        Optional
            .ofNullable(getAfterAnimationPattern())
            .ifPresent(p -> p.play(location));
    }
    
    protected int getBeforeAnimationDuration() {
        return Optional
            .ofNullable(getBefforeAnimationPattern())
            .map(Pattern::getDurationInTicks)
            .orElse(0);
    }

    public void teleportToHome(Home home, Player player) {
        boolean wait = PilatiHomes.getInstance().getConfig().getBoolean("teleport.before.wait");
        
        if(!wait) {
            beforeAnimation(player.getLocation());
            player.teleport(home.getLocation());
            afterAnimation(home.getLocation());
            return;
        }

        beforeAnimation(player.getLocation());
        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(home.getLocation());
                afterAnimation(home.getLocation());
            }
        }.runTaskLater(PilatiHomes.getInstance(), getBeforeAnimationDuration());
    }
}
