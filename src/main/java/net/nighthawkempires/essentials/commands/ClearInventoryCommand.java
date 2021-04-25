package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.lang.Messages;
import net.nighthawkempires.core.lang.ServerMessage;
import net.nighthawkempires.core.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;
import static net.nighthawkempires.core.CorePlugin.*;

public class ClearInventoryCommand implements CommandExecutor {

    public ClearInventoryCommand() {
        getCommandManager().registerCommands("clearinventory", new String[] {
                "ne.clearinventory.self", "ne.clearinventory.other"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Clear Inventory    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("clearinv", "[player]", "Clear a player's inventory"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel userModel = getUserRegistry().getUser(player.getUniqueId());

            if (!player.hasPermission("ne.clearinventory.self") && !player.hasPermission("ne.clearinventory.other")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    if (!player.hasPermission("ne.clearinventory.self")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    player.getInventory().clear();
                    player.sendMessage(getMessages().getChatMessage(GRAY + "Your inventory has been cleared."));
                    return true;
                case 1:
                    if (!player.hasPermission("ne.clearinventory.other")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    String name = args[0];

                    if (name.toLowerCase().equals("help")) {
                        player.sendMessage(help);
                        return true;
                    }

                    OfflinePlayer offlinePlayerTarget = Bukkit.getOfflinePlayer(name);
                    if (!offlinePlayerTarget.isOnline()) {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    } else {
                        Player target = offlinePlayerTarget.getPlayer();
                        target.getInventory().clear();
                        player.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "inventory has been cleared."));
                        target.sendMessage(getMessages().getChatMessage(GRAY + "Your inventory has been cleared."));
                        return true;
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

                    if (name.toLowerCase().equals("help")) {
                        sender.sendMessage(help);
                        return true;
                    }

                    OfflinePlayer offlinePlayerTarget = Bukkit.getOfflinePlayer(name);
                    if (!offlinePlayerTarget.isOnline()) {
                        sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    } else {
                        Player target = offlinePlayerTarget.getPlayer();
                        target.getInventory().clear();
                        sender.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "inventory has been cleared."));
                        target.sendMessage(getMessages().getChatMessage(GRAY + "Your inventory has been cleared."));
                        return true;
                    }
                default:
                    sender.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }
        return false;
    }
}
