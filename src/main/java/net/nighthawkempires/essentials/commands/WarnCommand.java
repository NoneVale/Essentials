package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.kick.Kick;
import net.nighthawkempires.core.user.UserModel;
import net.nighthawkempires.core.warning.Warning;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.core.lang.Messages.PLAYER_NOT_ONLINE;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class WarnCommand implements CommandExecutor {

    public WarnCommand() {
        getCommandManager().registerCommands("warn", new String[] {
                "ne.warn"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Warn    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("warn", "<player> [reason]", "Warn a player"),
            getMessages().getMessage(CHAT_FOOTER),
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.warn")) {
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
                    if (!getUserRegistry().userExists(offlinePlayer.getUniqueId())) {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                        return true;
                    }

                    UserModel targetUserModel = getUserRegistry().getUser(offlinePlayer.getUniqueId());

                    Warning warning = Warning.getWarning(player.getUniqueId(), "Unspecified", System.currentTimeMillis());
                    targetUserModel.warn(warning);

                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have warned " + GREEN + offlinePlayer.getName()
                            + GRAY + " for " + YELLOW + warning.getWarnReason() + GRAY + "."));
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();

                        target.sendMessage(getMessages().getChatMessage(GRAY + "You have been warned by " + GREEN + player.getName()
                                + GRAY + " for " + YELLOW + warning.getWarnReason() + GRAY + "."));
                    }
                    return true;
                default:
                    name = args[0];
                    offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (!getUserRegistry().userExists(offlinePlayer.getUniqueId())) {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                        return true;
                    }

                    targetUserModel = getUserRegistry().getUser(offlinePlayer.getUniqueId());

                    StringBuilder reason = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        reason.append(args[i]);
                        if (i < args.length - 1)
                            reason.append(" ");
                    }

                    warning = Warning.getWarning(player.getUniqueId(), reason.toString().trim(), System.currentTimeMillis());
                    targetUserModel.warn(warning);

                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have warned " + GREEN + offlinePlayer.getName()
                            + GRAY + " for " + YELLOW + warning.getWarnReason() + GRAY + "."));
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();

                        target.sendMessage(getMessages().getChatMessage(GRAY + "You have been warned by " + GREEN + player.getName()
                                + GRAY + " for " + YELLOW + warning.getWarnReason() + GRAY + "."));
                    }
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
                    if (!getUserRegistry().userExists(offlinePlayer.getUniqueId())) {
                        sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                        return true;
                    }

                    UserModel targetUserModel = getUserRegistry().getUser(offlinePlayer.getUniqueId());

                    Warning warning = Warning.getWarning(CorePlugin.getConfigg().getConsoleUuid(), "Unspecified", System.currentTimeMillis());
                    targetUserModel.warn(warning);

                    sender.sendMessage(getMessages().getChatMessage(GRAY + "You have warned " + GREEN + offlinePlayer.getName()
                            + GRAY + " for " + YELLOW + warning.getWarnReason() + GRAY + "."));
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();

                        target.sendMessage(getMessages().getChatMessage(GRAY + "You have been warned by " + GREEN + CorePlugin.getConfigg().getConsoleDisplayName()
                                + GRAY + " for " + YELLOW + warning.getWarnReason() + GRAY + "."));
                    }
                    return true;
                default:
                    name = args[0];
                    offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (!getUserRegistry().userExists(offlinePlayer.getUniqueId())) {
                        sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                        return true;
                    }

                    targetUserModel = getUserRegistry().getUser(offlinePlayer.getUniqueId());

                    StringBuilder reason = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        reason.append(args[i]);
                        if (i < args.length - 1)
                            reason.append(" ");
                    }

                    warning = Warning.getWarning(CorePlugin.getConfigg().getConsoleUuid(), reason.toString().trim(), System.currentTimeMillis());
                    targetUserModel.warn(warning);

                    sender.sendMessage(getMessages().getChatMessage(GRAY + "You have warned " + GREEN + offlinePlayer.getName()
                            + GRAY + " for " + YELLOW + warning.getWarnReason() + GRAY + "."));
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();

                        target.sendMessage(getMessages().getChatMessage(GRAY + "You have been warned by " + GREEN + CorePlugin.getConfigg().getConsoleDisplayName()
                                + GRAY + " for " + YELLOW + warning.getWarnReason() + GRAY + "."));
                    }
                    return true;
            }
        }
        return false;
    }
}
