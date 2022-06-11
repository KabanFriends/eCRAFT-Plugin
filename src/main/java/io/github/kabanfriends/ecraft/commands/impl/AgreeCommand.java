package io.github.kabanfriends.ecraft.commands.impl;

import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import io.github.kabanfriends.ecraft.utils.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class AgreeCommand extends AbstractCommand {

    public void run(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            YamlConfiguration yaml = PlayerUtils.getPlayerConfig(player.getUniqueId());
            if (yaml.getBoolean("rulesagreed")) {
                player.sendMessage(ComponentUtils.legacy("§c§l!§f You have already agreed to server rules!"));
                return;
            }

            player.sendMessage(ComponentUtils.legacy("§a§l!§f You agreed to server rules! Enjoy your Minecraft life in eCRAFT 2.0! :D"));
            if (player.getGameMode() == GameMode.ADVENTURE) player.setGameMode(GameMode.SURVIVAL);

            yaml.set("rulesagreed", true);
            PlayerUtils.savePlayerConfig(player.getUniqueId(), yaml);
            PlayerUtils.disagreedUsers.remove(player.getUniqueId());
        }
    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
