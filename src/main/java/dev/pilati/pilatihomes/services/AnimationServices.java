package dev.pilati.pilatihomes.services;

import java.util.Optional;

import org.bukkit.Location;

import dev.pilati.pilatihomes.PilatiHomes;
import dev.pilati.pilatihomes.particles.Pattern;

public class AnimationServices {
    
    public Pattern getAnimationPattern(String key) {
        String pattern = PilatiHomes.getInstance().getConfig().getString("teleport." + key + ".pattern");
        return Pattern.getPattern(pattern);
    }

    public void playAnimation(String key, Location location) {
        Optional
            .ofNullable(getAnimationPattern(key))
            .ifPresent(p -> p.play(location));
    }

    public int getAnimationDuration(String key) {
        return Optional
            .ofNullable(getAnimationPattern(key))
            .map(Pattern::getDurationInTicks)
            .orElse(0);
    }
}
