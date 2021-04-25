package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.lang.Messages;
import org.apache.logging.log4j.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.getCommandManager;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;

public class BroadcastCommand implements CommandExecutor {

    public BroadcastCommand() {
        getCommandManager().registerCommands("broadcast", new String[] {
                "ne.broadcast"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Broadcast    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("broadcast", "<message>", "Broadcast a message"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.broadcast")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                default:
                    StringBuilder builder = new StringBuilder();
                    for (String string : args) {
                        builder.append(string).append(" ");
                    }

                    for (Player online : Bukkit.getOnlinePlayers()) {
                        online.sendMessage(getMessages().getChatTag(CHAT_HEADER));
                        online.sendMessage(getMessages().getChatMessage(GREEN + player.getName() + GRAY + ": " + builder.substring(0, builder.length() - 1)));
                        online.sendMessage(getMessages().getChatTag(CHAT_FOOTER));
                    }
                    return true;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender console = (ConsoleCommandSender) sender;

            switch (args.length) {
                case 0:
                    console.sendMessage(help);
                    return true;
                default:
                    StringBuilder builder = new StringBuilder();
                    for (String string : args) {
                        builder.append(string).append(" ");
                    }

                    for (Player online : Bukkit.getOnlinePlayers()) {
                        online.sendMessage(getMessages().getChatTag(CHAT_HEADER));
                        online.sendMessage(getMessages().getChatMessage(GREEN + CorePlugin.getSettingsRegistry().getConfig().getConsoleDisplayName()) + GRAY + ": " + builder.substring(0, builder.length() - 1));
                        online.sendMessage(getMessages().getChatTag(CHAT_FOOTER));
                    }
            }
        }
        return true;
    }
}