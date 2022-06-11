package io.github.kabanfriends.ecraft.commands.impl;

import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.lang.management.ManagementFactory;
import java.util.List;

public class DebugCommand extends AbstractCommand {

    public void run(CommandSender sender, Command cmd, String[] args) {
        long ram = ((com.sun.management.OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean()).getTotalMemorySize();

        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        long used = total - free;
        long max = Runtime.getRuntime().maxMemory();

        String message = "§aDebug Information:" + "\n"
                + "§fMachine RAM: §e" + ram / 1024 + " KB" + "\n"
                + "§fJVM Memory Usage:" + "\n"
                + "§7total: §6" + total / 1024 + " KB" + "\n"
                + "§7free: §6" + free / 1024 + " KB" + "\n"
                + "§7used: §6" + used / 1024 + " KB" + "\n"
                + "§7max: §6" + max / 1024 + " KB";

        sender.sendMessage(ComponentUtils.legacy(message));
    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
