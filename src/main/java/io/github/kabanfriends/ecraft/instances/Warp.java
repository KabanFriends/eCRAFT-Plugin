package io.github.kabanfriends.ecraft.instances;

import org.bukkit.Location;

import java.util.UUID;

public class Warp {

    private String name;
    private UUID owner;
    private Location location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
