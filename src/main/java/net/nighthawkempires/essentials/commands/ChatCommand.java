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
import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.GRAY;

public class ChatCommand implements CommandExecutor {

    public ChatCommand() {
        getCommandManager().registerCommands("chat", new String[] {
                "ne.chat"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Clear Inventory    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("chat", "<message>", "Send a chat message"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.chat")) {
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

                    CorePlugin.getChatFormat().sendMessage(CorePlugin.getChatFormat().getFormattedMessage(player, builder.substring(0, builder.length() -1)));
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

                    CorePlugin.getChatFormat().sendMessage(CorePlugin.getChatFormat().getFormattedMessage(console, builder.substring(0, builder.length() -1)));
            }
        }
        return true;
    }
}
