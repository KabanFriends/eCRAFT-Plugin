package io.github.kabanfriends.ecraft.commands.impl;

import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import io.github.kabanfriends.ecraft.events.listeners.PlayerEvents;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PvpCommand extends AbstractCommand {

    public void run(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (PlayerEvents.pvpPlayers.contains(player.getUniqueId())) {
                if (PlayerEvents.lastPvp.containsKey(player.getUniqueId())) {
                    long time = PlayerEvents.lastPvp.get(player.getUniqueId());
                    if (System.currentTimeMillis() - time > 10 * 1000L) {
                    }else {
                        player.sendMessage(ComponentUtils.legacy("§c§l!§f You cannot disable combats while you are fighting with other players! Please try again a few seconds."));
                        return;
                    }
                }

                PlayerEvents.pvpPlayers.remove(player.getUniqueId());
                player.sendMessage(ComponentUtils.legacy("§e§l!§f Combats are now disabled for you! Other players cannot hurt you now."));
            }else {
                PlayerEvents.pvpPlayers.add(player.getUniqueId());
                player.sendMessage(ComponentUtils.legacy("§a§l!§f Combats are now enabled for you!"));
                player.sendMessage(ComponentUtils.legacy("§a§l!§f To fight with other players, the opponent also needs to have combats enabled."));
            }
        }
    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
