package io.github.kabanfriends.ecraft.commands.impl;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends AbstractCommand {

    public void run(CommandSender sender, Command cmd, String[] args) {
        ECraft.getInstance().close();
        ECraft.getInstance().initialize();
        sender.sendMessage("§aeCRAFT data files reloaded!");
    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
