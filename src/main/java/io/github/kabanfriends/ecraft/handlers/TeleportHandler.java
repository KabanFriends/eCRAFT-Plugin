package io.github.kabanfriends.ecraft.handlers;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class TeleportHandler {

    private final int COOLDOWN_SECONDS = 15;
    private final int TPA_TIMEOUT = 60;

    private HashMap<UUID, Long> lastTp = new HashMap<>();
    private HashMap<UUID, UUID> tpRequests = new HashMap<>();
    private HashMap<UUID, BukkitTask> tpTasks = new HashMap<>();

    public boolean teleport(Player player, Location location, boolean bypass) {
        if (!bypass) {
            if (lastTp.containsKey(player.getUniqueId())) {
                long time = lastTp.get(player.getUniqueId());

                long diff = (System.currentTimeMillis() - time) / 1000L;
                if (!player.hasPermission("ecraft.tpcooldown.bypass")) {
                    if (diff < COOLDOWN_SECONDS) {
                        player.sendMessage(ComponentUtils.legacy("§c§l!§f Teleportation commands are in cooldown! Please wait §e" + (COOLDOWN_SECONDS - diff) + " seconds§f before using this command again."));
                        return false;
                    }
                }
            }

            if (!player.hasPermission("ecraft.tpcooldown.bypass")) {
                if (!player.isOnGround()) {
                    player.sendMessage(ComponentUtils.legacy("§c§l!§f You can only use this command while you are standing on a ground!"));
                    return false;
                }
            }

            lastTp.put(player.getUniqueId(), System.currentTimeMillis());
        }

        player.teleportAsync(location, PlayerTeleportEvent.TeleportCause.COMMAND);
        return true;
    }

    public boolean teleport(Player player, Location location) {
        return teleport(player, location, false);
    }

    public void resetCooldown(Player player) {
        lastTp.remove(player.getUniqueId());
    }

    public void requestTeleport(Player player, Player target) {
        if (tpRequests.containsKey(target.getUniqueId())) {
            UUID requester = tpRequests.get(target.getUniqueId());
            if (player.getUniqueId().equals(requester)) {
                player.sendMessage(ComponentUtils.legacy("§c§l!§f You already have an ongoing teleport request to this player!"));
            }else {
                player.sendMessage(ComponentUtils.legacy("§c§l!§f This player already has an ongoing teleport request from someone else!"));
            }
            return;
        }

        BukkitTask task = new BukkitRunnable() {
            public void run() {
                tpRequests.remove(target.getUniqueId());
                tpTasks.remove(target.getUniqueId());

                target.sendMessage(ComponentUtils.legacy("§c§l!§f The teleport request you had received from §e" + player.getName() + "§f expired!"));
                player.sendMessage(ComponentUtils.legacy("§c§l!§f The teleport request you had sent to §e" + target.getName() + "§f expired!"));
            }
        }.runTaskLater(ECraft.getInstance(), TPA_TIMEOUT * 20);

        tpRequests.put(target.getUniqueId(), player.getUniqueId());
        tpTasks.put(target.getUniqueId(), task);

        player.sendMessage(ComponentUtils.legacy("§a§l!§f You sent a teleport request to §e" + target.getName() + "§f. Please wait until the player responds.\n§7(This request will be expired in 1 minute.)"));
        target.sendMessage(ComponentUtils.legacy("§a§l!§e " + player.getName() + "§f wants to teleport to you!\nType §a/accept§f or §c/deny§f to respond.\n§7(This request will be expired in 1 minute.)"));
    }

    public void acceptTeleport(Player player) {
        if (tpRequests.containsKey(player.getUniqueId())) {
            UUID requesterId = tpRequests.get(player.getUniqueId());
            BukkitTask task = tpTasks.get(player.getUniqueId());
            Player requester = Bukkit.getPlayer(requesterId);
            task.cancel();

            player.sendMessage(ComponentUtils.legacy("§a§l!§f Accepted the teleport request from §e" + requester.getName() + "§f!"));
            requester.sendMessage(ComponentUtils.legacy("§a§l!§e " + player.getName() + "§f accepted your teleport request!"));
            requester.teleportAsync(player.getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);

            tpRequests.remove(player.getUniqueId());
            tpTasks.remove(player.getUniqueId());
        }else {
            player.sendMessage(ComponentUtils.legacy("§c§l!§f You don't have an ongoing teleport request!"));
        }
    }

    public void denyTeleport(Player player, boolean sendMsg) {
        if (tpRequests.containsKey(player.getUniqueId())) {
            UUID requesterId = tpRequests.get(player.getUniqueId());
            BukkitTask task = tpTasks.get(player.getUniqueId());
            Player requester = Bukkit.getPlayer(requesterId);
            task.cancel();

            if (sendMsg) player.sendMessage(ComponentUtils.legacy("§c§l!§f Denied the teleport request from §e" + requester.getName() + "§f."));
            requester.sendMessage(ComponentUtils.legacy("§c§l!§e " + player.getName() + "§f denied your teleport request."));

            tpRequests.remove(player.getUniqueId());
            tpTasks.remove(player.getUniqueId());
        }else {
            if (sendMsg) player.sendMessage(ComponentUtils.legacy("§c§l!§f You don't have an ongoing teleport request!"));
        }
    }
}
