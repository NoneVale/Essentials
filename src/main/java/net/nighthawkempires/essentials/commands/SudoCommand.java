package net.nighthawkempires.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.getCommandManager;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class SudoCommand implements CommandExecutor {

    public SudoCommand() {
        getCommandManager().registerCommands("sudo", new String[] {
                "ne.sudo"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Sudo    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("sudo", "<player> <message>", "Run a command as a different player"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.sudo")) {
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

                        Bukkit.dispatchCommand(target, message.toString().trim());
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You forced " + GREEN + target.getName()
                                + GRAY + " to run command: " + message.toString().trim() + GRAY + "."));
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

                        Bukkit.dispatchCommand(target, message.toString().trim());
                        sender.sendMessage(getMessages().getChatMessage(GRAY + "You forced " + GREEN + target.getName()
                                + GRAY + " to run command: " + message.toString().trim() + GRAY + "."));
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
