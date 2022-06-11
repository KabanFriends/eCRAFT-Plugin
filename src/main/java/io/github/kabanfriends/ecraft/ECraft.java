package io.github.kabanfriends.ecraft;

import io.github.kabanfriends.ecraft.commands.impl.*;
import io.github.kabanfriends.ecraft.events.listeners.CommandEvents;
import io.github.kabanfriends.ecraft.events.listeners.PlayerEvents;
import io.github.kabanfriends.ecraft.events.listeners.ServerEvents;
import io.github.kabanfriends.ecraft.events.sources.RegionEventSource;
import io.github.kabanfriends.ecraft.handlers.*;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class ECraft extends JavaPlugin {

    private static ECraft instance;

    private CommandHandler cmdHandler;
    private MotdHandler motdHandler;
    private HomeHandler homeHandler;
    private WarpHandler warpHandler;
    private TeleportHandler tpHandler;
    private HelpHandler helpHandler;
    private TablistHandler tablistHandler;
    private AutoSaveHandler autoSaveHandler;

    private Economy econ;
    private Chat chat;

    @Override
    public void onEnable() {
        instance = this;

        //--- REGISTER EVENTS ---
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        getServer().getPluginManager().registerEvents(new ServerEvents(), this);
        getServer().getPluginManager().registerEvents(new RegionEventSource(), this);

        //--- CREATE FOLDERS & FILES ---
        try {
            //plugin base folder
            File pluginDir = new File("plugins/eCRAFT");
            if (!pluginDir.exists()) pluginDir.mkdir();

            //nbs songs folder
            File songsDir = new File("plugins/eCRAFT/songs");
            if (!songsDir.exists()) songsDir.mkdir();

            //homes folder
            File homesDir = new File("plugins/eCRAFT/homes");
            if (!homesDir.exists()) homesDir.mkdir();

            //warps folder
            File warpsDir = new File("plugins/eCRAFT/warps");
            if (!warpsDir.exists()) warpsDir.mkdir();

            //hints folder
            File hintsDir = new File("plugins/eCRAFT/hints");
            if (!hintsDir.exists()) hintsDir.mkdir();

            //playerdata folder
            File playerDataDir = new File("plugins/eCRAFT/playerdata");
            if (!playerDataDir.exists()) playerDataDir.mkdir();

            //icon folder
            File iconDir = new File("plugins/eCRAFT/icons");
            if (!iconDir.exists()) iconDir.mkdir();

            //motd list
            File motdList = new File("plugins/eCRAFT/motd.txt");
            if (!motdList.exists()) motdList.createNewFile();

        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Error while creating plugin files!");
            e.printStackTrace();
        }

        //--- REGISTER COMMANDS ---
        cmdHandler = new CommandHandler();

        cmdHandler.register("ecreload", new ReloadCommand());
        cmdHandler.register("ecdebug", new DebugCommand());
        cmdHandler.register("playsong", new PlaySongCommand());
        cmdHandler.register("stopsong", new StopSongCommand());
        cmdHandler.register("pvp", new PvpCommand());
        cmdHandler.register("sethome", new SetHomeCommand());
        cmdHandler.register("home", new HomeCommand());
        cmdHandler.register("setwarp", new SetWarpCommand());
        cmdHandler.register("delwarp", new DelWarpCommand());
        cmdHandler.register("warp", new WarpCommand());
        cmdHandler.register("warpinfo", new WarpInfoCommand());
        cmdHandler.register("tpa", new TpaCommand());
        cmdHandler.register("accept", new AcceptCommand());
        cmdHandler.register("deny", new DenyCommand());
        cmdHandler.register("spawn", new SpawnCommand());
        cmdHandler.register("help", new HelpCommand());
        cmdHandler.register("agree", new AgreeCommand());
        cmdHandler.register("disagree", new DisagreeCommand());
        cmdHandler.register("nickname", new NicknameCommand());
        cmdHandler.register("realname", new RealNameCommand());
        cmdHandler.register("setnickname", new SetNicknameCommand());

        //--- LOAD FILES &  ---
        initialize();

        //--- SET UP WORLDS ---
        setupWorld("world");
        setupWorld("world_nether");
        setupWorld("world_the_end");

        //--- CREATE SCOREBOARD TEAM ---
        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        if (sb.getTeam("default") == null) {
            Team team = sb.registerNewTeam("default");
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
    }

    @Override
    public void onDisable() {
        close();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return CommandEvents.onCommand(sender, cmd, args);
    }

    public void close() {
        warpHandler.close();
        homeHandler.close();
        helpHandler.close();
        tablistHandler.close();
        autoSaveHandler.close();
    }

    public void initialize() {
        motdHandler = new MotdHandler();
        homeHandler = new HomeHandler();
        warpHandler = new WarpHandler();
        tpHandler = new TeleportHandler();
        helpHandler = new HelpHandler();
        tablistHandler = new TablistHandler();
        autoSaveHandler = new AutoSaveHandler();

        RegisteredServiceProvider<Economy> rspEcon = getServer().getServicesManager().getRegistration(Economy.class);
        econ = rspEcon.getProvider();

        RegisteredServiceProvider<Chat> rspChat = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rspChat.getProvider();
    }

    public void saveWarps() {
        warpHandler.close();
        warpHandler = new WarpHandler();
    }

    public void saveHomes() {
        homeHandler.close();
        homeHandler = new HomeHandler();
    }

    public CommandHandler getCommandHandler() {
        return cmdHandler;
    }

    public MotdHandler getMotdHandler() {
        return motdHandler;
    }

    public HomeHandler getHomeHandler() {
        return homeHandler;
    }

    public WarpHandler getWarpHandler() {
        return warpHandler;
    }

    public TeleportHandler getTeleportHandler() {
        return tpHandler;
    }

    public HelpHandler getHelpHandler() {
        return helpHandler;
    }

    public TablistHandler getTablistHandler() {
        return tablistHandler;
    }

    public Economy getEconomy() {
        return econ;
    }

    public Chat getChat() {
        return chat;
    }

    public static void setupWorld(String name) {
        World world = Bukkit.getWorld(name);

        if (world != null) {
            world.setGameRule(GameRule.MOB_GRIEFING, false);
            world.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false);
            world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 50);
            world.setGameRule(GameRule.SPAWN_RADIUS, 0);
        }else {
            ECraft.getInstance().getLogger().log(Level.SEVERE, "Configuring World: World \"" + name + "\" does not exist!");
        }
    }

    public static ECraft getInstance() {
        return instance;
    }
}
