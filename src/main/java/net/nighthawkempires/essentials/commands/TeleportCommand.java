package net.nighthawkempires.essentials.commands;

import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import static net.nighthawkempires.core.CorePlugin.getCommandManager;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class TeleportCommand implements CommandExecutor {

    public TeleportCommand() {
        getCommandManager().registerCommands("teleport", new String[] {
                "ne.teleport"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Teleport    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("teleport", "<player>", "Teleport to a player"),
            getMessages().getCommand("teleport", "<player> <targetPlayer>", "Teleport a player to a player"),
            getMessages().getCommand("teleport", "<x> <y> <z>", "Teleport to a set of coordinates"),
            getMessages().getCommand("teleport", "<player> <x> <y> <z>", "Teleport to a set of coordinates"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.teleport")) {
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
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();

                        player.teleport(target);
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have teleported to " + GREEN + target.getName() + GRAY + "."));
                        return true;
                    } else {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }
                case 2:
                    name = args[0];
                    offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();

                        name = args[1];
                        offlinePlayer = Bukkit.getOfflinePlayer(name);
                        if (offlinePlayer.isOnline()) {
                            Player targetTo = offlinePlayer.getPlayer();

                            target.teleport(targetTo, PlayerTeleportEvent.TeleportCause.COMMAND);
                            player.sendMessage(getMessages().getChatMessage(GRAY + "You have teleported " + GREEN + target.getName()
                                    + GRAY + " to " + GREEN + targetTo.getName() + GRAY + "."));
                            target.sendMessage(getMessages().getChatMessage(GRAY + "You have been teleported to " + GREEN + targetTo.getName() + GRAY + "."));
                            return true;
                        } else {
                            player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                            return true;
                        }
                    } else {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }
                case 3:
                    int x = 0, y = 0, z = 0;
                    String num = args[0];
                    if (num.equals("~")) {
                        x = player.getLocation().getBlockX();
                    } else if (NumberUtils.isDigits(num)) {
                        x = Integer.parseInt(num);
                    } else {
                        sender.sendMessage(getMessages().getChatMessage(GRAY + "Incorrect input for x coordinate."));
                        return true;
                    }

                    num = args[1];
                    if (num.equals("~")) {
                        y = player.getLocation().getBlockY();
                    } else if (NumberUtils.isDigits(num)) {
                        y = Integer.parseInt(num);
                    } else {
                        sender.sendMessage(getMessages().getChatMessage(GRAY + "Incorrect input for y coordinate."));
                        return true;
                    }

                    num = args[2];
                    if (num.equals("~")) {
                        z = player.getLocation().getBlockZ();
                    } else if (NumberUtils.isDigits(num)) {
                        z = Integer.parseInt(num);
                    } else {
                        sender.sendMessage(getMessages().getChatMessage(GRAY + "Incorrect input for z coordinate."));
                        return true;
                    }

                    Location location = new Location(player.getWorld(), x, y, z);
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have teleported to "
                            + DARK_GRAY + "[" + GOLD + x + DARK_GRAY + ", " + GOLD + y + DARK_GRAY + ", " + GOLD + z + DARK_GRAY + "]"
                            + GRAY + " in world " + AQUA + player.getWorld().getName() + GRAY + "."));
                    player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
                    return true;
                case 4:
                    name = args[0];
                    offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();

                        x = y = z = 0;
                        num = args[1];
                        if (num.equals("~")) {
                            x = target.getLocation().getBlockX();
                        } else if (NumberUtils.isDigits(num)) {
                            x = Integer.parseInt(num);
                        } else {
                            sender.sendMessage(getMessages().getChatMessage(GRAY + "Incorrect input for x coordinate."));
                            return true;
                        }

                        num = args[2];
                        if (num.equals("~")) {
                            y = target.getLocation().getBlockY();
                        } else if (NumberUtils.isDigits(num)) {
                            y = Integer.parseInt(num);
                        } else {
                            sender.sendMessage(getMessages().getChatMessage(GRAY + "Incorrect input for y coordinate."));
                            return true;
                        }

                        num = args[3];
                        if (num.equals("~")) {
                            z = target.getLocation().getBlockZ();
                        } else if (NumberUtils.isDigits(num)) {
                            z = Integer.parseInt(num);
                        } else {
                            sender.sendMessage(getMessages().getChatMessage(GRAY + "Incorrect input for z coordinate."));
                            return true;
                        }

                        location = new Location(target.getWorld(), x, y, z);
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have teleported " + GREEN + target.getName()
                                + GRAY + " to "
                                + DARK_GRAY + "[" + GOLD + x + DARK_GRAY + ", " + GOLD + y + DARK_GRAY + ", " + GOLD + z + DARK_GRAY + "]"
                                + GRAY + " in world " + AQUA + target.getWorld().getName() + GRAY + "."));
                        target.sendMessage(getMessages().getChatMessage(GRAY + "You have been teleported to "
                                + DARK_GRAY + "[" + GOLD + x + DARK_GRAY + ", " + GOLD + y + DARK_GRAY + ", " + GOLD + z + DARK_GRAY + "]"
                                + GRAY + " in world " + AQUA + target.getWorld().getName() + GRAY + "."));
                        target.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
                        return true;
                    } else {
                        sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }
                default:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            switch (args.length) {
                case 0:
                    String[] help = new String[] {
                            getMessages().getMessage(CHAT_HEADER),
                            DARK_GRAY + "Command" + GRAY + ": Teleport    " + DARK_GRAY + "    [Optional], <Required>",
                            getMessages().getMessage(CHAT_FOOTER),
                            getMessages().getCommand("teleport", "<player> <targetPlayer>", "Teleport a player to a player"),
                            getMessages().getCommand("teleport", "<player> <x> <y> <z>", "Teleport to a set of coordinates"),
                            getMessages().getMessage(CHAT_FOOTER)
                    };
                    sender.sendMessage(help);
                    return true;
                case 2:
                    String name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();

                        name = args[1];
                        offlinePlayer = Bukkit.getOfflinePlayer(name);
                        if (offlinePlayer.isOnline()) {
                            Player targetTo = offlinePlayer.getPlayer();

                            target.teleport(targetTo, PlayerTeleportEvent.TeleportCause.COMMAND);
                            sender.sendMessage(getMessages().getChatMessage(GRAY + "You have teleported " + GREEN + target.getName()
                                    + GRAY + " to " + GREEN + targetTo.getName() + GRAY + "."));
                            target.sendMessage(getMessages().getChatMessage(GRAY + "You have been teleported to " + GREEN + targetTo.getName() + GRAY + "."));
                            return true;
                        } else {
                            sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                            return true;
                        }
                    } else {
                        sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }
                case 4:
                    name = args[0];
                    offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();

                        int x = 0, y = 0, z = 0;
                        String num = args[1];
                        if (num.equals("~")) {
                            x = target.getLocation().getBlockX();
                        } else if (NumberUtils.isDigits(num)) {
                            x = Integer.parseInt(num);
                        } else {
                            sender.sendMessage(getMessages().getChatMessage(GRAY + "Incorrect input for x coordinate."));
                            return true;
                        }

                        num = args[2];
                        if (num.equals("~")) {
                            y = target.getLocation().getBlockY();
                        } else if (NumberUtils.isDigits(num)) {
                            y = Integer.parseInt(num);
                        } else {
                            sender.sendMessage(getMessages().getChatMessage(GRAY + "Incorrect input for y coordinate."));
                            return true;
                        }

                        num = args[3];
                        if (num.equals("~")) {
                            z = target.getLocation().getBlockZ();
                        } else if (NumberUtils.isDigits(num)) {
                            z = Integer.parseInt(num);
                        } else {
                            sender.sendMessage(getMessages().getChatMessage(GRAY + "Incorrect input for z coordinate."));
                            return true;
                        }

                        Location location = new Location(target.getWorld(), x, y, z);
                        sender.sendMessage(getMessages().getChatMessage(GRAY + "You have teleported " + GREEN + target.getName()
                                + GRAY + " to "
                                + DARK_GRAY + "[" + GOLD + x + DARK_GRAY + ", " + GOLD + y + DARK_GRAY + ", " + GOLD + z + DARK_GRAY + "]"
                                + GRAY + " in world " + AQUA + target.getWorld().getName() + GRAY + "."));
                        target.sendMessage(getMessages().getChatMessage(GRAY + "You have been teleported to "
                                + DARK_GRAY + "[" + GOLD + x + DARK_GRAY + ", " + GOLD + y + DARK_GRAY + ", " + GOLD + z + DARK_GRAY + "]"
                                + GRAY + " in world " + AQUA + target.getWorld().getName() + GRAY + "."));
                        target.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
                        return true;
                    } else {
                        sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }
                default:
                    sender.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }
        return false;
    }
}
