package dev.pilati.pilatihomes.model;

import java.util.UUID;

import org.bukkit.Location;

import dev.pilati.pilatihomes.services.HomeServices;


public class Home implements Model {
    private Integer id = null;

    private Location location = null;

    private String name = HomeServices.DEFAULT_HOME_NAME;

    private UUID owner = null;

    public Home() {}

    public Home(Integer id, Location location, String name, UUID owner) {
        this.id = id;
        this.location = location;
        this.name = name;
        this.owner = owner;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        
        if (name == null || name.isEmpty()) {
            this.name = HomeServices.DEFAULT_HOME_NAME;
        }
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }
}
