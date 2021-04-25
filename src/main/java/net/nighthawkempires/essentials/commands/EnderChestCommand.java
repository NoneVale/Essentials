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

public class EnderChestCommand implements CommandExecutor {

    public EnderChestCommand() {
        getCommandManager().registerCommands("enderchest", new String[] {
                "ne.enderchest.self", "ne.enderchest.other"
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel userModel = getUserRegistry().getUser(player.getUniqueId());

            if (!player.hasPermission("ne.enderchest.self") && !player.hasPermission("ne.enderchest.other")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    if (!player.hasPermission("ne.enderchest.self")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    player.openInventory(player.getEnderChest());
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have opened your ender chest."));
                    return true;
                case 1:
                    if (!player.hasPermission("ne.enderchest.other")) {
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
                        player.openInventory(target.getEnderChest());
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have opened " + GREEN + target.getName() + "'s " + GRAY + " ender chest."));
                        return true;
                    }
                default:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(getMessages().getChatMessage(GRAY + "This command is not available from the console."));
            return true;
        }
        return false;
    }
}
