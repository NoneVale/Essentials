package net.nighthawkempires.essentials.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.nighthawkempires.essentials.EssentialsPlugin;
import net.nighthawkempires.essentials.kit.KitModel;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.essentials.EssentialsPlugin.*;
import static org.bukkit.ChatColor.*;

public class CreateKitCommand implements CommandExecutor {

    public CreateKitCommand() {
        getCommandManager().registerCommands("createkit", new String[] {
                "ne.kits.manage"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Create Kit    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("createkit", "help", "Show this help menu"),
            getMessages().getCommand("createkit", "<name> <cooldown>", "Create a kit"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.kits.manage")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                case 1:
                    if (args[0].toLowerCase().equals("help")) {
                        player.sendMessage(help);
                        return true;
                    } else {
                        player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                        return true;
                    }
                case 2:
                    String name = args[0];

                    if (getKitRegistry().kitExists(name)) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but a kit already exists with that name."));
                        return true;
                    }

                    String timeString = args[1].substring(0, args[1].length() - 1);

                    if (!NumberUtils.isNumber(timeString)) {
                        player.sendMessage(getMessages().getChatMessage(ChatColor.GRAY + "The duration must be a number instead of a string."));
                        return true;
                    }

                    int duration = Integer.parseInt(timeString);

                    String timeUnitString = args[1].substring(args[1].length() - 1);

                    Date kitCooldown = null;
                    TimeUnit timeUnit = null;
                    switch (timeUnitString.toLowerCase()) {
                        case "s":
                            timeUnit = TimeUnit.SECONDS;
                            kitCooldown = DateUtils.addSeconds(new Date(), duration);
                            break;
                        case "m":
                            timeUnit = TimeUnit.MINUTES;
                            kitCooldown = DateUtils.addMinutes(new Date(), duration);
                            break;
                        case "h":
                            timeUnit = TimeUnit.HOURS;
                            kitCooldown = DateUtils.addHours(new Date(), duration);
                            break;
                        case "d":
                            timeUnit = TimeUnit.DAYS;
                            kitCooldown = DateUtils.addDays(new Date(), duration);
                            break;
                    }

                    if (kitCooldown == null || timeUnit == null) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but please suffix the end of" +
                                " the time argument with s for seconds, m for minutes, h for hours, or d for days."));
                        return true;
                    }

                    long cooldown = kitCooldown.getTime() - System.currentTimeMillis();

                    KitModel kitModel = getKitRegistry().createKit(name);
                    kitModel.setItems(player.getInventory());
                    kitModel.setCooldown(cooldown);

                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have created kit " + AQUA + name + GRAY
                            + " with a cooldown of " + GOLD + duration + " " + timeUnit.toString().toLowerCase() + GRAY + "."));
                    return true;
                default:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }
        return false;
    }
}