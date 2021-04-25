package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.core.lang.Messages.INVALID_SYNTAX;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class UnmuteCommand implements CommandExecutor {

    public UnmuteCommand() {
        getCommandManager().registerCommands("unmute", new String[] {
                "ne.unmute"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Unmute    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("unmute", "<player>", "Unmute a player"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.unmute")) {
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

                    if (!targetUserModel.isMuted()) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "That player is not currently muted!"));
                        return true;
                    }

                    targetUserModel.unmute();
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have unmuted " + GREEN + offlinePlayer.getName() + GRAY + "."));
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

                    if (!targetUserModel.isMuted()) {
                        sender.sendMessage(getMessages().getChatMessage(GRAY + "That player is not currently muted!"));
                        return true;
                    }

                    targetUserModel.unmute();
                    sender.sendMessage(getMessages().getChatMessage(GRAY + "You have unmuted " + GREEN + offlinePlayer.getName() + GRAY + "."));
                    return true;
                default:
                    sender.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }
        return false;
    }
}
