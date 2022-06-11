package io.github.kabanfriends.ecraft.commands.impl;

import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class StopSongCommand extends AbstractCommand {

    public void run(CommandSender sender, Command cmd, String[] args) {
        if (PlaySongCommand.songPlayer != null) {
            if (PlaySongCommand.songPlayer.isPlaying()) {
                PlaySongCommand.songPlayer.setPlaying(false);
                PlaySongCommand.songPlayer.destroy();
                sender.sendMessage("§aSong stopped.");
                return;
            }
        }
        sender.sendMessage("§cNo song is playing right now!");
    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
