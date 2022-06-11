package io.github.kabanfriends.ecraft.events.listeners;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.enums.MovementWay;
import io.github.kabanfriends.ecraft.events.RegionLeaveEvent;
import io.github.kabanfriends.ecraft.handlers.TablistHandler;
import io.github.kabanfriends.ecraft.instances.Warp;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import io.github.kabanfriends.ecraft.utils.PlayerUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Team;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerEvents implements Listener {

    public static List<UUID> pvpPlayers = new ArrayList<>();
    public static HashMap<UUID, Long> lastPvp = new HashMap<>();

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        String message = ComponentUtils.plain(event.message());
        if (message.contains("burgerking") || message.contains("burger king")) {
            event.getPlayer().sendMessage("§cwhy?????????????");
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        List<Component> lines = event.lines();

        for (int i = 0; i < lines.size(); i++) {
            Component component = lines.get(i);
            String plainComponent = ComponentUtils.plain(component);
            event.line(i, ComponentUtils.legacyAmpersand(plainComponent));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String message = "§e" + player.getName() + "§e joined the game";

        if (!player.hasPlayedBefore()) {
            message = "§e" + player.getName() + "§e joined for the first time! Welcome!!";
        }

        if (FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId()))
            message += "§7 (using Bedrock Edition)";
        else message += "§7 (using Java Edition)";

        event.joinMessage(ComponentUtils.legacy(message));

        YamlConfiguration yaml = PlayerUtils.getPlayerConfig(player.getUniqueId());

        if (!yaml.getBoolean("rulesagreed")) {
            Warp warp = ECraft.getInstance().getWarpHandler().getWarp("spawn");
            if (warp == null) {
                player.sendMessage("§cSpawn location not set! This should not be happening. Please report this to server staff.");
                return;
            }
            ECraft.getInstance().getTeleportHandler().teleport(player, warp.getLocation(), true);
            player.setGameMode(GameMode.ADVENTURE);

            if (!PlayerUtils.disagreedUsers.contains(player.getUniqueId())) {
                PlayerUtils.disagreedUsers.add(player.getUniqueId());
            }
        }

        yaml.set("totaljoins", yaml.getInt("totaljoins") + 1);
        yaml.set("lastname", player.getName());
        if (!yaml.isLong("joindate")) yaml.set("joindate", System.currentTimeMillis());
        PlayerUtils.savePlayerConfig(player.getUniqueId(), yaml);

        TablistHandler.sendTablist(player);

        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("default");
        team.addPlayer(player);

        String nickname = PlayerUtils.getNickname(player);
        if (nickname != null) {
            player.displayName(ComponentUtils.legacy(nickname + "§7~"));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ECraft.getInstance().getTeleportHandler().denyTeleport(event.getPlayer(), false);
        event.quitMessage(ComponentUtils.legacy("§e" + event.getPlayer().getName() + "§e left the game"));
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();

            if (!pvpPlayers.contains(damager.getUniqueId())) {
                damager.sendMessage(ComponentUtils.legacy("§c§l!§f You haven't enabled combats! Type §e/pvp§f to toggle combats."));
                event.setCancelled(true);
                return;
            }
            if (!pvpPlayers.contains(victim.getUniqueId())) {
                damager.sendMessage(ComponentUtils.legacy("§c§l!§f This player does not have combats enabled!"));
                event.setCancelled(true);
                return;
            }

            lastPvp.put(damager.getUniqueId(), System.currentTimeMillis());
            lastPvp.put(victim.getUniqueId(), System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onPlayerRegionLeave(RegionLeaveEvent event) {
        if (event.getMovementWay() == MovementWay.DISCONNECT) return;

        if (PlayerUtils.disagreedUsers.contains(event.getPlayer().getUniqueId())) {
            if (event.getRegion().getId().equals("spawn")) {
                Warp warp = ECraft.getInstance().getWarpHandler().getWarp("spawn");
                if (warp == null) {
                    event.getPlayer().sendMessage("§cSpawn location not set! This should not be happening. Please report this to server staff.");
                    return;
                }
                ECraft.getInstance().getTeleportHandler().teleport(event.getPlayer(), warp.getLocation(), true);
                event.getPlayer().sendMessage(ComponentUtils.legacy("§c§l!§f You cannot start playing yet! Please read server rules."));
            }
        }
    }
}
