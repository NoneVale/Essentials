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

public class SetWarpCommand implements CommandExecutor {

    public SetWarpCommand() {
        getCommandManager().registerCommands("setwarp", new String[]{
                "ne.warps.manage"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Set Warp    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("setwarp", "<warp>", "Set a warp"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.warps.manage")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                case 1:
                    String name = args[0].toLowerCase();

                    PublicLocationModel publicLocationModel = getPublicLocationRegistry().getPublicLocations();

                    if (publicLocationModel.warpExists(name)) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but a warp under the name "
                                + WHITE + name + GRAY + " already exists."));
                        return true;
                    }

                    publicLocationModel.setWarp(name, player.getLocation());
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have set warp " + WHITE + name + GRAY + "."));
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
