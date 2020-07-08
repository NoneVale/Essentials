package net.nighthawkempires.essentials.commands;

import com.google.common.collect.Maps;
import net.nighthawkempires.core.bans.Ban;
import net.nighthawkempires.core.bans.BanType;
import net.nighthawkempires.core.mute.MuteType;
import net.nighthawkempires.core.user.UserModel;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;

public class BanCommand implements CommandExecutor {

    public BanCommand() {
        getCommandManager().registerCommands("ban", new String[] {
                "ne.ban.temp", "ne.ban.perm"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Ban    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("ban", "help", "Show this help menu"),
            getMessages().getCommand("ban", "<player> [reason]", "Permanently ban a player"),
            getMessages().getCommand("ban", "<player> [time] [reason]", "Temporarily ban a player"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel userModel = getUserRegistry().getUser(player.getUniqueId());
            
            if (!player.hasPermission("ne.ban.perm") && !player.hasPermission("ne.ban.temp")) {
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
                            if (!player.hasPermission("ne.ban.perm")) {
                                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                                return true;
                            }

                            String targetName = args[0];
                            OfflinePlayer offlineTargetPlayer = Bukkit.getOfflinePlayer(targetName);
                            if (!getUserRegistry().userExists(offlineTargetPlayer.getUniqueId())) {
                                player.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                                return true;
                            }

                            UserModel targetUserModel = getUserRegistry().getUser(offlineTargetPlayer.getUniqueId());

                            Map<String, Object> banMap = Maps.newHashMap();
                            banMap.put("banned-by", player.getUniqueId().toString());
                            banMap.put("banned-user", offlineTargetPlayer.getUniqueId().toString());
                            banMap.put("ban-type", BanType.PERM.toString());
                            banMap.put("ban-reason", "Unspecified");
                            banMap.put("ban-issued", System.currentTimeMillis());
                            banMap.put("ban-active", true);
                            Ban ban = new Ban(banMap);
                            targetUserModel.ban(ban);

                            player.sendMessage(getMessages().getChatMessage(GRAY + "You have permanently banned " + GREEN + offlineTargetPlayer.getName()
                                    + GRAY + " for " + YELLOW + "Unspecified" + GRAY + "."));
                            getMessages().broadcatServerMessage(GREEN + targetUserModel.getUserName() + GRAY + " has been banned by " + GREEN
                                    + player.getName() + GRAY + " for " + YELLOW + "Unspecified" + GRAY + ".");
                            if (offlineTargetPlayer.isOnline()) {
                                offlineTargetPlayer.getPlayer().kickPlayer(ban.getBanInfo());
                            }
                            return true;
                    }
                case 2:
                    if (args[1].startsWith("-")) {
                        if (!player.hasPermission("ne.ban.temp")) {
                            player.sendMessage(getMessages().getChatTag(NO_PERMS));
                            return true;
                        }

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

                        Map<String, Object> banMap = Maps.newHashMap();
                        banMap.put("banned-by", player.getUniqueId().toString());
                        banMap.put("banned-user", offlineTargetPlayer.getUniqueId().toString());
                        banMap.put("ban-type", MuteType.TEMP.toString());
                        banMap.put("ban-reason", "Unspecified");
                        banMap.put("ban-issued", System.currentTimeMillis());
                        banMap.put("banned-until", mutedUntil.getTime());
                        banMap.put("ban-active", true);
                        Ban ban = new Ban(banMap);
                        targetUserModel.ban(ban);

                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have temporarily banned " + GREEN + offlineTargetPlayer.getName() + GRAY
                                + " for " + GOLD + duration + " " + timeUnit.toString() + GRAY + " for " + YELLOW + "Unspecified" + GRAY + "."));
                        getMessages().broadcatServerMessage(GREEN + targetUserModel.getUserName() + GRAY + " has been banned by " + GREEN
                                + player.getName() + GRAY + " for " + YELLOW + "Unspecified" + GRAY + ".");
                        if (offlineTargetPlayer.isOnline()) {
                            offlineTargetPlayer.getPlayer().kickPlayer(ban.getBanInfo());
                        }
                        return true;
                    } else {
                        if (!player.hasPermission("ne.ban.perm")) {
                            player.sendMessage(getMessages().getChatTag(NO_PERMS));
                            return true;
                        }

                        String targetName = args[0];
                        OfflinePlayer offlineTargetPlayer = Bukkit.getOfflinePlayer(targetName);
                        if (!getUserRegistry().userExists(offlineTargetPlayer.getUniqueId())) {
                            player.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                            return true;
                        }

                        UserModel targetUserModel = getUserRegistry().getUser(offlineTargetPlayer.getUniqueId());

                        String reason = args[1];

                        Map<String, Object> banMap = Maps.newHashMap();
                        banMap.put("banned-by", player.getUniqueId().toString());
                        banMap.put("banned-user", offlineTargetPlayer.getUniqueId().toString());
                        banMap.put("ban-type", BanType.PERM.toString());
                        banMap.put("ban-reason", reason);
                        banMap.put("ban-issued", System.currentTimeMillis());
                        banMap.put("ban-active", true);
                        Ban ban = new Ban(banMap);
                        targetUserModel.ban(ban);

                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have permanently banned " + GREEN + offlineTargetPlayer.getName()
                                + GRAY + " for " + YELLOW + reason + GRAY + "."));
                        getMessages().broadcatServerMessage(GREEN + targetUserModel.getUserName() + GRAY + " has been banned by " + GREEN
                                + player.getName() + GRAY + " for " + YELLOW + reason + GRAY + ".");
                        if (offlineTargetPlayer.isOnline()) {
                            offlineTargetPlayer.getPlayer().kickPlayer(ban.getBanInfo());
                        }
                        return true;
                    }
                default:
                    if (args[1].startsWith("-")) {
                        if (!player.hasPermission("ne.ban.temp")) {
                            player.sendMessage(getMessages().getChatTag(NO_PERMS));
                            return true;
                        }

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

                        Map<String, Object> banMap = Maps.newHashMap();
                        banMap.put("banned-by", player.getUniqueId().toString());
                        banMap.put("banned-user", offlineTargetPlayer.getUniqueId().toString());
                        banMap.put("ban-type", MuteType.TEMP.toString());
                        banMap.put("ban-reason", reason.toString());
                        banMap.put("ban-issued", System.currentTimeMillis());
                        banMap.put("banned-until", mutedUntil.getTime());
                        banMap.put("ban-active", true);
                        Ban ban = new Ban(banMap);
                        targetUserModel.ban(ban);

                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have temporarily banned " + GREEN + offlineTargetPlayer.getName() + GRAY
                                + " for " + GOLD + duration + " " + timeUnit.toString() + GRAY + " for " + YELLOW + reason.toString() + GRAY + "."));
                        getMessages().broadcatServerMessage(GREEN + targetUserModel.getUserName() + GRAY + " has been banned by " + GREEN
                                + player.getName() + GRAY + " for " + YELLOW + reason.toString() + GRAY + ".");
                        if (offlineTargetPlayer.isOnline()) {
                            offlineTargetPlayer.getPlayer().kickPlayer(ban.getBanInfo());
                        }
                        return true;
                    } else {
                        if (!player.hasPermission("ne.ban.perm")) {
                            player.sendMessage(getMessages().getChatTag(NO_PERMS));
                            return true;
                        }

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

                        Map<String, Object> banMap = Maps.newHashMap();
                        banMap.put("banned-by", player.getUniqueId().toString());
                        banMap.put("banned-user", offlineTargetPlayer.getUniqueId().toString());
                        banMap.put("ban-type", BanType.PERM.toString());
                        banMap.put("ban-reason", reason.toString());
                        banMap.put("ban-issued", System.currentTimeMillis());
                        banMap.put("ban-active", true);
                        Ban ban = new Ban(banMap);
                        targetUserModel.ban(ban);

                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have permanently banned " + GREEN + offlineTargetPlayer.getName()
                                + GRAY + " for " + YELLOW + reason.toString() + GRAY + "."));
                        getMessages().broadcatServerMessage(GREEN + targetUserModel.getUserName() + GRAY + " has been banned by " + GREEN
                                + player.getName() + GRAY + " for " + YELLOW + reason.toString() + GRAY + ".");
                        if (offlineTargetPlayer.isOnline()) {
                            offlineTargetPlayer.getPlayer().kickPlayer(ban.getBanInfo());
                        }
                        return true;
                    }
            }
        }
        return false;
    }
}
