package io.github.kabanfriends.ecraft.handlers;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.instances.Home;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class HomeHandler {

    private HashMap<UUID, Home> readCache;
    private HashMap<UUID, Home> writeCache;

    public HomeHandler() {
        readCache = new HashMap<>();
        writeCache = new HashMap<>();
    }

    public void close() {
        ECraft.getInstance().getLogger().log(Level.INFO, "Saving homes...");

        for (Home home : writeCache.values()) {
            if (home != null) write(home);
        }
    }

    public void write(Home home) {
        YamlConfiguration yaml = new YamlConfiguration();

        yaml.set("owner", home.getOwner().toString());
        yaml.set("location", home.getLocation());

        try {
            yaml.save("plugins/eCRAFT/homes/" + home.getOwner() + ".yml");
        } catch (IOException e) {
            e.printStackTrace();
            ECraft.getInstance().getLogger().log(Level.SEVERE, "Error while writing home yaml!");
        }
    }

    public Home getHome(UUID uuid) {
        if (!readCache.containsKey(uuid)) {
            File file = new File("plugins/eCRAFT/homes/" + uuid + ".yml");
            if (file.exists() && file.isFile()) {
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

                Location loc = yaml.getLocation("location");
                Home home = new Home();
                home.setOwner(uuid);
                home.setLocation(loc);

                readCache.put(uuid, home);
                return home;
            }
            return null;
        }else {
            return readCache.get(uuid);
        }
    }

    public void setHome(Home home) {
        readCache.put(home.getOwner(), home);
        writeCache.put(home.getOwner(), home);
    }

    public void deleteHome(UUID uuid) {
        readCache.put(uuid, null);
        writeCache.remove(uuid);

        File file = new File("plugins/eCRAFT/homes/" + uuid + ".yml");
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }
}
