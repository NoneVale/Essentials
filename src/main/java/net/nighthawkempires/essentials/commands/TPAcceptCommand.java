package net.nighthawkempires.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.essentials.EssentialsPlugin.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class TPAcceptCommand implements CommandExecutor {

    public TPAcceptCommand() {
        getCommandManager().registerCommands("tpaccept", new String[] {
                "ne.tpaccept"
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.tpaccept")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    HashMap<UUID, List<UUID>> requestsMap = getPlayerData().getTeleportRequestsMap();
                    if (requestsMap.containsKey(player.getUniqueId())) {
                        List<UUID> requests = requestsMap.get(player.getUniqueId());
                        if (requests.isEmpty()) {
                            player.sendMessage(getMessages().getChatMessage(GRAY + "You do not have any active requests."));
                            requestsMap.remove(player.getUniqueId());
                            return true;
                        }

                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(requests.get(0));
                        if (!offlinePlayer.isOnline()) {
                            player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                            requests.remove(0);
                            if (requests.isEmpty())
                                requestsMap.remove(player.getUniqueId());
                            return true;
                        }

                        Player target = offlinePlayer.getPlayer();
                        target.teleport(player);
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have accepted " + GREEN + target.getName()
                                + "'s " + GRAY + "teleportation request."));
                        target.sendMessage(getMessages().getChatMessage(GREEN + player.getName() + GRAY
                                + " has accepted your teleportation request."));
                        requests.remove(0);
                        if (requests.isEmpty())
                            requestsMap.remove(player.getUniqueId());
                        return true;
                    } else {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You do not have any active requests."));
                        return true;
                    }
                case 1:
                    requestsMap = getPlayerData().getTeleportRequestsMap();
                    if (requestsMap.containsKey(player.getUniqueId())) {
                        List<UUID> requests = requestsMap.get(player.getUniqueId());
                        if (requests.isEmpty()) {
                            player.sendMessage(getMessages().getChatMessage(GRAY + "You do not have any active requests."));
                            requestsMap.remove(player.getUniqueId());
                            return true;
                        }

                        String name = args[0];
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);

                        if (!requests.contains(offlinePlayer.getUniqueId())) {
                            player.sendMessage(getMessages().getChatMessage(GRAY + "You do not have a teleporation request from that player."));
                            return true;
                        }

                        if (!offlinePlayer.isOnline()) {
                            player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                            requests.remove(offlinePlayer.getUniqueId());
                            if (requests.isEmpty())
                                requestsMap.remove(player.getUniqueId());
                            return true;
                        }

                        Player target = offlinePlayer.getPlayer();
                        target.teleport(player);
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have accepted " + GREEN + target.getName()
                                + "'s " + GRAY + "teleportation request."));
                        target.sendMessage(getMessages().getChatMessage(GREEN + player.getName() + GRAY
                                + " has accepted your teleportation request."));
                        requests.remove(target.getUniqueId());
                        if (requests.isEmpty())
                            requestsMap.remove(player.getUniqueId());
                    } else {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You do not have any active requests."));
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
