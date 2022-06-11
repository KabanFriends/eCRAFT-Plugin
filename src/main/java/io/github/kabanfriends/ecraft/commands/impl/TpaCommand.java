package io.github.kabanfriends.ecraft.commands.impl;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TpaCommand extends AbstractCommand {

    public void run(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(ComponentUtils.legacy("§e§l!§f Usage: §a/tpa <player>\n§7→ Request to teleport to the specified player."));
            }else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(ComponentUtils.legacy("§c§l!§f Could not find a player with name §e" + args[0] + "§f."));
                    return;
                }
                if (player.getName().equals(target.getName())) {
                    player.sendMessage(ComponentUtils.legacy("§c§l!§f You cannot send a teleport request to yourself!"));
                    return;
                }

                ECraft.getInstance().getTeleportHandler().requestTeleport(player, target);
            }
        }
    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
