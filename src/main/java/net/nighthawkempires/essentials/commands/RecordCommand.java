package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.bans.Ban;
import net.nighthawkempires.core.bans.BanType;
import net.nighthawkempires.core.kick.Kick;
import net.nighthawkempires.core.mute.Mute;
import net.nighthawkempires.core.mute.MuteType;
import net.nighthawkempires.core.user.UserModel;
import net.nighthawkempires.core.warning.Warning;
import net.nighthawkempires.essentials.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.core.lang.Messages.CHAT_HEADER;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class RecordCommand implements CommandExecutor {

    public RecordCommand() {
        getCommandManager().registerCommands("record", new String[] {
                "ne.record"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Record    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("record", "<player>", "Show a player's record"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.record")) {
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
                    if (!getUserRegistry().userExists(offlinePlayer.getUniqueId())) {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                        return true;
                    }

                    UserModel userModel = getUserRegistry().getUser(offlinePlayer.getUniqueId());

                    StringBuilder bans = new StringBuilder();
                    for (int i = 0; i < userModel.getBans().size(); i++) {
                        bans.append(DARK_GRAY).append(" - Ban #").append(i + 1).append("\n");
                        Ban ban = userModel.getBans().get(i);
                        String byName = (ban.getBannedBy().equals(CorePlugin.getConfigg().getConsoleUuid())
                                ? ChatColor.translateAlternateColorCodes('&', CorePlugin.getConfigg().getConsoleDisplayName())
                                : Bukkit.getOfflinePlayer(ban.getBannedBy()).getName());
                        bans.append(DARK_GRAY).append("      Banned By").append(GRAY).append(": ").append(byName);
                        bans.append(DARK_GRAY).append("    -    Ban Type").append(GRAY).append(": ").append(ban.getBanType().name()).append("\n");
                        bans.append(DARK_GRAY).append("      Ban Issued").append(GRAY).append(": ").append(ban.getBanIssuedDate()).append("\n");
                        if (ban.getBanType() == BanType.TEMP) {
                            bans.append(DARK_GRAY).append("      Banned Until").append(GRAY).append(": ").append(ban.getBannedUntilDate()).append("\n");
                        }
                        bans.append(DARK_GRAY).append("      Ban Reason").append(GRAY).append(": ").append(ban.getBanReason()).append("\n");
                    }

                    StringBuilder kicks = new StringBuilder();
                    for (int i = 0; i < userModel.getKicks().size(); i++) {
                        kicks.append(DARK_GRAY).append(" - Kick #").append(i + 1).append("\n");
                        Kick kick = userModel.getKicks().get(i);
                        String byName = (kick.getKickedBy().equals(CorePlugin.getConfigg().getConsoleUuid())
                                ? ChatColor.translateAlternateColorCodes('&', CorePlugin.getConfigg().getConsoleDisplayName())
                                : Bukkit.getOfflinePlayer(kick.getKickedBy()).getName());
                        kicks.append(DARK_GRAY).append("      Kicked By").append(GRAY).append(": ").append(byName).append("\n");
                        kicks.append(DARK_GRAY).append("      Kick Issued").append(GRAY).append(": ").append(kick.getKickIssuedDate()).append("\n");
                        kicks.append(DARK_GRAY).append("      Kick Reason").append(GRAY).append(": ").append(kick.getKickReason()).append("\n");
                    };

                    StringBuilder mutes = new StringBuilder();
                    for (int i = 0; i < userModel.getMutes().size(); i++) {
                        mutes.append(DARK_GRAY).append(" - Mute #").append(i + 1).append("\n");
                        Mute mute = userModel.getMutes().get(i);
                        String byName = (mute.getMutedBy().equals(CorePlugin.getConfigg().getConsoleUuid())
                                ? ChatColor.translateAlternateColorCodes('&', CorePlugin.getConfigg().getConsoleDisplayName())
                                : Bukkit.getOfflinePlayer(mute.getMutedBy()).getName());
                        mutes.append(DARK_GRAY).append("      Muted By").append(GRAY).append(": ").append(byName);
                        mutes.append(DARK_GRAY).append("    -    Mute Type").append(GRAY).append(": ").append(mute.getMuteType().name()).append("\n");
                        mutes.append(DARK_GRAY).append("      Mute Issued").append(GRAY).append(": ").append(mute.getMuteIssuedDate()).append("\n");
                        if (mute.getMuteType() == MuteType.TEMP) {
                            mutes.append(DARK_GRAY).append("      Muted Until").append(GRAY).append(": ").append(mute.getMutedUntilDate()).append("\n");
                        }
                        mutes.append(DARK_GRAY).append("      Mute Reason").append(GRAY).append(": ").append(mute.getMuteReason()).append("\n");
                    }

                    StringBuilder warns = new StringBuilder();
                    for (int i = 0; i < userModel.getWarnings().size(); i++) {
                        warns.append(DARK_GRAY).append(" - Warning #").append(i + 1).append("\n");
                        Warning warning = userModel.getWarnings().get(i);
                        String byName = (warning.getWarnedBy().equals(CorePlugin.getConfigg().getConsoleUuid())
                                ? ChatColor.translateAlternateColorCodes('&', CorePlugin.getConfigg().getConsoleDisplayName())
                                : Bukkit.getOfflinePlayer(warning.getWarnedBy()).getName());
                        warns.append(DARK_GRAY).append("      Warned By").append(GRAY).append(": ").append(byName);
                        warns.append(DARK_GRAY).append("    -    Warn Issued").append(GRAY).append(": ").append(warning.getWarnIssuedDate()).append("\n");
                        warns.append(DARK_GRAY).append("      Warn Reason").append(GRAY).append(": ").append(warning.getWarnReason()).append("\n");
                    }

                    String[] record = new String[]{
                            getMessages().getMessage(CHAT_HEADER),
                            DARK_GRAY + "Record" + GRAY + ": " + offlinePlayer.getName(),
                            getMessages().getMessage(CHAT_FOOTER),
                    };

                    player.sendMessage(record);
                    if (!userModel.getBans().isEmpty()) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8Bans&7: \n") + bans.toString().trim());
                    }
                    if (!userModel.getKicks().isEmpty()) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8Kicks&7: \n") + kicks.toString().trim());
                    }
                    if (!userModel.getMutes().isEmpty()) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8Mutes&7: \n") + mutes.toString().trim());
                    }
                    if (!userModel.getWarnings().isEmpty()) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8Warnings&7: \n") + warns.toString().trim());
                    }

                    if (userModel.getBans().isEmpty() && userModel.getKicks().isEmpty()
                            && userModel.getMutes().isEmpty() && userModel.getWarnings().isEmpty()) {
                        player.sendMessage(DARK_GRAY + " - " + RED + "None");
                    }
                    player.sendMessage(getMessages().getMessage(CHAT_FOOTER));
            }
        } else if (sender instanceof ConsoleCommandSender) {
            switch (args.length) {
                case 0:
                    sender.sendMessage(help);
                    return true;
                case 1:
                    String name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (!getUserRegistry().userExists(offlinePlayer.getUniqueId())) {
                        sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                        return true;
                    }

                    UserModel userModel = getUserRegistry().getUser(offlinePlayer.getUniqueId());

                    StringBuilder bans = new StringBuilder();
                    for (int i = 0; i < userModel.getBans().size(); i++) {
                        bans.append(DARK_GRAY).append(" - Ban #").append(i + 1).append("\n");
                        Ban ban = userModel.getBans().get(i);
                        String byName = (ban.getBannedBy().equals(CorePlugin.getConfigg().getConsoleUuid())
                                ? ChatColor.translateAlternateColorCodes('&', CorePlugin.getConfigg().getConsoleDisplayName())
                                : Bukkit.getOfflinePlayer(ban.getBannedBy()).getName());
                        bans.append(DARK_GRAY).append("      Banned By").append(GRAY).append(": ").append(byName);
                        bans.append(DARK_GRAY).append("    -    Ban Type").append(GRAY).append(": ").append(ban.getBanType().name()).append("\n");
                        bans.append(DARK_GRAY).append("      Ban Issued").append(GRAY).append(": ").append(ban.getBanIssuedDate()).append("\n");
                        if (ban.getBanType() == BanType.TEMP) {
                            bans.append(DARK_GRAY).append("      Banned Until").append(GRAY).append(": ").append(ban.getBannedUntilDate()).append("\n");
                        }
                        bans.append(DARK_GRAY).append("      Ban Reason").append(GRAY).append(": ").append(ban.getBanReason()).append("\n");
                    }

                    StringBuilder kicks = new StringBuilder();
                    for (int i = 0; i < userModel.getKicks().size(); i++) {
                        kicks.append(DARK_GRAY).append(" - Kick #").append(i + 1).append("\n");
                        Kick kick = userModel.getKicks().get(i);
                        String byName = (kick.getKickedBy().equals(CorePlugin.getConfigg().getConsoleUuid())
                                ? ChatColor.translateAlternateColorCodes('&', CorePlugin.getConfigg().getConsoleDisplayName())
                                : Bukkit.getOfflinePlayer(kick.getKickedBy()).getName());
                        kicks.append(DARK_GRAY).append("      Kicked By").append(GRAY).append(": ").append(byName).append("\n");
                        kicks.append(DARK_GRAY).append("      Kick Issued").append(GRAY).append(": ").append(kick.getKickIssuedDate()).append("\n");
                        kicks.append(DARK_GRAY).append("      Kick Reason").append(GRAY).append(": ").append(kick.getKickReason()).append("\n");
                    };

                    StringBuilder mutes = new StringBuilder();
                    for (int i = 0; i < userModel.getMutes().size(); i++) {
                        mutes.append(DARK_GRAY).append(" - Mute #").append(i + 1).append("\n");
                        Mute mute = userModel.getMutes().get(i);
                        String byName = (mute.getMutedBy().equals(CorePlugin.getConfigg().getConsoleUuid())
                                ? ChatColor.translateAlternateColorCodes('&', CorePlugin.getConfigg().getConsoleDisplayName())
                                : Bukkit.getOfflinePlayer(mute.getMutedBy()).getName());
                        mutes.append(DARK_GRAY).append("      Muted By").append(GRAY).append(": ").append(byName);
                        mutes.append(DARK_GRAY).append("    -    Mute Type").append(GRAY).append(": ").append(mute.getMuteType().name()).append("\n");
                        mutes.append(DARK_GRAY).append("      Mute Issued").append(GRAY).append(": ").append(mute.getMuteIssuedDate()).append("\n");
                        if (mute.getMuteType() == MuteType.TEMP) {
                            mutes.append(DARK_GRAY).append("      Muted Until").append(GRAY).append(": ").append(mute.getMutedUntilDate()).append("\n");
                        }
                        mutes.append(DARK_GRAY).append("      Mute Reason").append(GRAY).append(": ").append(mute.getMuteReason()).append("\n");
                    }

                    StringBuilder warns = new StringBuilder();
                    for (int i = 0; i < userModel.getWarnings().size(); i++) {
                        warns.append(DARK_GRAY).append(" - Warning #").append(i + 1).append("\n");
                        Warning warning = userModel.getWarnings().get(i);
                        String byName = (warning.getWarnedBy().equals(CorePlugin.getConfigg().getConsoleUuid())
                                ? ChatColor.translateAlternateColorCodes('&', CorePlugin.getConfigg().getConsoleDisplayName())
                                : Bukkit.getOfflinePlayer(warning.getWarnedBy()).getName());
                        warns.append(DARK_GRAY).append("      Warned By").append(GRAY).append(": ").append(byName);
                        warns.append(DARK_GRAY).append("    -    Warn Issued").append(GRAY).append(": ").append(warning.getWarnIssuedDate()).append("\n");
                        warns.append(DARK_GRAY).append("      Warn Reason").append(GRAY).append(": ").append(warning.getWarnReason()).append("\n");
                    }

                    String[] record = new String[]{
                            getMessages().getMessage(CHAT_HEADER),
                            DARK_GRAY + "Record" + GRAY + ": " + offlinePlayer.getName(),
                            getMessages().getMessage(CHAT_FOOTER),
                    };

                    sender.sendMessage(record);
                    if (!userModel.getBans().isEmpty()) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8Bans&7: \n") + bans.toString().trim());
                    }
                    if (!userModel.getKicks().isEmpty()) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8Kicks&7: \n") + kicks.toString().trim());
                    }
                    if (!userModel.getMutes().isEmpty()) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8Mutes&7: \n") + mutes.toString().trim());
                    }
                    if (!userModel.getWarnings().isEmpty()) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8Warnings&7: \n") + warns.toString().trim());
                    }

                    if (userModel.getBans().isEmpty() && userModel.getKicks().isEmpty()
                            && userModel.getMutes().isEmpty() && userModel.getWarnings().isEmpty()) {
                        sender.sendMessage(DARK_GRAY + " - " + RED + "None");
                    }
                    sender.sendMessage(getMessages().getMessage(CHAT_FOOTER));
            }
        }
        return true;
    }
}