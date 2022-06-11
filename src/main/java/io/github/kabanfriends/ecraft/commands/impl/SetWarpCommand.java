package io.github.kabanfriends.ecraft.commands.impl;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import io.github.kabanfriends.ecraft.instances.Warp;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetWarpCommand extends AbstractCommand {

    public void run(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(ComponentUtils.legacy("§e§l!§f Usage: §a/setwarp <name>"));
                player.sendMessage(ComponentUtils.legacy("§7→ Creates a new warp."));
            }else {
                String name = args[0];

                if (StringUtils.isNumeric(name)) {
                    player.sendMessage(ComponentUtils.legacy("§c§l!§f You cannot use number-only warp names!"));
                    return;
                }
                if (name.length() > 16) {
                    player.sendMessage(ComponentUtils.legacy("§c§l!§f Warp name is too long!"));
                    return;
                }
                if (!name.matches("^[a-zA-Z0-9\\-_]*$")) {
                    player.sendMessage(ComponentUtils.legacy("§c§l!§f Invalid characters found in the warp name!"));
                    return;
                }
                if (ECraft.getInstance().getWarpHandler().getWarp(name) != null) {
                    player.sendMessage(ComponentUtils.legacy("§c§l!§f A warp with that name already exists!"));
                    return;
                }

                Warp warp = new Warp();
                warp.setName(name);
                warp.setOwner(player.getUniqueId());
                warp.setLocation(player.getLocation());

                ECraft.getInstance().getWarpHandler().setWarp(warp);
                player.sendMessage(ComponentUtils.legacy("§a§l!§f New warp §e" + name + "§f has been created!"));
            }
        }
    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
