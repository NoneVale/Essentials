package net.nighthawkempires.essentials.commands;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.lang.Messages;
import net.nighthawkempires.core.location.PublicLocationModel;
import net.nighthawkempires.core.location.player.PlayerLocationModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.RED;

public class WarpsCommand implements CommandExecutor {

    public WarpsCommand() {
        getCommandManager().registerCommands("warps", new String[] {
                "ne.warp"
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.warp")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            PublicLocationModel publicLocationModel = getPublicLocationRegistry().getPublicLocations();
            switch (args.length) {
                case 0:
                    StringBuilder warpBuilder = new StringBuilder();
                    warpBuilder.append(ChatColor.translateAlternateColorCodes('&', "&8 - "));
                    List<String> available = Lists.newArrayList();
                    for (int i = 0; i < publicLocationModel.getWarps().size(); i++) {
                        if (player.hasPermission("ne.warp." + publicLocationModel.getWarps().get(i).toLowerCase())) {
                            warpBuilder.append(GREEN).append(publicLocationModel.getWarps().get(i).toLowerCase());
                            available.add(publicLocationModel.getWarps().get(i).toLowerCase());
                            if (i < publicLocationModel.getWarps().size() - 1) {
                                warpBuilder.append(DARK_GRAY).append(", ");
                            }
                        }
                    }

                    if (available.isEmpty()) {
                        warpBuilder.append(RED).append("None");
                    }

                    String[] list = new String[]{
                            getMessages().getMessage(Messages.CHAT_HEADER),
                            ChatColor.translateAlternateColorCodes('&', "&8List&7: Warps"),
                            getMessages().getMessage(Messages.CHAT_FOOTER),
                            ChatColor.translateAlternateColorCodes('&', "&8Warps&7: "),
                            warpBuilder.toString(),
                            getMessages().getMessage(Messages.CHAT_FOOTER)
                    };
                    player.sendMessage(list);
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
