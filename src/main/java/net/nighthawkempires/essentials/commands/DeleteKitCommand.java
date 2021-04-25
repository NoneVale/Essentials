package net.nighthawkempires.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.essentials.EssentialsPlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;

public class DeleteKitCommand implements CommandExecutor {

    public DeleteKitCommand() {
        getCommandManager().registerCommands("deletekit", new String[] {
                "ne.kits.manage"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Delete Kit    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("deletekit", "help", "Show this help menu"),
            getMessages().getCommand("deletekit", "<name>", "Delete a kit"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.kits.manage")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                case 1:
                    String name = args[0];

                    if (!getKitRegistry().kitExists(name)) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but there isn't a kit that exists with that name."));
                        return true;
                    }

                    getKitRegistry().deleteKit(name);
                    player.sendMessage(getMessages().getChatMessage(GRAY + "Kit " + AQUA + name + GRAY + " has been deleted."));
                    return true;
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

                    if (!getKitRegistry().kitExists(name)) {
                        sender.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but there isn't a kit that exists with that name."));
                        return true;
                    }

                    getKitRegistry().deleteKit(name);
                    sender.sendMessage(getMessages().getChatMessage(GRAY + "Kit " + AQUA + name + GRAY + " has been deleted."));
                    return true;
                default:
                    sender.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }
        return false;
    }
}