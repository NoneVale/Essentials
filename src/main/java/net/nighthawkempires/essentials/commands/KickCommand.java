package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.kick.Kick;
import net.nighthawkempires.core.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;

public class KickCommand implements CommandExecutor {

    public KickCommand() {
        getCommandManager().registerCommands("kick", new String[] {
                "ne.kick"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Kick    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("kick", "<player> [reason]", "Kick a player"),
            getMessages().getMessage(CHAT_FOOTER),
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.kick")) {
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

                        UserModel userModel = getUserRegistry().getUser(target.getUniqueId());
                        Kick kick = Kick.getKick(player.getUniqueId(), "Unspecified", System.currentTimeMillis());
                        userModel.kick(kick);
                        target.kickPlayer(kick.getKickInfo());
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have kicked " + GREEN + target.getName()
                                + GRAY + " for " + YELLOW + "Unspecified" + GRAY + "."));
                        getMessages().broadcatServerMessage(GREEN + target.getName() + GRAY + " has been kicked by " + GREEN
                                + player.getName() + GRAY + " for " + YELLOW + "Unspecified" + GRAY + ".");
                        return true;
                    } else {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }
                default:
                    name = args[0];
                    offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();

                        StringBuilder reason = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            reason.append(args[i]);
                            if (i < args.length - 1)
                                reason.append(" ");
                        }

                        UserModel userModel = getUserRegistry().getUser(target.getUniqueId());
                        Kick kick = Kick.getKick(player.getUniqueId(), reason.toString().trim(), System.currentTimeMillis());
                        userModel.kick(kick);
                        target.kickPlayer(kick.getKickInfo());
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have kicked " + GREEN + target.getName()
                                + GRAY + " for " + YELLOW + kick.getKickReason() + GRAY + "."));
                        getMessages().broadcatServerMessage(GREEN + target.getName() + GRAY + " has been kicked by " + GREEN
                                + player.getName() + GRAY + " for " + YELLOW + kick.getKickReason() + GRAY + ".");
                        return true;
                    } else {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            switch (args.length) {
                case 0:
                    sender.sendMessage(help);
                    return true;
                case 1:
                    String name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();

                        UserModel userModel = getUserRegistry().getUser(target.getUniqueId());
                        Kick kick = Kick.getKick(CorePlugin.getConfigg().getConsoleUuid(), "Unspecified", System.currentTimeMillis());
                        userModel.kick(kick);
                        target.kickPlayer(kick.getKickInfo());
                        sender.sendMessage(getMessages().getChatMessage(GRAY + "You have kicked " + GREEN + target.getName()
                                + GRAY + " for " + YELLOW + "Unspecified" + GRAY + "."));
                        getMessages().broadcatServerMessage(GREEN + target.getName() + GRAY + " has been kicked by " + GREEN
                                + CorePlugin.getConfigg().getConsoleDisplayName() + GRAY + " for " + YELLOW + "Unspecified" + GRAY + ".");
                        return true;
                    } else {
                        sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }
                default:
                    name = args[0];
                    offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();

                        StringBuilder reason = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            reason.append(args[i]);
                            if (i < args.length - 1)
                                reason.append(" ");
                        }

                        UserModel userModel = getUserRegistry().getUser(target.getUniqueId());
                        Kick kick = Kick.getKick(CorePlugin.getConfigg().getConsoleUuid(), reason.toString().trim(), System.currentTimeMillis());
                        userModel.kick(kick);
                        target.kickPlayer(kick.getKickInfo());
                        sender.sendMessage(getMessages().getChatMessage(GRAY + "You have kicked " + GREEN + target.getName()
                                + GRAY + " for " + YELLOW + kick.getKickReason() + GRAY + "."));
                        getMessages().broadcatServerMessage(GREEN + target.getName() + GRAY + " has been kicked by " + GREEN
                                + CorePlugin.getConfigg().getConsoleDisplayName() + GRAY + " for " + YELLOW + kick.getKickReason() + GRAY + ".");
                        return true;
                    } else {
                        sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }
            }
        }
        return false;
    }
}
