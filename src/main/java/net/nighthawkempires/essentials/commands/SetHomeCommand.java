package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.location.player.PlayerLocationModel;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.essentials.EssentialsPlugin.*;
import static org.bukkit.ChatColor.*;

public class SetHomeCommand implements CommandExecutor {

    public SetHomeCommand() {
        getCommandManager().registerCommands("sethome", new String[] {
                "ne.home"
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.home")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            int homes = 1;
            for (PermissionAttachmentInfo info : player.getEffectivePermissions()) {
                if (info.getPermission().toLowerCase().startsWith("ne.homes.")) {
                    String homeAmountString = info.getPermission().replaceFirst("ne.homes.", "");

                    if (NumberUtils.isDigits(homeAmountString)) {
                        homes = Integer.parseInt(homeAmountString);
                    }
                }
            }

            PlayerLocationModel playerLocationModel = getPlayerLocationRegistry().getPlayerLocations(player.getUniqueId());

            switch (args.length) {
                case 0:
                    if (!playerLocationModel.getHomes().contains("home") && playerLocationModel.getHomes().size() >= homes) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You currently have too many homes set to be able to set home "
                                + WHITE + "home" + GRAY + "."));
                        return true;
                    }

                    playerLocationModel.setHome("home", player.getLocation());
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have set a home under the name " + WHITE + "home" + GRAY + "."));
                    return true;
                case 1:
                    String name = args[0].toLowerCase();

                    if (playerLocationModel.getHomes().contains(name.toLowerCase())) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You already have a home set under the name " + WHITE + name
                                + GRAY + "."));
                        return true;
                    }

                    if (playerLocationModel.getHomes().size() >= homes) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have reached your home limit, consider removing old homes in order to set new ones."));
                        return true;
                    }

                    playerLocationModel.setHome(name, player.getLocation());
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have set a home under the name " + WHITE + name + GRAY + "."));
                    return true;
                default:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }
        return false;
    }
}
