package net.nighthawkempires.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.essentials.EssentialsPlugin.*;
import static org.bukkit.ChatColor.*;

public class GodmodeCommand implements CommandExecutor {

    public GodmodeCommand() {
        getCommandManager().registerCommands("godmode", new String[] {
                "ne.godmode.self", "ne.godmode.other"
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.godmode.self") && !player.hasPermission("ne.godmode.other")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    if (!player.hasPermission("ne.godmode.self")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    if (getPlayerData().getGodmodeList().contains(player.getUniqueId())) {
                        getPlayerData().getGodmodeList().remove(player.getUniqueId());
                        player.sendMessage(getMessages().getChatMessage(GRAY + "Godmode has been " + RED
                                + "" + UNDERLINE + "" + ITALIC + "DISABLED" + GRAY + "."));
                        return true;
                    } else {
                        getPlayerData().getGodmodeList().add(player.getUniqueId());
                        player.sendMessage(getMessages().getChatMessage(GRAY + "Godmode has been " + GREEN
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
                        if (getPlayerData().getGodmodeList().contains(target.getUniqueId())) {
                            getPlayerData().getGodmodeList().remove(target.getUniqueId());
                            player.sendMessage(getMessages().getChatMessage(GRAY + "You have disabled godmode for " + GREEN + target.getName() + GRAY + "."));
                            target.sendMessage(getMessages().getChatMessage(GRAY + "Godmode has been " + RED
                                    + "" + UNDERLINE + "" + ITALIC + "DISABLED" + GRAY + "."));
                            return true;
                        } else {
                            getPlayerData().getGodmodeList().add(target.getUniqueId());
                            player.sendMessage(getMessages().getChatMessage(GRAY + "You have enabled godmode for " + GREEN + target.getName() + GRAY + "."));
                            target.sendMessage(getMessages().getChatMessage(GRAY + "Godmode has been " + GREEN
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
                            DARK_GRAY + "Command" + GRAY + ": Godmode    " + DARK_GRAY + "    [Optional], <Required>",
                            getMessages().getMessage(CHAT_FOOTER),
                            getMessages().getCommand("godmode", "<player>", "Toggle fly for a player"),
                            getMessages().getMessage(CHAT_FOOTER)
                    };

                    sender.sendMessage(help);
                    return true;
                case 1:
                    String name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();
                        if (getPlayerData().getGodmodeList().contains(target.getUniqueId())) {
                            getPlayerData().getGodmodeList().remove(target.getUniqueId());
                            sender.sendMessage(getMessages().getChatMessage(GRAY + "You have disabled godmode for " + GREEN + target.getName() + GRAY + "."));
                            target.sendMessage(getMessages().getChatMessage(GRAY + "Godmode has been " + RED
                                    + "" + UNDERLINE + "" + ITALIC + "DISABLED" + GRAY + "."));
                            return true;
                        } else {
                            getPlayerData().getGodmodeList().add(target.getUniqueId());
                            sender.sendMessage(getMessages().getChatMessage(GRAY + "You have enabled godmode for " + GREEN + target.getName() + GRAY + "."));
                            target.sendMessage(getMessages().getChatMessage(GRAY + "Godmode has been " + GREEN
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
