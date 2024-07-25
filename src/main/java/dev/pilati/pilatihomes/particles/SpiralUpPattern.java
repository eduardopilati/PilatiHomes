package dev.pilati.pilatihomes.particles;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import dev.pilati.pilatihomes.PilatiHomes;

public class SpiralUpPattern extends Pattern {

    private final double particles;

    private final double turns;

    private final double radius;

    private final double height;

    private final double timeInTicks;

    public SpiralUpPattern () {
        super();

        FileConfiguration config = PilatiHomes.getInstance().getConfig();
        this.particles = config.getDouble("patterns-config.spiralup.particle-count");
        this.turns = config.getDouble("patterns-config.spiralup.turns");
        this.radius = config.getDouble("patterns-config.spiralup.radius");
        this.height = config.getDouble("patterns-config.spiralup.height");
        this.timeInTicks = config.getDouble("patterns-config.spiralup.time-in-ticks");
    }

    @Override
    public void play(Location location) {
        for (int i = 0; i < this.particles; i ++) {
            double angle = ((360 * this.turns) / this.particles) * i;
            double x = this.radius * Math.cos(angle);
            double z = this.radius * Math.sin(angle);
            double y = i * (this.height / this.particles);

            createTask(
                location.clone().add(x, y, z), 
                i
            );
        }
    }

    @Override
    public int getDurationInTicks() {
        return (int) (this.timeInTicks);
    }

    protected void createTask(Location location, int index) {
        int ticks = (int) (this.timeInTicks / this.particles * index);
        new SpawnParticleTask(location, index).runTaskLater(PilatiHomes.getInstance(), ticks);
    }            

    protected void spawnParticle(Location location, int index) {
        int green = (int) (255 / this.particles * index);
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, green, 255), 1);
        location.getWorld().spawnParticle(Particle.DUST, location, 1, dustOptions);
    }

    private class SpawnParticleTask extends BukkitRunnable {

        private final Location location;

        private int index = 0;

        public SpawnParticleTask(Location location, int index) {
            this.location = location;
            this.index = index;
        }

        @Override
        public void run() {
            spawnParticle(this.location, this.index);
        }
    }
}
