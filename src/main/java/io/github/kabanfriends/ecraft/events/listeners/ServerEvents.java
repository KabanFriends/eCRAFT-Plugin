package io.github.kabanfriends.ecraft.events.listeners;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.io.File;

public class ServerEvents implements Listener {

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        String motd = ECraft.getInstance().getMotdHandler().getRandomMotd();

        if (motd.contains("{totalplayers}")) {
            int totalPlayers = new File("plugins/eCRAFT/playerdata").listFiles().length;
            motd = motd.replaceAll("\\{totalplayers}", String.valueOf(totalPlayers));
        }

        event.motd(ComponentUtils.legacy("§#ffcc00§leCRAFT §#aadb1b§l2.0\n§f" + motd + "§8 - §rMinecraft 1.18.x"));

        event.setServerIcon(ECraft.getInstance().getMotdHandler().getRandomIcon());
    }
}
