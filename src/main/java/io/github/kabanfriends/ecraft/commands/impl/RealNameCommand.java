package io.github.kabanfriends.ecraft.commands.impl;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import io.github.kabanfriends.ecraft.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class RealNameCommand extends AbstractCommand {

    public void run(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            List<UUID> list = ECraft.getInstance().getTablistHandler().getRealNameTargets();

            if (list.contains(player.getUniqueId())) {
                player.sendMessage(ComponentUtils.legacy("§a§l!§f The player list now displays §bnicknames§f."));
                list.remove(player.getUniqueId());
            }else {
                player.sendMessage(ComponentUtils.legacy("§a§l!§f The player list now displays §bactual usernames§f."));
                list.add(player.getUniqueId());
            }
        }
    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
