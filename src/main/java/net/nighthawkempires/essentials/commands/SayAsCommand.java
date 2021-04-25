package net.nighthawkempires.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.essentials.EssentialsPlugin.*;
import static org.bukkit.ChatColor.*;

public class SayAsCommand implements CommandExecutor {

    public SayAsCommand() {
        getCommandManager().registerCommands("sayas", new String[] {
                "ne.sayas"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Say As    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("sayas", "<player> <message>", "Send a message as a different player"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.sayas")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                case 1:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
                default:
                    String name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();

                        StringBuilder message = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            message.append(args[i]).append(" ");
                        }

                        target.chat(message.toString().trim());
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You forced " + GREEN + target.getName() + GRAY
                                + " to say: " + message.toString().trim() + GRAY + "."));
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
                    sender.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
                default:
                    String name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();

                        StringBuilder message = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            message.append(args[i]).append(" ");
                        }

                        target.chat(message.toString().trim());
                        sender.sendMessage(getMessages().getChatMessage(GRAY + "You forced " + GREEN + target.getName() + GRAY
                                + " to say: " + message.toString().trim() + GRAY + "."));
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
