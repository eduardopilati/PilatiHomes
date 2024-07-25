package dev.pilati.pilatihomes.particles;

import org.bukkit.Location;

public abstract class Pattern {

    public abstract void play(Location location);

    public abstract int getDurationInTicks();

    public static Pattern getPattern(String key) {
        return switch (key) {
            case "spiralup" -> new SpiralUpPattern();
            case "spiraldown" -> new SpiralDownPattern();
            default -> null;
        };
    }    
}
