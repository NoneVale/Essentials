package net.nighthawkempires.essentials.commands;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.getCommandManager;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class TimeCommand implements CommandExecutor {

    public TimeCommand() {
        getCommandManager().registerCommands("time", new String[] {
                "ne.time"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Time    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("time", "<time>", "Set the current time"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.time")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                case 1:
                    World.Environment environment = player.getWorld().getEnvironment();

                    if (environment == World.Environment.NETHER || environment == World.Environment.THE_END) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "The time can not be set in this world."));
                        return true;
                    }

                    switch (args[0].toLowerCase()) {
                        case "day":
                            player.getWorld().setTime(1000L);
                            player.sendMessage(getMessages().getChatMessage(GRAY + "You have set the time to " + GOLD + "day" + GRAY
                                    + " in world " + AQUA + player.getWorld().getName() + GRAY + "."));
                            return true;
                        case "noon":
                            player.getWorld().setTime(6000L);
                            player.sendMessage(getMessages().getChatMessage(GRAY + "You have set the time to " + GOLD + "noon" + GRAY
                                    + " in world " + AQUA + player.getWorld().getName() + GRAY + "."));
                            return true;
                        case "night":
                            player.getWorld().setTime(13000L);
                            player.sendMessage(getMessages().getChatMessage(GRAY + "You have set the time to " + GOLD + "night" + GRAY
                                    + " in world " + AQUA + player.getWorld().getName() + GRAY + "."));
                            return true;
                        default:
                            if (!NumberUtils.isDigits(args[0])) {
                                player.sendMessage(getMessages().getChatMessage(GRAY + "Please make sure the time is a valid number."));
                                return true;
                            }

                            long time = Long.parseLong(args[0]);
                            player.getWorld().setTime(time);
                            player.sendMessage(getMessages().getChatMessage(GRAY + "You have set the time to " + GOLD + time + GRAY
                                    + " in world " + AQUA + player.getWorld().getName() + GRAY + "."));
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
