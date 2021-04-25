package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.essentials.EssentialsPlugin;
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
import static net.nighthawkempires.essentials.EssentialsPlugin.getPlayerData;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class VanishCommand implements CommandExecutor {

    public VanishCommand() {
        getCommandManager().registerCommands("vanish", new String[] {
                "ne.vanish"
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.vanish")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    if (!getPlayerData().getVanishList().contains(player.getUniqueId())) {
                        getPlayerData().getVanishList().add(player.getUniqueId());
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            online.hidePlayer(EssentialsPlugin.getPlugin(), player);
                        }

                        player.sendMessage(getMessages().getChatMessage(GRAY + "You are now vanished."));
                        return true;
                    } else {
                        getPlayerData().getVanishList().remove(player.getUniqueId());
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            online.showPlayer(EssentialsPlugin.getPlugin(), player);
                        }

                        player.sendMessage(getMessages().getChatMessage(GRAY + "You are no longer vanished."));
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
