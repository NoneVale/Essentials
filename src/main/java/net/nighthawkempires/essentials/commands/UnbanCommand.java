package net.nighthawkempires.essentials.commands;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.user.UserModel;
import net.nighthawkempires.essentials.EssentialsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.essentials.EssentialsPlugin.getPlayerData;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class UnbanCommand implements CommandExecutor {

    public UnbanCommand() {
        getCommandManager().registerCommands("unban", new String[] {
                "ne.unban"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Unban    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("unban", "<player>", "Unban a player"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.unban")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                case 1:
                    String name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    UserModel targetUserModel = getUserRegistry().getUser(offlinePlayer.getUniqueId());

                    if (!targetUserModel.isBanned()) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "That player is not currently banned!"));
                        return true;
                    }

                    targetUserModel.unban();
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have unbanned " + GREEN + offlinePlayer.getName() + GRAY + "."));
                    return true;
                default:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            switch (args.length) {
                case 0:
                    sender.sendMessage(help);
                    return true;
                case 1:
                    String name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    UserModel targetUserModel = getUserRegistry().getUser(offlinePlayer.getUniqueId());

                    if (!targetUserModel.isBanned()) {
                        sender.sendMessage(getMessages().getChatMessage(GRAY + "That player is not currently banned!"));
                        return true;
                    }

                    targetUserModel.unban();
                    sender.sendMessage(getMessages().getChatMessage(GRAY + "You have unbanned " + GREEN + offlinePlayer.getName() + GRAY + "."));
                    return true;
                default:
                    sender.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }
        return false;
    }
}
