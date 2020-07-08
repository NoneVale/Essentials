package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.location.PublicLocationModel;
import net.nighthawkempires.essentials.kit.KitModel;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.core.lang.Messages.INVALID_SYNTAX;
import static net.nighthawkempires.essentials.EssentialsPlugin.getKitRegistry;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class WarpCommand implements CommandExecutor {

    public WarpCommand() {
        getCommandManager().registerCommands("warp", new String[] {
                "ne.warps"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Warp    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("warp", "<name>", "Teleport to a warp"),
            getMessages().getMessage(CHAT_FOOTER),
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.warps")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                case 1:
                    PublicLocationModel publicLocationModel = getPublicLocationRegistry().getPublicLocations();

                    String name = args[0].toLowerCase();

                    if (!publicLocationModel.warpExists(name)) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but there isn't a warp that exists with that name."));
                        return true;
                    }

                    Location location = publicLocationModel.getWarp(name);

                    if (!player.hasPermission("ne.warp." + name)) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but you do not have permission to use this warp."));
                        return true;
                    }

                    player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have teleported to warp " + WHITE + name + GRAY + "."));
                    return true;
                default:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }
        return false;
    }
}
