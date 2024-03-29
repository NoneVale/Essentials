package net.nighthawkempires.essentials.commands;

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

public class FeedCommand implements CommandExecutor {

    public FeedCommand() {
        getCommandManager().registerCommands("feed", new String[] {
                "ne.feed.self", "ne.feed.other"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Feed    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("feed", "<player>", "Feed a player"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel userModel = getUserRegistry().getUser(player.getUniqueId());

            if (!player.hasPermission("ne.feed.self") && !player.hasPermission("ne.feed.other")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    if (!player.hasPermission("ne.feed.self")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    player.setFoodLevel(20);
                    player.sendMessage(getMessages().getChatMessage(GRAY + "Your hunger has been sated."));
                    return true;
                case 1:
                    if (!player.hasPermission("ne.feed.other")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    String name = args[0];

                    if (name.toLowerCase().equals("*")) {
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            online.setFoodLevel(20);
                            online.sendMessage(getMessages().getChatMessage(GRAY + "Your hunger has been sated."));
                        }
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have sated every ones hunger."));
                        return true;
                    }

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (!offlinePlayer.isOnline()) {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    } else {
                        Player target = offlinePlayer.getPlayer();
                        target.setFoodLevel(20);
                        player.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "hunger has been sated."));
                        target.sendMessage(getMessages().getChatMessage(GRAY + "Your hunger has been sated."));
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

                    if (name.toLowerCase().equals("*")) {
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            online.setFoodLevel(20);
                            online.sendMessage(getMessages().getChatMessage(GRAY + "Your hunger has been sated."));
                        }
                        sender.sendMessage(getMessages().getChatMessage(GRAY + "You have sated every ones hunger."));
                        return true;
                    }

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (!offlinePlayer.isOnline()) {
                        sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    } else {
                        Player target = offlinePlayer.getPlayer();
                        target.setFoodLevel(20);
                        sender.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "hunger has been sated."));
                        target.sendMessage(getMessages().getChatMessage(GRAY + "Your hunger has been sated."));
                    }
                default:
                    sender.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }

        return false;
    }
}
