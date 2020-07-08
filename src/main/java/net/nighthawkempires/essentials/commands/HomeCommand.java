package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.location.player.PlayerLocationModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;

public class HomeCommand implements CommandExecutor {

    public HomeCommand() {
        getCommandManager().registerCommands("home", new String[] {
                "ne.home.self", "ne.home.other"
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.home.self") && !player.hasPermission("ne.home.other")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            PlayerLocationModel playerLocationModel = getPlayerLocationRegistry().getPlayerLocations(player.getUniqueId());
            switch (args.length) {
                case 0:
                    if (!player.hasPermission("ne.home.self")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    String name = "home";
                    if (!playerLocationModel.homeExists(name)) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but it seems that you do not have a home set under the name "
                                + WHITE + name + GRAY + "."));
                        return true;
                    }

                    player.teleport(playerLocationModel.getHome(name), PlayerTeleportEvent.TeleportCause.COMMAND);
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have teleported to home " + WHITE + name + GRAY + "."));
                    return true;
                case 1:
                    if (!player.hasPermission("ne.home.self")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    name = args[0].toLowerCase();
                    if (!playerLocationModel.homeExists(name)) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but it seems that you do not have a home set under the name "
                                + WHITE + name + GRAY + "."));
                        return true;
                    }

                    player.teleport(playerLocationModel.getHome(name), PlayerTeleportEvent.TeleportCause.COMMAND);
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have teleported to home " + WHITE + name + GRAY + "."));
                    return true;
                case 2:
                    if (!player.hasPermission("ne.home.other")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    name = args[0].toLowerCase();

                    String targetName = args[1];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetName);
                    if (!getPlayerLocationRegistry().playerLocationsExist(offlinePlayer.getUniqueId())) {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                        return true;
                    }

                    playerLocationModel = getPlayerLocationRegistry().getPlayerLocations(offlinePlayer.getUniqueId());

                    if (!playerLocationModel.homeExists(name)) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but it seems that " + GREEN + offlinePlayer.getName()
                                + GRAY + " does not have a home set under the name " + WHITE + name + GRAY + "."));
                        return true;
                    }

                    player.teleport(playerLocationModel.getHome(name), PlayerTeleportEvent.TeleportCause.COMMAND);
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have teleported to " + GREEN + offlinePlayer.getName() + "'s "
                            + GRAY + " home " + WHITE + name + GRAY + "."));
                    return true;
                default:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }
        return false;
    }
}
