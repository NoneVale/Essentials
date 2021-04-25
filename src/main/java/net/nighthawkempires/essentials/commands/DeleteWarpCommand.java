package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.location.PublicLocationModel;
import net.nighthawkempires.core.user.UserModel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;

public class DeleteWarpCommand implements CommandExecutor {

    public DeleteWarpCommand() {
        getCommandManager().registerCommands("deletewarp", new String[] {
                "ne.warps.manage"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Delete Warp    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("delwarp", "<warp>", "Delete a warp"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel userModel = getUserRegistry().getUser(player.getUniqueId());
            PublicLocationModel publicLocationModel = getPublicLocationRegistry().getPublicLocations();

            if (!player.hasPermission("ne.warps.manage")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                case 1:
                    String name = args[0];
                    if (!publicLocationModel.warpExists(name)) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I could not find a warp that exists under the name "
                                + WHITE + name + GRAY + "."));
                        return true;
                    }

                    publicLocationModel.removeWarp(name);
                    player.sendMessage(getMessages().getChatMessage(GRAY + "Warp " + WHITE + name + GRAY + " has been deleted."));
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