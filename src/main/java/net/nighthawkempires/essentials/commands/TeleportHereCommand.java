package net.nighthawkempires.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import static net.nighthawkempires.core.CorePlugin.getCommandManager;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class TeleportHereCommand implements CommandExecutor {

    public TeleportHereCommand() {
        getCommandManager().registerCommands("teleporthere", new String[] {
                "ne.teleporthere"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Teleport Here    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("tphere", "<player>", "Teleport a player to you"),
            getMessages().getCommand("tphere", "*", "Teleport all players to you"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.teleporthere")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                case 1:
                    String name = args[0];
                    switch (name.toLowerCase()) {
                        case "*":
                            for (Player online : Bukkit.getOnlinePlayers()) {
                                if (online.getUniqueId() != player.getUniqueId()) {
                                    online.teleport(player);
                                    online.sendMessage(getMessages().getChatMessage(GRAY + "You have been teleported to " + GREEN + player.getName() + GRAY + "."));
                                }
                            }
                            player.sendMessage(getMessages().getChatMessage(GRAY + "You have teleported everyone to your location."));
                            return true;
                        default:
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                            if (offlinePlayer.isOnline()) {
                                Player target = offlinePlayer.getPlayer();

                                target.teleport(player);
                                player.sendMessage(getMessages().getChatMessage(GRAY + "You have teleported " + GREEN + target.getName() + GRAY + " to you."));
                                target.sendMessage(getMessages().getChatMessage(GRAY + "You have been teleported to " + GREEN + player.getName() + GREEN + "."));
                                return true;
                            } else {
                                player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                                return true;
                            }
                    }
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
