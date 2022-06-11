package io.github.kabanfriends.ecraft.commands.impl;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import io.github.kabanfriends.ecraft.instances.Warp;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import io.github.kabanfriends.ecraft.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class DisagreeCommand extends AbstractCommand {

    public void run(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage("/disagree <player>");
            }else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage("§cInvalid player!");
                    return;
                }

                YamlConfiguration yaml = PlayerUtils.getPlayerConfig(target.getUniqueId());
                yaml.set("rulesagreed", false);
                PlayerUtils.savePlayerConfig(target.getUniqueId(), yaml);

                target.sendMessage(ComponentUtils.legacy("§c§l!§f You were forced to disagree to server rules! Please carefully read the server rules again."));

                Warp warp = ECraft.getInstance().getWarpHandler().getWarp("spawn");
                if (warp == null) {
                    target.sendMessage("§cSpawn location not set! This should not be happening. Please report this to server staff.");
                    return;
                }

                ECraft.getInstance().getTeleportHandler().teleport(player, warp.getLocation(), true);
                if (!PlayerUtils.disagreedUsers.contains(target.getUniqueId())) {
                    PlayerUtils.disagreedUsers.add(target.getUniqueId());
                }
            }
        }
    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
