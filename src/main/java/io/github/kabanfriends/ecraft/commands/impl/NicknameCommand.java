package io.github.kabanfriends.ecraft.commands.impl;

import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import io.github.kabanfriends.ecraft.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class NicknameCommand extends AbstractCommand {

    public void run(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                PlayerUtils.setNickname(player, null);
                player.sendMessage(ComponentUtils.legacy("§a§l!§f Removed your nickname."));
            }else {
                String nick = String.join(" ", args);

                if (nick.equals(player.getName())) {
                    PlayerUtils.setNickname(player, null);
                    player.sendMessage(ComponentUtils.legacy("§a§l!§f Removed your nickname."));
                    return;
                }

                if (nick.length() > 20) {
                    player.sendMessage(ComponentUtils.legacy("§c§l!§f Nicknames cannot be longer than 20 characters!"));
                    return;
                }

                PlayerUtils.setNickname(player, nick);
                player.sendMessage(ComponentUtils.legacy("§a§l!§f Your nickname has been set to §e" + nick));
            }
        }
    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
