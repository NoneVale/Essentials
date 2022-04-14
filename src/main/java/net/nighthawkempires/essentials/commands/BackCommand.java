package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.lang.Messages;
import net.nighthawkempires.essentials.EssentialsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("ne.back")) {
                player.sendMessage(CorePlugin.getMessages().getChatTag(Messages.NO_PERMS));
                return true;
            }

            if (args.length == 0) {
                if (EssentialsPlugin.getPlayerData().getPreviousLocationMap().containsKey(player.getUniqueId())) {
                    player.teleport(EssentialsPlugin.getPlayerData().getPreviousLocationMap().get(player.getUniqueId()));
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have teleported back to your last location."));
                    return true;
                } else {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "No previous location was found."));
                    return true;
                }
            } else {
                player.sendMessage(CorePlugin.getMessages().getChatTag(Messages.INVALID_SYNTAX));
                return true;
            }
        }
        return false;
    }
}
