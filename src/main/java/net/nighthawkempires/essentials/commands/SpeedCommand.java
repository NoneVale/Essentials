package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.location.PublicLocationModel;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class SpeedCommand implements CommandExecutor {

    public SpeedCommand() {
        getCommandManager().registerCommands("speed", new String[]{
                "ne.speed.fly", "ne.speed.walk"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Speed    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("speed", "walk <speed>", "Set your walking speed"),
            getMessages().getCommand("speed", "walk reset", "Reset your walking speed"),
            getMessages().getCommand("speed", "fly <speed>", "Set your flying speed"),
            getMessages().getCommand("speed", "fly reset", "Reset your flying speed"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.speed")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            PublicLocationModel publicLocationModel = getPublicLocationRegistry().getPublicLocations();
            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                case 1:
                    switch (args[0].toLowerCase()) {
                        case "walk":
                        case "w":
                            player.sendMessage(getMessages().getChatMessage(GRAY + "Your current walking speed is set to "
                                    + GOLD + player.getWalkSpeed() * 10 + GRAY + "."));
                            return true;
                        case "fly":
                        case "f":
                            player.sendMessage(getMessages().getChatMessage(GRAY + "Your current flying speed is set to "
                                    + GOLD + player.getFlySpeed() * 10 + GRAY + "."));
                            return true;
                        default:
                            player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                            return true;
                    }
                case 2:
                    switch (args[0].toLowerCase()) {
                        case "walk":
                        case "w":
                            switch (args[1].toLowerCase()) {
                                case "reset":
                                    player.setWalkSpeed(0.2f);
                                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have reset your walking speed."));
                                    return true;
                                default:
                                    if (!NumberUtils.isDigits(args[1])) {
                                        player.sendMessage(getMessages().getChatMessage(GRAY + "The speed must be a valid number."));
                                        return true;
                                    }

                                    float speed = Float.parseFloat(args[1]);
                                    float decimalSpeed = speed / 10;

                                    if (decimalSpeed > 1) {
                                        player.sendMessage(getMessages().getChatMessage(GRAY + "The speed value is too high.  The max speed is 10"));
                                        return true;
                                    }

                                    player.setWalkSpeed(decimalSpeed);
                                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have set your walking speed to " + GOLD + speed + GRAY + "."));
                                    return true;
                            }
                        case "fly":
                        case "f":
                            switch (args[1].toLowerCase()) {
                                case "reset":
                                    player.setFlySpeed(0.2f);
                                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have reset your flying speed."));
                                    return true;
                                default:
                                    if (!NumberUtils.isDigits(args[1])) {
                                        player.sendMessage(getMessages().getChatMessage(GRAY + "The speed must be a valid number."));
                                        return true;
                                    }

                                    float speed = Float.parseFloat(args[1]);
                                    float decimalSpeed = speed / 10;

                                    if (decimalSpeed > 1) {
                                        player.sendMessage(getMessages().getChatMessage(GRAY + "The speed value is too high.  The max speed is 10"));
                                        return true;
                                    }

                                    player.setFlySpeed(decimalSpeed);
                                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have set your flying speed to " + GOLD + speed + GRAY + "."));
                                    return true;
                            }
                        default:
                            player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                            return true;
                    }
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
