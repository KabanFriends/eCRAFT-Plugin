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

public class SetNicknameCommand extends AbstractCommand {

    public void run(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage("/setnick <player> [<nickname>]");
            }else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage("§cInvalid player!");
                    return;
                }

                if (args.length >= 2) {
                    String[] nickArgs = new String[args.length - 1];
                    System.arraycopy(args, 1, nickArgs, 0, args.length - 1);
                    String nick = String.join(" ", nickArgs);

                    PlayerUtils.setNickname(target, nick);
                    player.sendMessage(ComponentUtils.legacy("§aNickname of " + target.getName() + " has been changed to §e" + nick));
                }else {
                    PlayerUtils.setNickname(target, null);
                    player.sendMessage(ComponentUtils.legacy("§aNickname of " + target.getName() + " has been cleared."));
                }
            }
        }
    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
