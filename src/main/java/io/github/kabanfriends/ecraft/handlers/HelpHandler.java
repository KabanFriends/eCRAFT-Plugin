package io.github.kabanfriends.ecraft.handlers;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class HelpHandler {

    private static final int AUTOMESSAGE_INTERVAL = 240;

    private final TreeMap<String, String> hints;
    private final BukkitTask autoMessageTask;

    private Random random;
    private List<String> awaitingHints;

    public HelpHandler() {
        random = new Random();
        hints = new TreeMap<>();

        ECraft.getInstance().getLogger().log(Level.INFO, "Loading hints...");

        File hintDir = new File("plugins/eCRAFT/hints");
        if (hintDir.exists() && hintDir.isDirectory()) {
            for (File file : hintDir.listFiles()) {
                if (file.getName().endsWith(".yml")) {
                    YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                    String id = FilenameUtils.removeExtension(file.getName());
                    String title = yaml.getString("title");

                    hints.put(id, title);
                }
            }
        }

        awaitingHints = new ArrayList(hints.keySet());

        autoMessageTask = new BukkitRunnable() {
            public void run() {
                if (awaitingHints.size() == 0) {
                    awaitingHints = new ArrayList(hints.keySet());
                }

                int index = random.nextInt(awaitingHints.size());
                String id = awaitingHints.get(index);
                awaitingHints.remove(index);

                File file = new File("plugins/eCRAFT/hints/" + id + ".yml");
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

                String title = yaml.getString("title");
                String autoMessage = yaml.getString("automessage");
                List<String> descriptions = yaml.getStringList("descriptions");

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(ComponentUtils.legacyAmpersand("&b▶&f " + autoMessage + "\n&7* Type &6/help " + id + "&7 to read more about &f" + title + "&7."));
                }
            }
        }.runTaskTimer(ECraft.getInstance(), AUTOMESSAGE_INTERVAL * 20, AUTOMESSAGE_INTERVAL * 20);
    }

    public void close() {
        autoMessageTask.cancel();
    }

    public TreeMap<String, String> getHelpCategories() {
        return hints;
    }
}
