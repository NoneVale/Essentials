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
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.core.lang.Messages.INVALID_SYNTAX;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class ExtinguishCommand implements CommandExecutor {


    public ExtinguishCommand() {
        getCommandManager().registerCommands("extinguish", new String[] {
                "ne.extinguish.self", "ne.extinguish.other"
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel userModel = getUserRegistry().getUser(player.getUniqueId());

            if (!player.hasPermission("ne.extinguish.self") && !player.hasPermission("ne.extinguish.other")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    if (!player.hasPermission("ne.extinguish.self")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    player.setFireTicks(0);
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have been extinguished."));
                    return true;
                case 1:
                    if (!player.hasPermission("ne.heal.other")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    String name = args[0];

                    if (name.toLowerCase().equals("*")) {
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            online.setFireTicks(0);
                            online.sendMessage(getMessages().getChatMessage(GRAY + "You have been extinguished."));
                        }
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have extinguished everyone."));
                        return true;
                    }

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (!offlinePlayer.isOnline()) {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    } else {
                        Player target = offlinePlayer.getPlayer();

                        target.setFireTicks(0);
                        player.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + GRAY + " has been extinguished."));
                        target.sendMessage(getMessages().getChatMessage(GRAY + "You have been extinguished."));
                        return true;
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
                            DARK_GRAY + "Command" + GRAY + ": Heal    " + DARK_GRAY + "    [Optional], <Required>",
                            getMessages().getMessage(CHAT_FOOTER),
                            getMessages().getCommand("heal", "<player>", "Feed a player"),
                            getMessages().getMessage(CHAT_FOOTER)
                    };

                    sender.sendMessage(help);
                    return true;
                case 1:
                    String name = args[0];

                    if (name.toLowerCase().equals("*")) {
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            online.setFireTicks(0);
                            online.sendMessage(getMessages().getChatMessage(GRAY + "You have been extinguished."));
                        }
                        sender.sendMessage(getMessages().getChatMessage(GRAY + "You have extinguished everyone."));
                        return true;
                    }

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (!offlinePlayer.isOnline()) {
                        sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    } else {
                        Player target = offlinePlayer.getPlayer();

                        target.setFireTicks(0);
                        sender.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + GRAY + " has been extinguished."));
                        target.sendMessage(getMessages().getChatMessage(GRAY + "You have been extinguished."));
                    }
                default:
                    sender.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }
        return false;
    }
}
