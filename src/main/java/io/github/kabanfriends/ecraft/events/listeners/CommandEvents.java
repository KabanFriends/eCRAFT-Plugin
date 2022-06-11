package io.github.kabanfriends.ecraft.events.listeners;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import io.github.kabanfriends.ecraft.handlers.CommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandEvents implements TabCompleter {

    public static boolean onCommand(CommandSender sender, Command cmd, String[] args) {
        String name = cmd.getName().toLowerCase();

        CommandHandler cmdHandler = ECraft.getInstance().getCommandHandler();
        if (cmdHandler.getCommands().containsKey(name)) {
            AbstractCommand command = cmdHandler.getCommands().get(name);

            command.run(sender, cmd, args);
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        String name = cmd.getName().toLowerCase();

        CommandHandler cmdHandler = ECraft.getInstance().getCommandHandler();
        if (cmdHandler.getCommands().containsKey(name)) {
            AbstractCommand command = cmdHandler.getCommands().get(name);

            return command.tabComplete(sender, cmd, args);
        }
        return null;
    }
}
