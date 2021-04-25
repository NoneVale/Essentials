package net.nighthawkempires.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static net.nighthawkempires.core.CorePlugin.getCommandManager;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.core.lang.Messages.INVALID_SYNTAX;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class SmiteCommand implements CommandExecutor {

    public SmiteCommand() {
        getCommandManager().registerCommands("smite", new String[]{
                "ne.smite"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Smite    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("smite", "<player>", "Smite a player"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.smite")) {
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
                    if (!offlinePlayer.isOnline()) {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }

                    Player target = offlinePlayer.getPlayer();

                    target.getWorld().strikeLightning(target.getLocation());
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have smitten " + GREEN + target.getName() + GRAY + "."));
                    target.sendMessage(getMessages().getChatMessage(GRAY + "You have been smote by the gods."));
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
                    if (!offlinePlayer.isOnline()) {
                        sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }

                    Player target = offlinePlayer.getPlayer();

                    target.getWorld().strikeLightning(target.getLocation());
                    sender.sendMessage(getMessages().getChatMessage(GRAY + "You have smitten " + GREEN + target.getName() + GRAY + "."));
                    target.sendMessage(getMessages().getChatMessage(GRAY + "You have been smote by the gods."));
                    return true;
                default:
                    sender.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }
        return false;
    }
}
