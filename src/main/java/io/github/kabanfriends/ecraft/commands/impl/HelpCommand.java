package io.github.kabanfriends.ecraft.commands.impl;

import io.github.kabanfriends.ecraft.ECraft;
import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import io.github.kabanfriends.ecraft.handlers.HelpHandler;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HelpCommand extends AbstractCommand {

    private static final int HELPS_PER_PAGE = 8;

    public void run(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                listHelps(player, 1);
            }else {
                String name = args[0];
                if (StringUtils.isNumeric(name)) {
                    listHelps(player, Integer.parseInt(args[0]));
                    return;
                }

                if (!ECraft.getInstance().getHelpHandler().getHelpCategories().containsKey(name)) {
                    player.sendMessage(ComponentUtils.legacy("§c§l!§f Help category §e" + name + "§f doesn't exist!"));
                    return;
                }

                File file = new File("plugins/eCRAFT/hints/" + name.toLowerCase() + ".yml");
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

                String title = yaml.getString("title");
                List<String> descriptions = yaml.getStringList("descriptions");

                String message = "&a&l!&f Viewing help - &e" + title + "&r:" + "\n"
                        + String.join("\n&r", descriptions);
                player.sendMessage(ComponentUtils.legacyAmpersand(message));
            }
        }
    }

    public void listHelps(Player player, int page) {
        HelpHandler handler = ECraft.getInstance().getHelpHandler();
        Map<String, String> categories = handler.getHelpCategories();
        String[] helpNames = handler.getHelpCategories().keySet().toArray(new String[0]);
        int size = helpNames.length;
        int totalPages = (int)Math.ceil((double)size / HELPS_PER_PAGE);
        if (page < 1 || page > totalPages) {
            player.sendMessage(ComponentUtils.legacy("§c§l!§f Invalid page!"));
            return;
        }
        int start = (HELPS_PER_PAGE * (page - 1)) + 1;
        int end = start + HELPS_PER_PAGE - 1;
        if (end > size) end = size;

        ArrayList<String> displayNames = new ArrayList<>();
        for (int i = 0; i < end - start + 1; i++) {
            String name = helpNames[start + i - 1];

            displayNames.add("§b/help " + name + "§8 - §7About §f" + categories.get(name));
        }

        String message = "§a§l!§f Viewing §b" + start + "-" + end + "§f help categories out of §b" + size + "§f, Page " + page + "/" + totalPages + ":" + "\n"
                + String.join("\n", displayNames) + "\n"
                + "§7→ Type §6/help <page>§7 to view other pages.";

        player.sendMessage(ComponentUtils.legacy(message));

    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        if (args.length == 1) return new ArrayList<>(ECraft.getInstance().getHelpHandler().getHelpCategories().keySet());
        return null;
    }
}
