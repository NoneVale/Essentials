package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.CorePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.getCommandManager;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;
import static net.nighthawkempires.essentials.EssentialsPlugin.*;

public class MeCommand implements CommandExecutor {

    public MeCommand() {
        getCommandManager().registerCommands("me", new String[] {
                "ne.me"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Me    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("me", "<message>", "Express an action"),
            getMessages().getMessage(CHAT_FOOTER),
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.me")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                default:
                    StringBuilder messageBuilder = new StringBuilder();
                    for (String s : args) {
                        messageBuilder.append(s).append(" ");
                    }

                    getMessages().broadcatServerMessage(DARK_GRAY + "" + ITALIC + "** " + BLUE + "" + ITALIC + player.getName()
                            + GRAY + " " + ITALIC + messageBuilder.toString().trim() + DARK_GRAY  + "" + ITALIC + " **");
                    return true;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            switch (args.length) {
                case 0:
                    sender.sendMessage(help);
                    return true;
                default:
                    StringBuilder messageBuilder = new StringBuilder();
                    for (String s : args) {
                        messageBuilder.append(s).append(" ");
                    }

                    getMessages().broadcatServerMessage(DARK_GRAY + "" + ITALIC + "** " + BLUE + "" + ITALIC + CorePlugin.getConfigg().getConsoleDisplayName()
                            + GRAY + " " + ITALIC + messageBuilder.toString().trim() + DARK_GRAY  + "" + ITALIC + " **");
                    return true;
            }
        }
        return false;
    }
}
