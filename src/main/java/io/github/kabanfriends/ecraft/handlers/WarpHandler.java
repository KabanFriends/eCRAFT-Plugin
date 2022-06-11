package io.github.kabanfriends.ecraft.handlers;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.instances.Warp;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;

public class WarpHandler {

    private TreeMap<String, Warp> readCache;
    private HashMap<String, Warp> writeCache;

    public WarpHandler() {
        readCache = new TreeMap<>();
        writeCache = new HashMap<>();
        load();
    }

    public void load() {
        ECraft.getInstance().getLogger().log(Level.INFO, "Loading warps...");

        //cache all warps
        File warpDir = new File("plugins/eCRAFT/warps");
        if (warpDir.isDirectory()) {
            for (File file : warpDir.listFiles()) {
                if (file.getName().endsWith(".yml")) {
                    getWarp(FilenameUtils.removeExtension(file.getName()));
                }
            }
        }
    }

    public void close() {
        ECraft.getInstance().getLogger().log(Level.INFO, "Saving warps...");

        for (Warp warp : writeCache.values()) {
            if (warp != null) write(warp);
        }
    }

    public void write(Warp warp) {
        YamlConfiguration yaml = new YamlConfiguration();

        yaml.set("name", warp.getName());
        yaml.set("owner", warp.getOwner().toString());
        yaml.set("location", warp.getLocation());

        try {
            yaml.save("plugins/eCRAFT/warps/" + warp.getName().toLowerCase() + ".yml");
        } catch (IOException e) {
            e.printStackTrace();
            ECraft.getInstance().getLogger().log(Level.SEVERE, "Error while writing Warp yaml!");
        }
    }

    public Warp getWarp(String name) {
        if (!readCache.containsKey(name)) {
            File file = new File("plugins/eCRAFT/warps/" + name.toLowerCase() + ".yml");
            if (file.exists() && file.isFile()) {
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

                String originalName = yaml.getString("name");
                Location loc = yaml.getLocation("location");
                UUID uuid = UUID.fromString(yaml.getString("owner"));

                Warp warp = new Warp();
                warp.setName(originalName);
                warp.setOwner(uuid);
                warp.setLocation(loc);

                readCache.put(name, warp);
                return warp;
            }
            return null;
        }else {
            return readCache.get(name);
        }
    }

    public void setWarp(Warp warp) {
        readCache.put(warp.getName(), warp);
        writeCache.put(warp.getName(), warp);
    }

    public void deleteWarp(String name) {
        readCache.put(name, null);
        writeCache.remove(name);

        File file = new File("plugins/eCRAFT/warps/" + name.toLowerCase() + ".yml");
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    public String[] getWarpNames() {
        return readCache.keySet().toArray(new String[0]);
    }
}
