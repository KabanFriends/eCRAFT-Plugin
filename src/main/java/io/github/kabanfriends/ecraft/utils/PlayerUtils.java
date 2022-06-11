package io.github.kabanfriends.ecraft.utils;

import io.github.kabanfriends.ecraft.ECraft;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerUtils {

    public static ArrayList<UUID> disagreedUsers = new ArrayList<>();

    public static String getLastSeenName(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) return player.getName();

        YamlConfiguration yaml = getPlayerConfig(uuid);
        return yaml.getString("lastname");
    }

    public static void setNickname(Player player, String nickname) {
        if (nickname != null) player.displayName(ComponentUtils.legacy(nickname + "§7~"));
        else player.displayName(null);

        YamlConfiguration yaml = getPlayerConfig(player.getUniqueId());
        yaml.set("nickname", nickname);
        savePlayerConfig(player.getUniqueId(), yaml);
    }

    public static String getNickname(Player player) {
        YamlConfiguration yaml = getPlayerConfig(player.getUniqueId());
        return yaml.getString("nickname");
    }

    public static YamlConfiguration getPlayerConfig(UUID uuid) {
        String uuidStr = uuid.toString();
        File file = new File("plugins/eCRAFT/playerdata/" + uuidStr + ".yml");

        if (file.exists() && file.isFile()) {
            return YamlConfiguration.loadConfiguration(file);
        }else {
            return new YamlConfiguration();
        }
    }

    public static void savePlayerConfig(UUID uuid, YamlConfiguration yaml) {
        String uuidStr = uuid.toString();
        try {
            yaml.save("plugins/eCRAFT/playerdata/" + uuidStr + ".yml");
        } catch (IOException e) {
            ECraft.getInstance().getLogger().log(Level.SEVERE, "Error while saving player data of " + Bukkit.getPlayer(uuid).getName() + "!");
            e.printStackTrace();
        }
    }
}
