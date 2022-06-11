package io.github.kabanfriends.ecraft.commands.impl;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import io.github.kabanfriends.ecraft.instances.Warp;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SpawnCommand extends AbstractCommand {

    public void run(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Warp warp = ECraft.getInstance().getWarpHandler().getWarp("spawn");
            if (warp == null) {
                player.sendMessage("§cSpawn location not set! This should not be happening. Please report this to server staff.");
                return;
            }

            if (ECraft.getInstance().getTeleportHandler().teleport(player, warp.getLocation())) {
                player.sendMessage(ComponentUtils.legacy("§a§l!§f Teleported to server spawn."));
            };
        }
    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
