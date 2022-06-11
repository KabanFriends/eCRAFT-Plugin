package io.github.kabanfriends.ecraft.commands.impl;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import io.github.kabanfriends.ecraft.instances.Warp;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class DelWarpCommand extends AbstractCommand {

    public void run(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(ComponentUtils.legacy("§e§l!§f Usage: §a/delwarp <name>"));
                player.sendMessage(ComponentUtils.legacy("§7→ Deletes the specified warp."));
            }else {
                String name = args[0];
                Warp warp = ECraft.getInstance().getWarpHandler().getWarp(name);

                if (warp == null) {
                    player.sendMessage(ComponentUtils.legacy("§c§l!§f Warp §e" + name + "§f does not exist!"));
                    return;
                }
                if (!warp.getOwner().equals(player.getUniqueId()) && !player.hasPermission("ecraft.delwarp.force")) {
                    player.sendMessage(ComponentUtils.legacy("§c§l!§f Cannot delete warps that you don't own!"));
                    return;
                }

                ECraft.getInstance().getWarpHandler().deleteWarp(name);
                player.sendMessage(ComponentUtils.legacy("§a§l!§f Warp §e" + warp.getName() + "§f has been deleted!"));
            }
        }
    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
