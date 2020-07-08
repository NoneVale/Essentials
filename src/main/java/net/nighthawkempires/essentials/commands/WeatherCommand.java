package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.lang.Messages;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.getCommandManager;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.core.lang.Messages.INVALID_SYNTAX;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class WeatherCommand implements CommandExecutor {

    public WeatherCommand() {
        getCommandManager().registerCommands("weather", new String[] {
                "ne.weather"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Weather    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("weather", "list", "Show a list of weather conditions"),
            getMessages().getCommand("weather", "set <condition>", "Set the weather condition"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.weather")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                case 1:
                    switch (args[0].toLowerCase()) {
                        case "list":
                            String[] list = new String[]{
                                    getMessages().getMessage(CHAT_HEADER),
                                    translateAlternateColorCodes('&', "&8List&7: Warps"),
                                    getMessages().getMessage(CHAT_FOOTER),
                                    translateAlternateColorCodes('&', "&8Warps&7: "),
                                    translateAlternateColorCodes('&', "&8 - " + GREEN + "Clear"
                                            + DARK_GRAY + ", " + GREEN + "Rain" + DARK_GRAY + ", " + GREEN + "Thunder"),
                                    getMessages().getMessage(CHAT_FOOTER)
                            };
                            player.sendMessage(list);
                            return true;
                        default:
                            player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                            return true;
                    }
                case 2:
                    World.Environment environment = player.getWorld().getEnvironment();

                    if (environment == World.Environment.NETHER || environment == World.Environment.THE_END) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "The weather can not be set in this world."));
                        return true;
                    }

                    switch (args[0].toLowerCase()) {
                        case "set":
                            switch (args[1].toLowerCase()) {
                                case "clear":
                                    player.getWorld().setStorm(false);
                                    player.getWorld().setThundering(false);
                                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have set the weather conditions in world "
                                        + AQUA + player.getWorld().getName() + GRAY + " to " + YELLOW + "clear" + GRAY + "."));
                                    return true;
                                case "rain":
                                    player.getWorld().setStorm(true);
                                    player.getWorld().setThundering(false);
                                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have set the weather conditions in world "
                                            + AQUA + player.getWorld().getName() + GRAY + " to " + YELLOW + "raining" + GRAY + "."));
                                    return true;
                                case "thunder":
                                    player.getWorld().setStorm(true);
                                    player.getWorld().setThundering(true);
                                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have set the weather conditions in world "
                                            + AQUA + player.getWorld().getName() + GRAY + " to " + YELLOW + "thundering" + GRAY + "."));
                                    return true;
                                default:
                                    player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but that is not a valid weather condition."));
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
        }
        return false;
    }
}
