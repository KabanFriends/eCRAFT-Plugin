package io.github.kabanfriends.ecraft.commands.impl;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import io.github.kabanfriends.ecraft.instances.Warp;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import io.github.kabanfriends.ecraft.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WarpInfoCommand extends AbstractCommand {

    public void run(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage("/warpinfo <warp>");
            }else {
                String name = args[0];
                Warp warp = ECraft.getInstance().getWarpHandler().getWarp(name);

                if (warp == null) {
                    player.sendMessage(ComponentUtils.legacy("§cInvalid warp!"));
                    return;
                }

                player.sendMessage("Warp info:");
                player.sendMessage("§7Name: §6" + warp.getName());
                player.sendMessage("§7Owner: §6" + PlayerUtils.getLastSeenName(warp.getOwner()) + " §8(" + warp.getOwner() + "§8)");

                try {
                    File file = new File("plugins/eCRAFT/warps/" + warp.getName().toLowerCase() + ".yml");
                    BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                    SimpleDateFormat sdf = new SimpleDateFormat();

                    player.sendMessage("§7Created: §6" + sdf.format(new Date(attrs.creationTime().toMillis())));
                }catch (IOException e) {
                    player.sendMessage("§7Created: §cError");
                    e.printStackTrace();
                }
            }
        }
    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
