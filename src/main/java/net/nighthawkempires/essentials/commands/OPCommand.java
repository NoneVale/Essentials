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
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.core.lang.Messages.INVALID_SYNTAX;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class OPCommand implements CommandExecutor {

    public OPCommand() {
        getCommandManager().registerCommands("op", new String[] {
                "ne.operator"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": OP    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("op", "<player>", "OP a player"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel userModel = getUserRegistry().getUser(player.getUniqueId());

            if (!player.isOp()) {
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
                    if (offlinePlayer.isOp()) {
                        player.sendMessage(getMessages().getChatMessage(GREEN + offlinePlayer.getName() + GRAY + " is already a server operator."));
                        return true;
                    }

                    offlinePlayer.setOp(true);
                    player.sendMessage(getMessages().getChatMessage(GREEN + offlinePlayer.getName() + GRAY + " is now a server operator"));
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();
                        target.sendMessage(getMessages().getChatMessage(GRAY + "You are now a server operator."));
                        return true;
                    }
                    return true;
                default:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
            }
        } else if (sender instanceof ConsoleCommandSender) {
            switch (args.length) {
                case 0:
                    sender.sendMessage(help);
                    return true;
                case 1:
                    String name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (offlinePlayer.isOp()) {
                        sender.sendMessage(getMessages().getChatMessage(GREEN + offlinePlayer.getName() + GRAY + " is already a server operator."));
                        return true;
                    }

                    offlinePlayer.setOp(true);
                    sender.sendMessage(getMessages().getChatMessage(GREEN + offlinePlayer.getName() + GRAY + " is now a server operator"));
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();
                        target.sendMessage(getMessages().getChatMessage(GRAY + "You are now a server operator."));
                        return true;
                    }
                    return true;
                default:
                    sender.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
            }
        }
        return false;
    }
}
