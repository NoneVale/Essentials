package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.location.PublicLocationModel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.essentials.EssentialsPlugin.*;
import static org.bukkit.ChatColor.*;


public class SetSpawnCommand implements CommandExecutor {

    public SetSpawnCommand() {
        getCommandManager().registerCommands("setspawn", new String[] {
                "ne.spawn.manage"
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.spawn.manage")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    PublicLocationModel publicLocationModel = getPublicLocationRegistry().getPublicLocations();

                    publicLocationModel.setSpawn(player.getLocation());
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have set the spawn for world " + AQUA + player.getWorld().getName() + GRAY + "."));
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
