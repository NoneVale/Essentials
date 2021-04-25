package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.location.PublicLocationModel;
import net.nighthawkempires.core.user.UserModel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.INVALID_SYNTAX;
import static net.nighthawkempires.core.lang.Messages.NO_PERMS;
import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.GRAY;

public class DeleteSpawnCommand implements CommandExecutor {

    public DeleteSpawnCommand() {
        getCommandManager().registerCommands("deletespawn", new String[] {
                "ne.spawn.manage"
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel userModel = getUserRegistry().getUser(player.getUniqueId());
            PublicLocationModel publicLocationModel = getPublicLocationRegistry().getPublicLocations();

            if (!player.hasPermission("ne.spawn.manage")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    if (!publicLocationModel.hasSpawn(player.getWorld())) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "There is not a spawn currently set for this world."));
                        return true;
                    }

                    publicLocationModel.removeSpawn(player.getWorld());
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have removed the spawn for world "
                            + AQUA + player.getWorld().getName() + GRAY + "."));
                    return true;
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
