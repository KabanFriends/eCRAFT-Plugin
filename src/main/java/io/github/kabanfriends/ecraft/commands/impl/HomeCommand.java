package io.github.kabanfriends.ecraft.commands.impl;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import io.github.kabanfriends.ecraft.instances.Home;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HomeCommand extends AbstractCommand {

    public void run(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Home home = ECraft.getInstance().getHomeHandler().getHome(player.getUniqueId());
            if (home == null) {
                player.sendMessage(ComponentUtils.legacy("§c§l!§f You haven't set your home yet! Use §e/sethome§f to set your home location."));
                return;
            }

            if (ECraft.getInstance().getTeleportHandler().teleport(player, home.getLocation())) {
                player.sendMessage(ComponentUtils.legacy("§a§l!§f Teleported to your home."));
            };
        }
    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
