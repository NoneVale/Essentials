package net.nighthawkempires.essentials;

import com.google.common.collect.Lists;
import net.nighthawkempires.essentials.commands.*;
import net.nighthawkempires.essentials.data.PlayerData;
import net.nighthawkempires.essentials.kit.registry.FKitRegistry;
import net.nighthawkempires.essentials.kit.registry.KitRegistry;
import net.nighthawkempires.essentials.listeners.PlayerListener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class EssentialsPlugin extends JavaPlugin {

    private static KitRegistry kitRegistry;

    private static List<Player> godmode;

    private static PlayerData playerData;

    private static Plugin plugin;

    public void onEnable() {
        plugin = this;

        kitRegistry = new FKitRegistry();
        kitRegistry.loadAllFromDb();

        godmode = Lists.newArrayList();

        playerData = new PlayerData();

        registerCommands();
        registerListeners();
    }

    public void onDisable() {

    }

    public void registerCommands() {
        this.getCommand("ban").setExecutor(new BanCommand());
        this.getCommand("chat").setExecutor(new ChatCommand());
        this.getCommand("clearinventory").setExecutor(new ClearInventoryCommand());
        this.getCommand("craft").setExecutor(new CraftCommand());
        this.getCommand("createkit").setExecutor(new CreateKitCommand());
        this.getCommand("delhome").setExecutor(new DeleteHomeCommand());
        this.getCommand("delkit").setExecutor(new DeleteKitCommand());
        this.getCommand("delspawn").setExecutor(new DeleteSpawnCommand());
        this.getCommand("delwarp").setExecutor(new DeleteWarpCommand());
        this.getCommand("deop").setExecutor(new DeOPCommand());
        this.getCommand("enderchest").setExecutor(new EnderChestCommand());
        this.getCommand("feed").setExecutor(new FeedCommand());
        this.getCommand("fly").setExecutor(new FlyCommand());
        this.getCommand("gamemode").setExecutor(new GamemodeCommand());
        this.getCommand("give").setExecutor(new GiveCommand());
        this.getCommand("godmode").setExecutor(new GodmodeCommand());
        this.getCommand("heal").setExecutor(new HealCommand());
        this.getCommand("help").setExecutor(new HelpCommand());
        this.getCommand("home").setExecutor(new HomeCommand());
        this.getCommand("homes").setExecutor(new HomesCommand());
        this.getCommand("inventory").setExecutor(new InventoryCommand());
        this.getCommand("kick").setExecutor(new KickCommand());
        this.getCommand("kill").setExecutor(new KillCommand());
        this.getCommand("kit").setExecutor(new KitCommand());
        this.getCommand("kits").setExecutor(new KitsCommand());
        this.getCommand("me").setExecutor(new MeCommand());
        this.getCommand("message").setExecutor(new MessageCommand());
        this.getCommand("mute").setExecutor(new MuteCommand());
        this.getCommand("nickname").setExecutor(new NicknameCommand());
        this.getCommand("op").setExecutor(new OPCommand());
        this.getCommand("record").setExecutor(new RecordCommand());
        this.getCommand("rename").setExecutor(new RenameCommand());
        this.getCommand("repair").setExecutor(new RepairCommand());
        this.getCommand("reply").setExecutor(new ReplyCommand());
        this.getCommand("sayas").setExecutor(new SayAsCommand());
        this.getCommand("scoreboard").setExecutor(new ScoreboardCommand());
        this.getCommand("sethome").setExecutor(new SetHomeCommand());
        this.getCommand("setspawn").setExecutor(new SetSpawnCommand());
        this.getCommand("setwarp").setExecutor(new SetWarpCommand());
        this.getCommand("slap").setExecutor(new SlapCommand());
        this.getCommand("smite").setExecutor(new SmiteCommand());
        this.getCommand("spawn").setExecutor(new SpawnCommand());
        this.getCommand("spawnmob").setExecutor(new SpawnMobCommand());
        this.getCommand("speed").setExecutor(new SpeedCommand());
        this.getCommand("sudo").setExecutor(new SudoCommand());
        this.getCommand("teleport").setExecutor(new TeleportCommand());
        this.getCommand("teleporthere").setExecutor(new TeleportHereCommand());
        this.getCommand("time").setExecutor(new TimeCommand());
        this.getCommand("tpaccept").setExecutor(new TPAcceptCommand());
        this.getCommand("tpa").setExecutor(new TPACommand());
        this.getCommand("unban").setExecutor(new UnbanCommand());
        this.getCommand("unmute").setExecutor(new UnmuteCommand());
        this.getCommand("vanish").setExecutor(new VanishCommand());
        this.getCommand("warn").setExecutor(new WarnCommand());
        this.getCommand("warp").setExecutor(new WarpCommand());
        this.getCommand("warps").setExecutor(new WarpsCommand());
        this.getCommand("weather").setExecutor(new WeatherCommand());
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static KitRegistry getKitRegistry() {
        return kitRegistry;
    }

    public static List<Player> getGodmode() {
        return godmode;
    }

    public static PlayerData getPlayerData() {
        return playerData;
    }
}