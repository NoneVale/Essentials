package net.nighthawkempires.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.getCommandManager;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.essentials.EssentialsPlugin.*;
import static org.bukkit.ChatColor.*;

public class MessageCommand implements CommandExecutor {

    public MessageCommand() {
        getCommandManager().registerCommands("message", new String[] {
                "ne.message"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Message    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("msg", "<player> <message>", "Message a player"),
            getMessages().getMessage(CHAT_FOOTER),
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.message")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                case 1:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
                default:
                    StringBuilder messageBuilder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        messageBuilder.append(args[i]).append(" ");
                    }

                    String name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (!offlinePlayer.isOnline()) {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }

                    Player target = offlinePlayer.getPlayer();

                    player.sendMessage(getMessages().formatMSGOut(target, messageBuilder.toString().trim()));
                    target.sendMessage(getMessages().formatMSGIn(player, messageBuilder.toString().trim()));
                    getPlayerData().getReplyMap().put(player.getUniqueId(), target.getUniqueId());
                    getPlayerData().getReplyMap().put(target.getUniqueId(), player.getUniqueId());
                    return true;
            }
        }

        return false;
    }
}
