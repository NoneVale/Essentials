package net.nighthawkempires.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.essentials.EssentialsPlugin.*;
import static org.bukkit.ChatColor.*;

public class ReplyCommand implements CommandExecutor {

    public ReplyCommand() {
        getCommandManager().registerCommands("reply", new String[] {
                "ne.message"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Reply    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("reply", "<message>", "Reply to a message"),
            getMessages().getMessage(CHAT_FOOTER)
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
                default:
                    if (!getPlayerData().getReplyMap().containsKey(player.getUniqueId())) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have nobody to reply to."));
                        return true;
                    }

                    UUID uuid = getPlayerData().getReplyMap().get(player.getUniqueId());

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                    if (!offlinePlayer.isOnline()) {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        getPlayerData().getReplyMap().remove(player.getUniqueId());
                        return true;
                    }

                    Player target = offlinePlayer.getPlayer();

                    StringBuilder messageBuilder = new StringBuilder();
                    for (String arg : args) {
                        messageBuilder.append(arg).append(" ");
                    }

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
