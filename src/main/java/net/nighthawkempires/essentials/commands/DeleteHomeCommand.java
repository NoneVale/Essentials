package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.location.player.PlayerLocationModel;
import net.nighthawkempires.core.location.player.registry.PlayerLocationRegistry;
import net.nighthawkempires.core.user.UserModel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;

public class DeleteHomeCommand implements CommandExecutor {

    public DeleteHomeCommand() {
        getCommandManager().registerCommands("deletehome", new String[] {
                "ne.home"
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel userModel = getUserRegistry().getUser(player.getUniqueId());
            PlayerLocationModel playerLocationModel = getPlayerLocationRegistry().getPlayerLocations(player.getUniqueId());

            if (!player.hasPermission("ne.home")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    if (!playerLocationModel.homeExists("home")) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I could not find a home that exists under the name "
                                + WHITE + "home" + GRAY + " for you."));
                        return true;
                    }

                    playerLocationModel.removeHome("home");
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have removed home " + WHITE + "home" + GRAY + "."));
                    return true;
                case 1:
                    String name = args[0].toLowerCase();
                    if (!playerLocationModel.homeExists(name)) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I could not find a home that exists under the name "
                                + WHITE + name + GRAY + " for you."));
                        return true;
                    }

                    playerLocationModel.removeHome(name);
                    player.sendMessage(getMessages().getChatMessage(GRAY + "Home " + WHITE + name + GRAY + " has been deleted."));
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
