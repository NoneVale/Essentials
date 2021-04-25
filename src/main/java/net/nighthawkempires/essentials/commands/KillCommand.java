package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.Console;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.DARK_GRAY;

public class KillCommand implements CommandExecutor {

    public KillCommand() {
        getCommandManager().registerCommands("kill", new String[] {
                "ne.kill.self", "ne.kill.other"
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel userModel = getUserRegistry().getUser(player.getUniqueId());

            if (!player.hasPermission("ne.kill.self") && !player.hasPermission("ne.kill.other")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    if (!player.hasPermission("ne.kill.self")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    player.setFoodLevel(0);
                    player.setHealth(0);
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have been killed."));
                    return true;
                case 1:
                    if (!player.hasPermission("ne.kill.other")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    String name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (!offlinePlayer.isOnline()) {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    } else {
                        Player target = offlinePlayer.getPlayer();
                        target.setFoodLevel(0);
                        target.setHealth(0);
                        player.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + GRAY + " has been killed."));
                        target.sendMessage(getMessages().getChatMessage(GRAY + "You have been killed."));
                    }
                default:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            switch (args.length) {
                case 0:
                    String[] help = new String[] {
                            getMessages().getMessage(CHAT_HEADER),
                            DARK_GRAY + "Command" + GRAY + ": Kill    " + DARK_GRAY + "    [Optional], <Required>",
                            getMessages().getMessage(CHAT_FOOTER),
                            getMessages().getCommand("kill", "<player>", "Kill a player"),
                            getMessages().getMessage(CHAT_FOOTER)
                    };

                    sender.sendMessage(help);
                    return true;
                case 1:
                    String name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (!offlinePlayer.isOnline()) {
                        sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    } else {
                        Player target = offlinePlayer.getPlayer();
                        target.setFoodLevel(0);
                        target.setHealth(0);
                        sender.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + GRAY + " has been killed."));
                        target.sendMessage(getMessages().getChatMessage(GRAY + "You have been killed."));
                    }
                default:
                    sender.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }
        return false;
    }
}
