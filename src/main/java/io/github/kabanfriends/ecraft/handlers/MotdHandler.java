package io.github.kabanfriends.ecraft.handlers;

import io.github.kabanfriends.ecraft.ECraft;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.util.CachedServerIcon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class MotdHandler {

    private List<CachedServerIcon> iconList;
    private List<String> motdList;
    private Random random;

    public MotdHandler() {
        loadMotd();
    }

    public void loadMotd() {
        random = new Random();
        motdList = new ArrayList<>();

        ECraft.getInstance().getLogger().log(Level.INFO, "Loading MOTD...");

        try {
            File file = new File("plugins/eCRAFT/motd.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                motdList.add(line);
            }
        }catch (IOException e) {
            e.printStackTrace();
            ECraft.getInstance().getLogger().log(Level.SEVERE, "Error while loading MOTD!");
            motdList.add("missingno");
        }

        ECraft.getInstance().getLogger().log(Level.INFO, "Loading server icons...");

        iconList = new ArrayList<>();
        File iconDir = new File("plugins/eCRAFT/icons");

        try {
            if (iconDir.exists() && iconDir.isDirectory()) {
                for (File file : iconDir.listFiles()) {
                    if (file.getName().endsWith(".png")) {
                        BufferedImage bimg = ImageIO.read(file);
                        int width          = bimg.getWidth();
                        int height         = bimg.getHeight();

                        if (width == 64 && height == 64) {
                            iconList.add(Bukkit.loadServerIcon(file));
                        }else {
                            ECraft.getInstance().getLogger().log(Level.WARNING, file.getName() + ": Invalid image size! Server icon must be 64x64.");
                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            ECraft.getInstance().getLogger().log(Level.SEVERE, "Error while loading server icons!");
        }
    }

    public String getRandomMotd() {
        return motdList.get(random.nextInt(motdList.size()));
    }

    public CachedServerIcon getRandomIcon() {
        return iconList.get(random.nextInt(iconList.size()));
    }

}
