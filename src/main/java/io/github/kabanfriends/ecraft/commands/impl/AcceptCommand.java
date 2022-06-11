package io.github.kabanfriends.ecraft.commands.impl;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AcceptCommand extends AbstractCommand {

    public void run(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof Player) {
            ECraft.getInstance().getTeleportHandler().acceptTeleport((Player) sender);
        }
    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
