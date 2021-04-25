package net.nighthawkempires.essentials.commands;

import com.google.common.collect.Maps;
import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.mute.MuteType;
import net.nighthawkempires.core.user.UserModel;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;

public class MuteCommand implements CommandExecutor {

    public MuteCommand() {
        getCommandManager().registerCommands("mute", new String[] {
                "ne.mute.temp", "ne.mute.perm"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Mute    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("mute", "help", "Show this help menu"),
            getMessages().getCommand("mute", "<player> [reason]", "Permanently mute a player"),
            getMessages().getCommand("mute", "<player> [time] [reason]", "Temporarily mute a player"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel userModel = getUserRegistry().getUser(player.getUniqueId());

            if (!player.hasPermission("ne.mute")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                case 1:
                    switch (args[0].toLowerCase()) {
                        case "help":
                            player.sendMessage(help);
                            return true;
                        default:
                            String targetName = args[0];
                            OfflinePlayer offlineTargetPlayer = Bukkit.getOfflinePlayer(targetName);
                            if (!getUserRegistry().userExists(offlineTargetPlayer.getUniqueId())) {
                                player.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                                return true;
                            }

                            UserModel targetUserModel = getUserRegistry().getUser(offlineTargetPlayer.getUniqueId());

                            Map<String, Object> muteMap = Maps.newHashMap();
                            muteMap.put("muted-by", player.getUniqueId().toString());
                            muteMap.put("muted-user", offlineTargetPlayer.getUniqueId().toString());
                            muteMap.put("mute-type", MuteType.PERM.toString());
                            muteMap.put("mute-reason", "Unspecified");
                            muteMap.put("mute-issued", System.currentTimeMillis());
                            muteMap.put("mute-active", true);
                            targetUserModel.mute(muteMap);

                            player.sendMessage(getMessages().getChatMessage(GRAY + "You have permanently muted " + GREEN + offlineTargetPlayer.getName()
                                    + GRAY + " for " + YELLOW + "Unspecified" + GRAY + "."));
                            if (offlineTargetPlayer.isOnline()) {
                                offlineTargetPlayer.getPlayer().sendMessage(getMessages().getChatMessage(GRAY + "You have been muted by " + GREEN
                                        + player.getName() + GRAY + " for " + YELLOW + "Unspecified" + GRAY + "."));
                            }
                            return true;
                    }
                case 2:
                    if (args[1].startsWith("-")) {
                        String targetName = args[0];
                        OfflinePlayer offlineTargetPlayer = Bukkit.getOfflinePlayer(targetName);
                        if (!getUserRegistry().userExists(offlineTargetPlayer.getUniqueId())) {
                            player.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                            return true;
                        }

                        UserModel targetUserModel = getUserRegistry().getUser(offlineTargetPlayer.getUniqueId());

                        String timeString = args[1].substring(1, args[1].length() - 1);

                        if (!NumberUtils.isNumber(timeString)) {
                            player.sendMessage(getMessages().getChatMessage(ChatColor.GRAY + "The duration must be a number instead of a string."));
                            return true;
                        }

                        int duration = Integer.parseInt(timeString);

                        String timeUnitString = args[1].substring(args[1].length() - 1);

                        Date mutedUntil = null;
                        TimeUnit timeUnit = null;
                        switch (timeUnitString.toLowerCase()) {
                            case "s":
                                timeUnit = TimeUnit.SECONDS;
                                mutedUntil = DateUtils.addSeconds(new Date(), duration);
                                break;
                            case "m":
                                timeUnit = TimeUnit.MINUTES;
                                mutedUntil = DateUtils.addMinutes(new Date(), duration);
                                break;
                            case "h":
                                timeUnit = TimeUnit.HOURS;
                                mutedUntil = DateUtils.addHours(new Date(), duration);
                                break;
                            case "d":
                                timeUnit = TimeUnit.DAYS;
                                mutedUntil = DateUtils.addDays(new Date(), duration);
                                break;
                        }

                        if (mutedUntil == null) {
                            player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but please suffix the end of" +
                                    " the time argument with s for seconds, m for minutes, h for hours, or d for days."));
                            return true;
                        }

                        Map<String, Object> muteMap = Maps.newHashMap();
                        muteMap.put("muted-by", player.getUniqueId().toString());
                        muteMap.put("muted-user", offlineTargetPlayer.getUniqueId().toString());
                        muteMap.put("mute-type", MuteType.TEMP.toString());
                        muteMap.put("mute-reason", "Unspecified");
                        muteMap.put("mute-issued", System.currentTimeMillis());
                        muteMap.put("muted-until", mutedUntil.getTime());
                        muteMap.put("mute-active", true);
                        targetUserModel.mute(muteMap);

                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have temporarily muted " + GREEN + offlineTargetPlayer.getName() + GRAY
                                + " for " + GOLD + duration + " " + timeUnit.toString() + GRAY + " for " + YELLOW + "Unspecified" + GRAY + "."));
                        if (offlineTargetPlayer.isOnline()) {
                            offlineTargetPlayer.getPlayer().sendMessage(getMessages().getChatMessage(GRAY + "You have been temporarily muted for " + GOLD
                                    + duration + " " + timeUnit.toString() + GRAY + " for " + YELLOW + "Unspecified" + GRAY + "."));
                        }
                        return true;
                    } else {
                        String targetName = args[0];
                        OfflinePlayer offlineTargetPlayer = Bukkit.getOfflinePlayer(targetName);
                        if (!getUserRegistry().userExists(offlineTargetPlayer.getUniqueId())) {
                            player.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                            return true;
                        }

                        UserModel targetUserModel = getUserRegistry().getUser(offlineTargetPlayer.getUniqueId());

                        String reason = args[1];

                        Map<String, Object> muteMap = Maps.newHashMap();
                        muteMap.put("muted-by", player.getUniqueId().toString());
                        muteMap.put("muted-user", offlineTargetPlayer.getUniqueId().toString());
                        muteMap.put("mute-type", MuteType.PERM.toString());
                        muteMap.put("mute-reason", reason);
                        muteMap.put("mute-issued", System.currentTimeMillis());
                        muteMap.put("mute-active", true);
                        targetUserModel.mute(muteMap);

                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have permanently muted " + GREEN + offlineTargetPlayer.getName()
                                + GRAY + " for " + YELLOW + reason + GRAY + "."));
                        if (offlineTargetPlayer.isOnline()) {
                            offlineTargetPlayer.getPlayer().sendMessage(getMessages().getChatMessage(GRAY + "You have been muted by " + GREEN
                                    + player.getName() + GRAY + " for " + YELLOW + reason + GRAY + "."));
                        }
                        return true;
                    }
                default:
                    if (args[1].startsWith("-")) {
                        String targetName = args[0];
                        OfflinePlayer offlineTargetPlayer = Bukkit.getOfflinePlayer(targetName);
                        if (!getUserRegistry().userExists(offlineTargetPlayer.getUniqueId())) {
                            player.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                            return true;
                        }

                        UserModel targetUserModel = getUserRegistry().getUser(offlineTargetPlayer.getUniqueId());

                        String timeString = args[1].substring(1, args[1].length() - 1);

                        if (!NumberUtils.isNumber(timeString)) {
                            player.sendMessage(getMessages().getChatMessage(ChatColor.GRAY + "The duration must be a number instead of a string."));
                            return true;
                        }

                        int duration = Integer.parseInt(timeString);

                        String timeUnitString = args[1].substring(args[1].length() - 1);

                        Date mutedUntil = null;
                        TimeUnit timeUnit = null;
                        switch (timeUnitString.toLowerCase()) {
                            case "s":
                                timeUnit = TimeUnit.SECONDS;
                                mutedUntil = DateUtils.addSeconds(new Date(), duration);
                                break;
                            case "m":
                                timeUnit = TimeUnit.MINUTES;
                                mutedUntil = DateUtils.addMinutes(new Date(), duration);
                                break;
                            case "h":
                                timeUnit = TimeUnit.HOURS;
                                mutedUntil = DateUtils.addHours(new Date(), duration);
                                break;
                            case "d":
                                timeUnit = TimeUnit.DAYS;
                                mutedUntil = DateUtils.addDays(new Date(), duration);
                                break;
                        }

                        if (mutedUntil == null) {
                            player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but please suffix the end of" +
                                    " the time argument with s for seconds, m for minutes, h for hours, or d for days."));
                            return true;
                        }

                        StringBuilder reason = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            reason.append(args[i]);
                            if (i < args.length - 1)
                                reason.append(" ");
                        }

                        Map<String, Object> muteMap = Maps.newHashMap();
                        muteMap.put("muted-by", player.getUniqueId().toString());
                        muteMap.put("muted-user", offlineTargetPlayer.getUniqueId().toString());
                        muteMap.put("mute-type", MuteType.TEMP.toString());
                        muteMap.put("mute-reason", reason.toString());
                        muteMap.put("mute-issued", System.currentTimeMillis());
                        muteMap.put("muted-until", mutedUntil.getTime());
                        muteMap.put("mute-active", true);
                        targetUserModel.mute(muteMap);

                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have temporarily muted " + GREEN + offlineTargetPlayer.getName() + GRAY
                                + " for " + GOLD + duration + " " + timeUnit.toString() + GRAY + " for " + YELLOW + reason.toString() + GRAY + "."));
                        if (offlineTargetPlayer.isOnline()) {
                            offlineTargetPlayer.getPlayer().sendMessage(getMessages().getChatMessage(GRAY + "You have been temporarily muted for " + GOLD
                                    + duration + " " + timeUnit.toString() + GRAY + " for " + YELLOW + reason.toString() + GRAY + "."));
                        }
                        return true;
                    } else {
                        String targetName = args[0];
                        OfflinePlayer offlineTargetPlayer = Bukkit.getOfflinePlayer(targetName);
                        if (!getUserRegistry().userExists(offlineTargetPlayer.getUniqueId())) {
                            player.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                            return true;
                        }

                        UserModel targetUserModel = getUserRegistry().getUser(offlineTargetPlayer.getUniqueId());

                        StringBuilder reason = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            reason.append(args[i]);
                            if (i < args.length - 1)
                                reason.append(" ");
                        }
                        Map<String, Object> muteMap = Maps.newHashMap();
                        muteMap.put("muted-by", player.getUniqueId().toString());
                        muteMap.put("muted-user", offlineTargetPlayer.getUniqueId().toString());
                        muteMap.put("mute-type", MuteType.PERM.toString());
                        muteMap.put("mute-reason", reason.toString());
                        muteMap.put("mute-issued", System.currentTimeMillis());
                        muteMap.put("mute-active", true);
                        targetUserModel.mute(muteMap);

                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have permanently muted " + GREEN + offlineTargetPlayer.getName()
                                + GRAY + " for " + YELLOW + reason.toString() + GRAY + "."));
                        if (offlineTargetPlayer.isOnline()) {
                            offlineTargetPlayer.getPlayer().sendMessage(getMessages().getChatMessage(GRAY + "You have been muted by " + GREEN
                                    + player.getName() + GRAY + " for " + YELLOW + reason.toString() + GRAY + "."));
                        }
                        return true;
                    }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            switch (args.length) {
                case 0:
                    sender.sendMessage(help);
                    return true;
                case 1:
                    switch (args[0].toLowerCase()) {
                        case "help":
                            sender.sendMessage(help);
                            return true;
                        default:
                            String targetName = args[0];
                            OfflinePlayer offlineTargetPlayer = Bukkit.getOfflinePlayer(targetName);
                            if (!getUserRegistry().userExists(offlineTargetPlayer.getUniqueId())) {
                                sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                                return true;
                            }

                            UserModel targetUserModel = getUserRegistry().getUser(offlineTargetPlayer.getUniqueId());

                            Map<String, Object> muteMap = Maps.newHashMap();
                            muteMap.put("muted-by", CorePlugin.getConfigg().getConsoleUuid().toString());
                            muteMap.put("muted-user", offlineTargetPlayer.getUniqueId().toString());
                            muteMap.put("mute-type", MuteType.PERM.toString());
                            muteMap.put("mute-reason", "Unspecified");
                            muteMap.put("mute-issued", System.currentTimeMillis());
                            muteMap.put("mute-active", true);
                            targetUserModel.mute(muteMap);

                            sender.sendMessage(getMessages().getChatMessage(GRAY + "You have permanently muted " + GREEN + offlineTargetPlayer.getName()
                                    + GRAY + " for " + YELLOW + "Unspecified" + GRAY + "."));
                            if (offlineTargetPlayer.isOnline()) {
                                offlineTargetPlayer.getPlayer().sendMessage(getMessages().getChatMessage(GRAY + "You have been muted by " + GREEN
                                        + CorePlugin.getConfigg().getConsoleDisplayName() + GRAY + " for " + YELLOW + "Unspecified" + GRAY + "."));
                            }
                            return true;
                    }
                case 2:
                    if (args[1].startsWith("-")) {
                        String targetName = args[0];
                        OfflinePlayer offlineTargetPlayer = Bukkit.getOfflinePlayer(targetName);
                        if (!getUserRegistry().userExists(offlineTargetPlayer.getUniqueId())) {
                            sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                            return true;
                        }

                        UserModel targetUserModel = getUserRegistry().getUser(offlineTargetPlayer.getUniqueId());

                        String timeString = args[1].substring(1, args[1].length() - 1);

                        if (!NumberUtils.isNumber(timeString)) {
                            sender.sendMessage(getMessages().getChatMessage(ChatColor.GRAY + "The duration must be a number instead of a string."));
                            return true;
                        }

                        int duration = Integer.parseInt(timeString);

                        String timeUnitString = args[1].substring(args[1].length() - 1);

                        Date mutedUntil = null;
                        TimeUnit timeUnit = null;
                        switch (timeUnitString.toLowerCase()) {
                            case "s":
                                timeUnit = TimeUnit.SECONDS;
                                mutedUntil = DateUtils.addSeconds(new Date(), duration);
                                break;
                            case "m":
                                timeUnit = TimeUnit.MINUTES;
                                mutedUntil = DateUtils.addMinutes(new Date(), duration);
                                break;
                            case "h":
                                timeUnit = TimeUnit.HOURS;
                                mutedUntil = DateUtils.addHours(new Date(), duration);
                                break;
                            case "d":
                                timeUnit = TimeUnit.DAYS;
                                mutedUntil = DateUtils.addDays(new Date(), duration);
                                break;
                        }

                        if (mutedUntil == null) {
                            sender.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but please suffix the end of" +
                                    " the time argument with s for seconds, m for minutes, h for hours, or d for days."));
                            return true;
                        }

                        Map<String, Object> muteMap = Maps.newHashMap();
                        muteMap.put("muted-by", CorePlugin.getConfigg().getConsoleUuid().toString());
                        muteMap.put("muted-user", offlineTargetPlayer.getUniqueId().toString());
                        muteMap.put("mute-type", MuteType.TEMP.toString());
                        muteMap.put("mute-reason", "Unspecified");
                        muteMap.put("mute-issued", System.currentTimeMillis());
                        muteMap.put("muted-until", mutedUntil.getTime());
                        muteMap.put("mute-active", true);
                        targetUserModel.mute(muteMap);

                        sender.sendMessage(getMessages().getChatMessage(GRAY + "You have temporarily muted " + GREEN + offlineTargetPlayer.getName() + GRAY
                                + " for " + GOLD + duration + " " + timeUnit.toString() + GRAY + " for " + YELLOW + "Unspecified" + GRAY + "."));
                        if (offlineTargetPlayer.isOnline()) {
                            offlineTargetPlayer.getPlayer().sendMessage(getMessages().getChatMessage(GRAY + "You have been temporarily muted for " + GOLD
                                    + duration + " " + timeUnit.toString() + GRAY + " for " + YELLOW + "Unspecified" + GRAY + "."));
                        }
                        return true;
                    } else {
                        String targetName = args[0];
                        OfflinePlayer offlineTargetPlayer = Bukkit.getOfflinePlayer(targetName);
                        if (!getUserRegistry().userExists(offlineTargetPlayer.getUniqueId())) {
                            sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                            return true;
                        }

                        UserModel targetUserModel = getUserRegistry().getUser(offlineTargetPlayer.getUniqueId());

                        String reason = args[1];

                        Map<String, Object> muteMap = Maps.newHashMap();
                        muteMap.put("muted-by", CorePlugin.getConfigg().getConsoleUuid().toString());
                        muteMap.put("muted-user", offlineTargetPlayer.getUniqueId().toString());
                        muteMap.put("mute-type", MuteType.PERM.toString());
                        muteMap.put("mute-reason", reason);
                        muteMap.put("mute-issued", System.currentTimeMillis());
                        muteMap.put("mute-active", true);
                        targetUserModel.mute(muteMap);

                        sender.sendMessage(getMessages().getChatMessage(GRAY + "You have permanently muted " + GREEN + offlineTargetPlayer.getName()
                                + GRAY + " for " + YELLOW + reason + GRAY + "."));
                        if (offlineTargetPlayer.isOnline()) {
                            offlineTargetPlayer.getPlayer().sendMessage(getMessages().getChatMessage(GRAY + "You have been muted by " + GREEN
                                    + CorePlugin.getConfigg().getConsoleDisplayName() + GRAY + " for " + YELLOW + reason + GRAY + "."));
                        }
                        return true;
                    }
                default:
                    if (args[1].startsWith("-")) {
                        String targetName = args[0];
                        OfflinePlayer offlineTargetPlayer = Bukkit.getOfflinePlayer(targetName);
                        if (!getUserRegistry().userExists(offlineTargetPlayer.getUniqueId())) {
                            sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                            return true;
                        }

                        UserModel targetUserModel = getUserRegistry().getUser(offlineTargetPlayer.getUniqueId());

                        String timeString = args[1].substring(1, args[1].length() - 1);

                        if (!NumberUtils.isNumber(timeString)) {
                            sender.sendMessage(getMessages().getChatMessage(ChatColor.GRAY + "The duration must be a number instead of a string."));
                            return true;
                        }

                        int duration = Integer.parseInt(timeString);

                        String timeUnitString = args[1].substring(args[1].length() - 1);

                        Date mutedUntil = null;
                        TimeUnit timeUnit = null;
                        switch (timeUnitString.toLowerCase()) {
                            case "s":
                                timeUnit = TimeUnit.SECONDS;
                                mutedUntil = DateUtils.addSeconds(new Date(), duration);
                                break;
                            case "m":
                                timeUnit = TimeUnit.MINUTES;
                                mutedUntil = DateUtils.addMinutes(new Date(), duration);
                                break;
                            case "h":
                                timeUnit = TimeUnit.HOURS;
                                mutedUntil = DateUtils.addHours(new Date(), duration);
                                break;
                            case "d":
                                timeUnit = TimeUnit.DAYS;
                                mutedUntil = DateUtils.addDays(new Date(), duration);
                                break;
                        }

                        if (mutedUntil == null) {
                            sender.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but please suffix the end of" +
                                    " the time argument with s for seconds, m for minutes, h for hours, or d for days."));
                            return true;
                        }

                        StringBuilder reason = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            reason.append(args[i]);
                            if (i < args.length - 1)
                                reason.append(" ");
                        }

                        Map<String, Object> muteMap = Maps.newHashMap();
                        muteMap.put("muted-by", CorePlugin.getConfigg().getConsoleUuid().toString());
                        muteMap.put("muted-user", offlineTargetPlayer.getUniqueId().toString());
                        muteMap.put("mute-type", MuteType.TEMP.toString());
                        muteMap.put("mute-reason", reason.toString());
                        muteMap.put("mute-issued", System.currentTimeMillis());
                        muteMap.put("muted-until", mutedUntil.getTime());
                        muteMap.put("mute-active", true);
                        targetUserModel.mute(muteMap);

                        sender.sendMessage(getMessages().getChatMessage(GRAY + "You have temporarily muted " + GREEN + offlineTargetPlayer.getName() + GRAY
                                + " for " + GOLD + duration + " " + timeUnit.toString() + GRAY + " for " + YELLOW + reason.toString() + GRAY + "."));
                        if (offlineTargetPlayer.isOnline()) {
                            offlineTargetPlayer.getPlayer().sendMessage(getMessages().getChatMessage(GRAY + "You have been temporarily muted for " + GOLD
                                    + duration + " " + timeUnit.toString() + GRAY + " for " + YELLOW + reason.toString() + GRAY + "."));
                        }
                        return true;
                    } else {
                        String targetName = args[0];
                        OfflinePlayer offlineTargetPlayer = Bukkit.getOfflinePlayer(targetName);
                        if (!getUserRegistry().userExists(offlineTargetPlayer.getUniqueId())) {
                            sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                            return true;
                        }

                        UserModel targetUserModel = getUserRegistry().getUser(offlineTargetPlayer.getUniqueId());

                        StringBuilder reason = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            reason.append(args[i]);
                            if (i < args.length - 1)
                                reason.append(" ");
                        }
                        Map<String, Object> muteMap = Maps.newHashMap();
                        muteMap.put("muted-by", CorePlugin.getConfigg().getConsoleUuid().toString());
                        muteMap.put("muted-user", offlineTargetPlayer.getUniqueId().toString());
                        muteMap.put("mute-type", MuteType.PERM.toString());
                        muteMap.put("mute-reason", reason.toString());
                        muteMap.put("mute-issued", System.currentTimeMillis());
                        muteMap.put("mute-active", true);
                        targetUserModel.mute(muteMap);

                        sender.sendMessage(getMessages().getChatMessage(GRAY + "You have permanently muted " + GREEN + offlineTargetPlayer.getName()
                                + GRAY + " for " + YELLOW + reason.toString() + GRAY + "."));
                        if (offlineTargetPlayer.isOnline()) {
                            offlineTargetPlayer.getPlayer().sendMessage(getMessages().getChatMessage(GRAY + "You have been muted by " + GREEN
                                    + CorePlugin.getConfigg().getConsoleDisplayName() + GRAY + " for " + YELLOW + reason.toString() + GRAY + "."));
                        }
                        return true;
                    }
            }
        }
        return false;
    }
}
