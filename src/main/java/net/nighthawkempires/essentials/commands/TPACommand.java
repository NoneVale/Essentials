package net.nighthawkempires.essentials.commands;

import com.google.common.collect.Lists;
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

import static net.nighthawkempires.core.CorePlugin.getCommandManager;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.essentials.EssentialsPlugin.getPlayerData;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.DARK_GRAY;

public class TPACommand implements CommandExecutor {

    public TPACommand() {
        getCommandManager().registerCommands("tpa", new String[] {
                "ne.tpa"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": TPA    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("tpa", "<player>", "Send a teleportation request to a player"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.tpa")) {
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

                    HashMap<UUID, List<UUID>> requestsMap = getPlayerData().getTeleportRequestsMap();
                    if (!requestsMap.containsKey(target.getUniqueId())) {
                        requestsMap.put(target.getUniqueId(), Lists.newArrayList());
                    }

                    List<UUID> requests = requestsMap.get(target.getUniqueId());

                    if (requests.contains(player.getUniqueId())) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have already sent this player a teleporation request."));
                        return true;
                    }

                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have sent a teleportation request to " + GREEN
                            + target.getName() + GRAY + "."));
                    target.sendMessage(getMessages().getChatMessage(GREEN + player.getName() + GRAY + " has sent you a teleportation request.  You have "
                            + GOLD + "60 seconds" + GRAY + " to accept before it expires."));
                    requests.add(0, player.getUniqueId());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(EssentialsPlugin.getPlugin(), () -> {
                        requests.remove(player.getUniqueId());
                        if (requests.isEmpty()) {
                            requestsMap.remove(target.getUniqueId());
                        }
                    }, 1200);
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