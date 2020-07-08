package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.essentials.EssentialsPlugin.getPlayerData;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class ScoreboardCommand implements CommandExecutor {

    public ScoreboardCommand() {
        getCommandManager().registerCommands("scoreboard", new String[] {
                "ne.scoreboard"
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel userModel = getUserRegistry().getUser(player.getUniqueId());

            if (!player.hasPermission("ne.scoreboard") ) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    if (getScoreboardManager().getPlayerScoreboardMap().containsKey(player.getUniqueId())) {
                        getScoreboardManager().stopScoreboards(player);
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have stopped your scoreboards."));
                        return true;
                    } else {
                        getScoreboardManager().startScoreboards(player);
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have started your scoreboards."));
                        return true;
                    }
                default:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }

        return false;
    }
}
