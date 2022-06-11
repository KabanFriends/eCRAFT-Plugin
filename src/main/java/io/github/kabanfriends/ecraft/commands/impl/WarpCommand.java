package io.github.kabanfriends.ecraft.commands.impl;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import io.github.kabanfriends.ecraft.handlers.WarpHandler;
import io.github.kabanfriends.ecraft.instances.Warp;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WarpCommand extends AbstractCommand {

    private static final int WARPS_PER_PAGE = 25;

    public void run(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                listWarps(player, 1);
            }else {
                String name = args[0];
                if (StringUtils.isNumeric(name)) {
                    listWarps(player, Integer.parseInt(args[0]));
                    return;
                }

                Warp warp = ECraft.getInstance().getWarpHandler().getWarp(name);
                if (warp == null) {
                    player.sendMessage(ComponentUtils.legacy("§c§l!§f Warp §e" + name + "§f does not exist!"));
                    return;
                }

                if (ECraft.getInstance().getTeleportHandler().teleport(player, warp.getLocation())) {
                    player.sendMessage(ComponentUtils.legacy("§a§l!§f Teleported to warp §e" + warp.getName() + "§f."));
                }
            }
        }
    }

    public void listWarps(Player player, int page) {
        WarpHandler handler = ECraft.getInstance().getWarpHandler();
        String[] warpNames = handler.getWarpNames();
        int size = warpNames.length;
        int totalPages = (int)Math.ceil((double)size / WARPS_PER_PAGE);
        if (page < 1 || page > totalPages) {
            player.sendMessage(ComponentUtils.legacy("§c§l!§f Invalid page!"));
            return;
        }
        int start = (WARPS_PER_PAGE * (page - 1)) + 1;
        int end = start + WARPS_PER_PAGE - 1;
        if (end > size) end = size;

        ArrayList<String> displayNames = new ArrayList<>();
        for (int i = 0; i < end - start + 1; i++) {
            String name = warpNames[start + i - 1];
            Warp warp = handler.getWarp(name);
            if (warp != null) {
                displayNames.add((warp.getOwner().equals(player.getUniqueId()) ? "§d" : "§e") + warp.getName());
            }
        }

        String message = "§a§l!§f Viewing §b" + start + "-" + end + "§f warps out of §b" + size + "§f, Page " + page + "/" + totalPages + ":" + "\n"
                + String.join("§f, ", displayNames) + "\n"
                + "§7→ Type §6/warp <page>§7 to view other pages." + "\n"
                + "§7→ Type §6/warp <name>§7 to warp." + "\n"
                + "§7→§d Pink§7 names are warps you own.";

        player.sendMessage(ComponentUtils.legacy(message));

    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        if (args.length == 1) return Arrays.asList(ECraft.getInstance().getWarpHandler().getWarpNames());
        return null;
    }
}
