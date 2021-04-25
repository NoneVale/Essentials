package net.nighthawkempires.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;

public class GamemodeCommand implements CommandExecutor {

    public GamemodeCommand() {
        getCommandManager().registerCommands("gamemode", new String[] {
                "ne.gamemode.self", "ne.gamemode.other"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Gamemode    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("gamemode", "<mode> [player]", "Set a player's gamemode."),
            getMessages().getMessage(CHAT_FOOTER),
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.gamemode.self") && !player.hasPermission("ne.gamemode.other")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    player.sendMessage(DARK_GRAY + "Current Gamemode: " + GRAY
                            + player.getGameMode().name().substring(0, 1) + player.getGameMode().name().substring(1).toLowerCase());
                    player.sendMessage(getMessages().getMessage(CHAT_FOOTER));
                    return true;
                case 1:
                    if (!player.hasPermission("ne.gamemode.self")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    switch (args[0].toLowerCase()) {
                        case "s":
                        case "0":
                        case "survival":
                            if (player.getGameMode() == GameMode.SURVIVAL) {
                                player.sendMessage(getMessages().getChatMessage(GRAY + "Your gamemode is already set to "
                                        + gameModeName(GameMode.SURVIVAL) + "."));
                                return true;
                            }

                            player.setGameMode(GameMode.SURVIVAL);
                            player.sendMessage(getMessages().getChatMessage(GRAY + "Your gamemode has been set to "
                                    + gameModeName(GameMode.SURVIVAL) + "."));
                            return true;
                        case "c":
                        case "1":
                        case "creative":
                            if (player.getGameMode() == GameMode.CREATIVE) {
                                player.sendMessage(getMessages().getChatMessage(GRAY + "Your gamemode is already set to "
                                        + gameModeName(GameMode.CREATIVE) + "."));
                                return true;
                            }

                            player.setGameMode(GameMode.CREATIVE);
                            player.sendMessage(getMessages().getChatMessage(GRAY + "Your gamemode has been set to "
                                    + gameModeName(GameMode.CREATIVE) + "."));
                            return true;
                        case "a":
                        case "2":
                        case "adventure":
                            if (player.getGameMode() == GameMode.ADVENTURE) {
                                player.sendMessage(getMessages().getChatMessage(GRAY + "Your gamemode is already set to "
                                        + gameModeName(GameMode.ADVENTURE) + "."));
                                return true;
                            }

                            player.setGameMode(GameMode.ADVENTURE);
                            player.sendMessage(getMessages().getChatMessage(GRAY + "Your gamemode has been set to "
                                    + gameModeName(GameMode.ADVENTURE) + "."));
                            return true;
                        case "sp":
                        case "3":
                        case "spectator":
                            if (player.getGameMode() == GameMode.SPECTATOR) {
                                player.sendMessage(getMessages().getChatMessage(GRAY + "Your gamemode is already set to "
                                        + gameModeName(GameMode.SPECTATOR) + "."));
                                return true;
                            }

                            player.setGameMode(GameMode.SPECTATOR);
                            player.sendMessage(getMessages().getChatMessage(GRAY + "Your gamemode has been set to "
                                    + gameModeName(GameMode.SPECTATOR) + "."));
                            return true;
                        default:
                            player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but that is not a valid gamemode."));
                            return true;
                    }
                case 2:
                    if (!player.hasPermission("ne.gamemode.other")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    String name = args[1];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (!offlinePlayer.isOnline()) {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    } else {
                        Player target = offlinePlayer.getPlayer();
                        switch (args[0].toLowerCase()) {
                            case "s":
                            case "0":
                            case "survival":
                                if (target.getGameMode() == GameMode.SURVIVAL) {
                                    player.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "gamemode is already set to "
                                            + gameModeName(GameMode.SURVIVAL) + "."));
                                    return true;
                                }

                                target.setGameMode(GameMode.SURVIVAL);
                                player.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "gamemode has been set to "
                                        + gameModeName(GameMode.SURVIVAL) + "."));
                                target.sendMessage(getMessages().getChatMessage(GRAY + "Your gamemode has been set to "
                                        + gameModeName(GameMode.SPECTATOR) + "."));
                                return true;
                            case "c":
                            case "1":
                            case "creative":
                                if (target.getGameMode() == GameMode.CREATIVE) {
                                    player.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "gamemode is already set to "
                                            + gameModeName(GameMode.CREATIVE) + "."));
                                    return true;
                                }

                                target.setGameMode(GameMode.CREATIVE);
                                player.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "gamemode has been set to "
                                        + gameModeName(GameMode.CREATIVE) + "."));
                                target.sendMessage(getMessages().getChatMessage(GRAY + "Your gamemode has been set to "
                                        + gameModeName(GameMode.SPECTATOR) + "."));
                                return true;
                            case "a":
                            case "2":
                            case "adventure":
                                if (target.getGameMode() == GameMode.ADVENTURE) {
                                    player.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "gamemode is already set to "
                                            + gameModeName(GameMode.ADVENTURE) + "."));
                                    return true;
                                }

                                target.setGameMode(GameMode.ADVENTURE);
                                player.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "gamemode has been set to "
                                        + gameModeName(GameMode.ADVENTURE) + "."));
                                target.sendMessage(getMessages().getChatMessage(GRAY + "Your gamemode has been set to "
                                        + gameModeName(GameMode.SPECTATOR) + "."));
                                return true;
                            case "sp":
                            case "3":
                            case "spectator":
                                if (target.getGameMode() == GameMode.SPECTATOR) {
                                    player.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "gamemode is already set to "
                                            + gameModeName(GameMode.SPECTATOR) + "."));
                                    return true;
                                }

                                target.setGameMode(GameMode.SPECTATOR);
                                player.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "gamemode has been set to "
                                        + gameModeName(GameMode.SPECTATOR) + "."));
                                target.sendMessage(getMessages().getChatMessage(GRAY + "Your gamemode has been set to "
                                        + gameModeName(GameMode.SPECTATOR) + "."));
                                return true;
                            default:
                                player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but that is not a valid gamemode."));
                                return true;
                        }
                    }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            switch (args.length) {
                case 0:
                case 1:
                    sender.sendMessage(help);
                    return true;
                case 2:
                    String name = args[1];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (!offlinePlayer.isOnline()) {
                        sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    } else {
                        Player target = offlinePlayer.getPlayer();
                        switch (args[0].toLowerCase()) {
                            case "s":
                            case "0":
                            case "survival":
                                if (target.getGameMode() == GameMode.SURVIVAL) {
                                    sender.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "gamemode is already set to "
                                            + gameModeName(GameMode.SURVIVAL) + "."));
                                    return true;
                                }

                                target.setGameMode(GameMode.SURVIVAL);
                                sender.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "gamemode has been set to "
                                        + gameModeName(GameMode.SURVIVAL) + "."));
                                target.sendMessage(getMessages().getChatMessage(GRAY + "Your gamemode has been set to "
                                        + gameModeName(GameMode.SPECTATOR) + "."));
                                return true;
                            case "c":
                            case "1":
                            case "creative":
                                if (target.getGameMode() == GameMode.CREATIVE) {
                                    sender.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "gamemode is already set to "
                                            + gameModeName(GameMode.CREATIVE) + "."));
                                    return true;
                                }

                                target.setGameMode(GameMode.CREATIVE);
                                sender.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "gamemode has been set to "
                                        + gameModeName(GameMode.CREATIVE) + "."));
                                target.sendMessage(getMessages().getChatMessage(GRAY + "Your gamemode has been set to "
                                        + gameModeName(GameMode.SPECTATOR) + "."));
                                return true;
                            case "a":
                            case "2":
                            case "adventure":
                                if (target.getGameMode() == GameMode.ADVENTURE) {
                                    sender.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "gamemode is already set to "
                                            + gameModeName(GameMode.ADVENTURE) + "."));
                                    return true;
                                }

                                target.setGameMode(GameMode.ADVENTURE);
                                sender.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "gamemode has been set to "
                                        + gameModeName(GameMode.ADVENTURE) + "."));
                                target.sendMessage(getMessages().getChatMessage(GRAY + "Your gamemode has been set to "
                                        + gameModeName(GameMode.SPECTATOR) + "."));
                                return true;
                            case "sp":
                            case "3":
                            case "spectator":
                                if (target.getGameMode() == GameMode.SPECTATOR) {
                                    sender.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "gamemode is already set to "
                                            + gameModeName(GameMode.SPECTATOR) + "."));
                                    return true;
                                }

                                target.setGameMode(GameMode.SPECTATOR);
                                sender.sendMessage(getMessages().getChatMessage(GREEN + target.getName() + "'s " + GRAY + "gamemode has been set to "
                                        + gameModeName(GameMode.SPECTATOR) + "."));
                                target.sendMessage(getMessages().getChatMessage(GRAY + "Your gamemode has been set to "
                                        + gameModeName(GameMode.SPECTATOR) + "."));
                                return true;
                            default:
                                sender.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but that is not a valid gamemode."));
                                return true;
                        }
                    }
            }
        }

        return false;
    }

    private String gameModeName(GameMode gameMode) {
        return gameMode.name().substring(0, 1) + gameMode.name().substring(1).toLowerCase();
    }
}