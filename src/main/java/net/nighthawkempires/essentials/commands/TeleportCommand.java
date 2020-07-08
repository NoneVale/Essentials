package net.nighthawkempires.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import static net.nighthawkempires.core.CorePlugin.getCommandManager;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class TeleportCommand implements CommandExecutor {

    public TeleportCommand() {
        getCommandManager().registerCommands("teleport", new String[] {
                "ne.teleport"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Teleport    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("teleport", "<player>", "Teleport to a player"),
            getMessages().getCommand("teleport", "<player> <targetPlayer>", "Teleport a player to a player"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.teleport")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                case 1:
                    String name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();

                        player.teleport(target);
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have teleported to " + GREEN + target.getName() + GRAY + "."));
                        return true;
                    } else {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }
                case 2:
                    name = args[0];
                    offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();

                        name = args[1];
                        offlinePlayer = Bukkit.getOfflinePlayer(name);
                        if (offlinePlayer.isOnline()) {
                            Player targetTo = offlinePlayer.getPlayer();

                            target.teleport(targetTo, PlayerTeleportEvent.TeleportCause.COMMAND);
                            player.sendMessage(getMessages().getChatMessage(GRAY + "You have teleported " + GREEN + target.getName()
                                    + GRAY + " to " + GREEN + targetTo.getName() + GRAY + "."));
                            target.sendMessage(getMessages().getChatMessage(GRAY + "You have been teleported to " + GREEN + targetTo.getName() + GRAY + "."));
                            return true;
                        } else {
                            player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                            return true;
                        }
                    } else {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }
                default:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }
        return false;
    }
}
