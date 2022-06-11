package io.github.kabanfriends.ecraft.commands.impl;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import io.github.kabanfriends.ecraft.commands.AbstractCommand;
import io.github.kabanfriends.ecraft.utils.ComponentUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class PlaySongCommand extends AbstractCommand {

    public static RadioSongPlayer songPlayer;

    public void run(CommandSender sender, Command cmd, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("/playsong <file>");
        }else {
            String filename = String.join(" ", args);
            if (!filename.endsWith(".nbs")) filename += ".nbs";

            File nbs = new File("plugins/eCRAFT/songs/" + filename);
            if (!nbs.exists() || !nbs.isFile()) {
                sender.sendMessage("§cInvalid filename!");
                return;
            }

            if (songPlayer != null) {
                if (songPlayer.isPlaying()) {
                    songPlayer.setPlaying(false);
                    songPlayer.destroy();
                }
            }

            Song song = NBSDecoder.parse(nbs);
            songPlayer = new RadioSongPlayer(song);
            for (Player player : Bukkit.getOnlinePlayers()) {
                songPlayer.addPlayer(player);
            }
            songPlayer.setPlaying(true);
            songPlayer.setAutoDestroy(true);

            Bukkit.broadcast(ComponentUtils.legacy("§aPlaying song:§r " + filename));
        }
    }

    public List<String> tabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
