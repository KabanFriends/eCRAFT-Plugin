package io.github.kabanfriends.ecraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class AbstractCommand {

    public abstract void run(CommandSender sender, Command cmd, String[] args);

    public abstract List<String> tabComplete(CommandSender sender, Command cmd, String[] args);

}
