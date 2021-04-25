package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.user.UserModel;
import org.apache.logging.log4j.core.appender.rolling.action.IfAll;
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
import static net.nighthawkempires.essentials.EssentialsPlugin.getPlayerData;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.ITALIC;

public class FlyCommand implements CommandExecutor {

    public FlyCommand() {
        getCommandManager().registerCommands("fly", new String[] {
                "ne.fly.self", "ne.fly.other"
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel userModel = getUserRegistry().getUser(player.getUniqueId());

            if (!player.hasPermission("ne.fly.self") && !player.hasPermission("ne.fly.other")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    if (!player.hasPermission("ne.fly.self")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    if (getPlayerData().getFlyList().contains(player.getUniqueId())) {
                        getPlayerData().getFlyList().remove(player.getUniqueId());
                        player.setAllowFlight(false);
                        player.setFlying(false);
                        player.sendMessage(getMessages().getChatMessage(GRAY + "Flight has been " + RED
                                + "" + UNDERLINE + "" + ITALIC + "DISABLED" + GRAY + "."));
                        return true;
                    } else {
                        getPlayerData().getFlyList().add(player.getUniqueId());
                        player.setAllowFlight(true);
                        player.sendMessage(getMessages().getChatMessage(GRAY + "Flight has been " + GREEN
                                + "" + UNDERLINE + "" + ITALIC + "ENABLED" + GRAY + "."));
                        return true;
                    }
                case 1:
                    if (!player.hasPermission("ne.godmode.other")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    String name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();
                        if (getPlayerData().getFlyList().contains(target.getUniqueId())) {
                            getPlayerData().getFlyList().remove(target.getUniqueId());
                            target.setAllowFlight(false);
                            target.setFlying(false);
                            player.sendMessage(getMessages().getChatMessage(GRAY + "You have disabled flight for " + GREEN + target.getName() + GRAY + "."));
                            target.sendMessage(getMessages().getChatMessage(GRAY + "Flight has been " + RED
                                    + "" + UNDERLINE + "" + ITALIC + "DISABLED" + GRAY + "."));
                            return true;
                        } else {
                            getPlayerData().getFlyList().add(target.getUniqueId());
                            target.setAllowFlight(true);
                            player.sendMessage(getMessages().getChatMessage(GRAY + "You have enabled flight for " + GREEN + target.getName() + GRAY + "."));
                            target.sendMessage(getMessages().getChatMessage(GRAY + "Flight has been " + GREEN
                                    + "" + UNDERLINE + "" + ITALIC + "ENABLED" + GRAY + "."));
                            return true;
                        }
                    }  else {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
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
                            DARK_GRAY + "Command" + GRAY + ": Fly    " + DARK_GRAY + "    [Optional], <Required>",
                            getMessages().getMessage(CHAT_FOOTER),
                            getMessages().getCommand("fly", "<player>", "Toggle fly for a player"),
                            getMessages().getMessage(CHAT_FOOTER)
                    };

                    sender.sendMessage(help);
                    return true;
                case 1:
                    String name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();
                        if (getPlayerData().getFlyList().contains(target.getUniqueId())) {
                            getPlayerData().getFlyList().remove(target.getUniqueId());
                            target.setAllowFlight(false);
                            target.setFlying(false);
                            sender.sendMessage(getMessages().getChatMessage(GRAY + "You have disabled flight for " + GREEN + target.getName() + GRAY + "."));
                            target.sendMessage(getMessages().getChatMessage(GRAY + "Flight has been " + RED
                                    + "" + UNDERLINE + "" + ITALIC + "DISABLED" + GRAY + "."));
                            return true;
                        } else {
                            getPlayerData().getFlyList().add(target.getUniqueId());
                            target.setAllowFlight(true);
                            sender.sendMessage(getMessages().getChatMessage(GRAY + "You have enabled flight for " + GREEN + target.getName() + GRAY + "."));
                            target.sendMessage(getMessages().getChatMessage(GRAY + "Flight has been " + GREEN
                                    + "" + UNDERLINE + "" + ITALIC + "ENABLED" + GRAY + "."));
                            return true;
                        }
                    }  else {
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
