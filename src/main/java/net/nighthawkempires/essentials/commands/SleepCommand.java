package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.lang.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SleepCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("ne.sleep")) {
                player.sendMessage(CorePlugin.getMessages().getChatTag(Messages.NO_PERMS));
                return true;
            }

            player.sleep(player.getLocation().subtract(0, 1, 0), true);
        }
        return false;
    }
}
