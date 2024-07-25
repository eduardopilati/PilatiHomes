package dev.pilati.pilatihomes.services;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import dev.pilati.pilatihomes.PilatiHomes;
import dev.pilati.pilatihomes.model.Home;

public class TeleportServices {

    public void teleportToHome(Home home, Player player) {
        boolean wait = PilatiHomes.getInstance().getConfig().getBoolean("teleport.before.wait");
        AnimationServices animationServices = new AnimationServices();
        
        animationServices.playAnimation("before", player.getLocation());
        
        if(!wait) {
            player.teleport(home.getLocation());
            setLastTeleportTime(player);
            animationServices.playAnimation("after", home.getLocation());
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(home.getLocation());
                setLastTeleportTime(player);
                animationServices.playAnimation("after", home.getLocation());
            }
        }.runTaskLater(PilatiHomes.getInstance(), animationServices.getAnimationDuration("before"));
    }

    private void setLastTeleportTime(Player player) {
        player.setMetadata(
            "lastTeleport", 
            new FixedMetadataValue(PilatiHomes.getInstance(), System.currentTimeMillis())
        );
    }

    private long getLastTeleportTime(Player player) {
        if(!player.hasMetadata("lastTeleport")) {
            return 0;
        }

        return player.getMetadata("lastTeleport").get(0).asLong();
    }

    public boolean isInCooldown(Player player) {
        int cooldown = PilatiHomes.getInstance().getConfig().getInt("teleport.cooldown");
        long cooldownUntil = getLastTeleportTime(player) + cooldown * 1000;
        return System.currentTimeMillis() < cooldownUntil;
    }
    
    public int getCooldownRemainingSeconds(Player player) {
        int cooldown = PilatiHomes.getInstance().getConfig().getInt("teleport.cooldown");
        long cooldownUntil = getLastTeleportTime(player) + cooldown * 1000;
        return (int) ((cooldownUntil - System.currentTimeMillis()) / 1000);
    }
}
