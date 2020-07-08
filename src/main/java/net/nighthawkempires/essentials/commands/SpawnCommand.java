package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.location.PublicLocationModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.core.lang.Messages.INVALID_SYNTAX;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class SpawnCommand implements CommandExecutor {

    public SpawnCommand() {
        getCommandManager().registerCommands("spawn", new String[]{
                "ne.spawn", "ne.spawn.other"
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.spawn") || !player.hasPermission("ne.spawn.other")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            PublicLocationModel publicLocationModel = getPublicLocationRegistry().getPublicLocations();
            switch (args.length) {
                case 0:
                    if (!publicLocationModel.hasSpawn(player.getWorld())) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "There is not a spawn set for this current world."));
                        return true;
                    }

                    World world = player.getWorld();
                    if (!publicLocationModel.hasSpawn(world)) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "There is not a spawn set in your current world." +
                                "  Teleporting you to a different world if possible."));

                        for (World tpWorld : Bukkit.getWorlds()) {
                            if (publicLocationModel.hasSpawn(tpWorld)) {
                                world = tpWorld;
                                break;
                            }
                        }
                    }

                    player.teleport(publicLocationModel.getSpawn(world));
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have teleported to spawn."));
                    return true;
                case 1:
                    String name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (!offlinePlayer.isOnline()) {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }

                    Player target = offlinePlayer.getPlayer();

                    world = player.getWorld();
                    if (!publicLocationModel.hasSpawn(world)) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "There is not a spawn set in " + GREEN + target.getName() + "'s "
                                + GRAY + "current world.  Teleporting them to a different world if possible."));

                        for (World tpWorld : Bukkit.getWorlds()) {
                            if (publicLocationModel.hasSpawn(tpWorld)) {
                                world = tpWorld;
                                break;
                            }
                        }

                        if (!publicLocationModel.hasSpawn(world)) {
                            player.sendMessage(getMessages().getChatMessage(GRAY + "I could not find a world with a spawn set."));
                            return true;
                        }
                    }

                    target.teleport(publicLocationModel.getSpawn(world));
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have teleported " + GREEN + target.getName() + GRAY
                            + " to the spawn in world " + AQUA + world.getName() + GRAY + "."));
                    target.sendMessage(getMessages().getChatMessage(GRAY + "You have been teleported to spawn."));
                    return true;
                default:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }
        return false;
    }
}
