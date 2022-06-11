package io.github.kabanfriends.ecraft.handlers;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TablistHandler {

    private static final int TABLIST_INTERVAL = 1;

    private final List<UUID> realNameTargets;
    private final BukkitTask tablistTask;

    public TablistHandler() {
        realNameTargets = new ArrayList<UUID>();

        tablistTask = new BukkitRunnable() {
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) sendTablist(player);
            }
        }.runTaskTimer(ECraft.getInstance(), TABLIST_INTERVAL * 20, TABLIST_INTERVAL * 20);
    }

    public void close() {
        tablistTask.cancel();
    }

    public List<UUID> getRealNameTargets() {
        return realNameTargets;
    }

    public static void sendTablist(Player player) {
        Economy econ = ECraft.getInstance().getEconomy();
        Chat chat = ECraft.getInstance().getChat();

        int players = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!isVanished(p)) players++;
        }

        String playerString = "players";
        if (players <= 1) playerString = "player";

        String header = "§#ffcc00§leCRAFT §#aadb1b§l2.0" + "\n"
                + "§f" + players + "/" + Bukkit.getMaxPlayers() + "§7 " + playerString + " playing" + "\n"
                + "§7Server TPS: §f" + String.format("%.1f", Bukkit.getTPS()[0]);
        String footer = "§7Balance: §f" + econ.format(econ.getBalance(player)) + "\n"
                + "§7Ping: §f" + player.getPing() + "ms" + "\n"
                + "\n"
                + "§fdiscord.gg/3MkYyFnkqG";

        player.sendPlayerListHeader(ComponentUtils.legacy(header));
        player.sendPlayerListFooter(ComponentUtils.legacy(footer));

        StringBuilder builder = new StringBuilder();
        String prefix = chat.getGroupPrefix(player.getWorld(), chat.getPrimaryGroup(player));
        prefix = prefix.replaceAll("#([a-fA-F0-9]{6})", "§#$1");
        if (!prefix.isEmpty()) {
            builder.append(prefix);
        }
        if (ECraft.getInstance().getTablistHandler().getRealNameTargets().contains(player.getUniqueId())) builder.append(player.getName());
        else builder.append(ComponentUtils.legacy(player.displayName()));

        player.playerListName(ComponentUtils.legacy(builder.toString()));
    }

    private static boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }
}
