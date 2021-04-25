package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.lang.Messages;
import net.nighthawkempires.core.location.player.PlayerLocationModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;

public class HomesCommand implements CommandExecutor {

    public HomesCommand() {
        getCommandManager().registerCommands("homes", new String[] {
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

                    StringBuilder homeBuilder = new StringBuilder();
                    homeBuilder.append(ChatColor.translateAlternateColorCodes('&', "&8 - "));
                    for (int i = 0; i < playerLocationModel.getHomes().size(); i++) {
                        homeBuilder.append(GRAY).append(playerLocationModel.getHomes().get(i));

                        if (i < playerLocationModel.getHomes().size() - 1) {
                            homeBuilder.append(DARK_GRAY).append(", ");
                        }
                    }

                    if (playerLocationModel.getHomes().isEmpty()) {
                        homeBuilder.append(RED).append("None");
                    }

                    String[] list = new String[] {
                            getMessages().getMessage(Messages.CHAT_HEADER),
                            ChatColor.translateAlternateColorCodes('&', "&8List&7: Homes"),
                            getMessages().getMessage(Messages.CHAT_FOOTER),
                            ChatColor.translateAlternateColorCodes('&', "&8Homes&7: "),
                            homeBuilder.toString(),
                            getMessages().getMessage(Messages.CHAT_FOOTER)
                    };
                    player.sendMessage(list);
                    return true;
                case 1:
                    if (!player.hasPermission("ne.home.other")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    String targetName = args[1];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetName);
                    if (!getPlayerLocationRegistry().playerLocationsExist(offlinePlayer.getUniqueId())) {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                        return true;
                    }

                    playerLocationModel = getPlayerLocationRegistry().getPlayerLocations(offlinePlayer.getUniqueId());

                    homeBuilder = new StringBuilder();
                    homeBuilder.append(ChatColor.translateAlternateColorCodes('&', "&8 - "));
                    for (int i = 0; i < playerLocationModel.getHomes().size(); i++) {
                        homeBuilder.append(GRAY).append(playerLocationModel.getHomes().get(i));

                        if (i < playerLocationModel.getHomes().size() - 1) {
                            homeBuilder.append(DARK_GRAY).append(", ");
                        }
                    }

                    if (playerLocationModel.getHomes().isEmpty()) {
                        homeBuilder.append(RED).append("None");
                    }

                    list = new String[] {
                            getMessages().getMessage(Messages.CHAT_HEADER),
                            ChatColor.translateAlternateColorCodes('&', "&8List&7: " + offlinePlayer.getName() + "'s Homes"),
                            getMessages().getMessage(Messages.CHAT_FOOTER),
                            ChatColor.translateAlternateColorCodes('&', "&8Homes&7: "),
                            homeBuilder.toString(),
                            getMessages().getMessage(Messages.CHAT_FOOTER)
                    };
                    player.sendMessage(list);
                    return true;
                default:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(getMessages().getChatMessage(GRAY + "This command is not available from the console."));
            return true;
        }
        return false;
    }
}