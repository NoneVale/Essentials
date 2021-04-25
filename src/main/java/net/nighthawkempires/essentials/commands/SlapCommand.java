package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static net.nighthawkempires.core.CorePlugin.getCommandManager;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;

public class SlapCommand implements CommandExecutor {

    public SlapCommand() {
        getCommandManager().registerCommands("slap", new String[]{
                "ne.slap"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Slap    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("slap", "<player> [-d|-h]", "Slap a player"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.slap")) {
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
                    if (!offlinePlayer.isOnline()) {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }

                    Player target = offlinePlayer.getPlayer();

                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have slapped " + GREEN + target.getName() + GRAY + "."));
                    getMessages().broadcatServerMessage(GREEN + target.getName() + GRAY + " has been slapped by " + GREEN + player.getName() + GRAY + ".");
                    return true;
                case 2:
                    switch (args[1].toLowerCase()) {
                        case "-d":
                            name = args[0];
                            offlinePlayer = Bukkit.getOfflinePlayer(name);
                            if (!offlinePlayer.isOnline()) {
                                player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                                return true;
                            }

                            target = offlinePlayer.getPlayer();
                            target.setHealth(target.getHealth() - 0.5);
                            target.setVelocity(target.getLocation().getDirection().multiply(-1.5).setY(0.5));

                            player.sendMessage(getMessages().getChatMessage(GRAY + "You have slapped " + GREEN + target.getName() + GRAY + "."));
                            getMessages().broadcatServerMessage(GREEN + target.getName() + GRAY + " has been slapped by " + GREEN + player.getName() + GRAY + ".");
                            return true;
                        case "-h":
                            name = args[0];
                            offlinePlayer = Bukkit.getOfflinePlayer(name);
                            if (!offlinePlayer.isOnline()) {
                                player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                                return true;
                            }

                            target = offlinePlayer.getPlayer();
                            target.setVelocity(new Vector(0, 5, 0));
                            target.setHealth(target.getHealth() - 0.1);

                            player.sendMessage(getMessages().getChatMessage(GRAY + "You have slapped " + GREEN + target.getName() + GRAY + "."));
                            getMessages().broadcatServerMessage(GREEN + target.getName() + GRAY + " has been slapped by " + GREEN + player.getName() + GRAY + ".");
                            return true;
                        default:
                            player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                            return true;
                    }
                default:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            switch (args.length) {
                case 0:
                    sender.sendMessage(help);
                    return true;
                case 1:
                    String name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (!offlinePlayer.isOnline()) {
                        sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }

                    Player target = offlinePlayer.getPlayer();

                    sender.sendMessage(getMessages().getChatMessage(GRAY + "You have slapped " + GREEN + target.getName() + GRAY + "."));
                    getMessages().broadcatServerMessage(GREEN + target.getName() + GRAY + " has been slapped by " + GREEN + CorePlugin.getConfigg().getConsoleDisplayName() + GRAY + ".");
                    return true;
                case 2:
                    switch (args[1].toLowerCase()) {
                        case "-d":
                            name = args[0];
                            offlinePlayer = Bukkit.getOfflinePlayer(name);
                            if (!offlinePlayer.isOnline()) {
                                sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                                return true;
                            }

                            target = offlinePlayer.getPlayer();
                            target.setHealth(target.getHealth() - 0.5);
                            target.setVelocity(target.getLocation().getDirection().multiply(-1.5).setY(0.5));

                            sender.sendMessage(getMessages().getChatMessage(GRAY + "You have slapped " + GREEN + target.getName() + GRAY + "."));
                            getMessages().broadcatServerMessage(GREEN + target.getName() + GRAY + " has been slapped by " + GREEN + CorePlugin.getConfigg().getConsoleDisplayName() + GRAY + ".");
                            return true;
                        case "-h":
                            name = args[0];
                            offlinePlayer = Bukkit.getOfflinePlayer(name);
                            if (!offlinePlayer.isOnline()) {
                                sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                                return true;
                            }

                            target = offlinePlayer.getPlayer();
                            target.setVelocity(new Vector(0, 5, 0));
                            target.setHealth(target.getHealth() - 0.1);

                            sender.sendMessage(getMessages().getChatMessage(GRAY + "You have slapped " + GREEN + target.getName() + GRAY + "."));
                            getMessages().broadcatServerMessage(GREEN + target.getName() + GRAY + " has been slapped by " + GREEN + CorePlugin.getConfigg().getConsoleDisplayName() + GRAY + ".");
                            return true;
                        default:
                            sender.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                            return true;
                    }
                default:
                    sender.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }
        return false;
    }
}
