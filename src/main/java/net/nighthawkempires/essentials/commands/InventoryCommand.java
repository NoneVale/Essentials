package net.nighthawkempires.essentials.commands;

import com.sun.org.apache.bcel.internal.generic.PUSH;
import net.nighthawkempires.core.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;

public class InventoryCommand implements CommandExecutor {

    public InventoryCommand() {
        getCommandManager().registerCommands("inventory", new String[]{
                "ne.inventory.self", "ne.inventory.other"
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel userModel = getUserRegistry().getUser(player.getUniqueId());

            if (!player.hasPermission("ne.inventory.self") && !player.hasPermission("ne.inventory.other")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    if (!player.hasPermission("ne.inventory.self")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    player.openInventory(player.getInventory());
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have opened your inventory."));
                    return true;
                case 1:
                    if (!player.hasPermission("ne.inventory.other")) {
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
                        player.openInventory(target.getInventory());
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have opened " + GREEN + target.getName() + "'s " + GRAY + " inventory."));
                        return true;
                    }
                default:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }
        return false;
    }
}