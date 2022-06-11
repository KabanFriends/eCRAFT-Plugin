package io.github.kabanfriends.ecraft.handlers;

import io.github.kabanfriends.ecraft.ECraft;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Level;

public class AutoSaveHandler {

    private static final int AUTOSAVE_INTERVAL = 1800;

    private final BukkitTask task;

    public AutoSaveHandler() {
        task = new BukkitRunnable() {
            public void run() {
                ECraft plugin = ECraft.getInstance();

                plugin.getLogger().log(Level.INFO, "Autosave started");
                plugin.saveWarps();
                plugin.saveHomes();
                plugin.getLogger().log(Level.INFO, "Autosave finished");
            }
        }.runTaskTimer(ECraft.getInstance(), AUTOSAVE_INTERVAL * 20, AUTOSAVE_INTERVAL * 20);
    }

    public void close() {
        task.cancel();
    }
}
