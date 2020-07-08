package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;

public class NicknameCommand implements CommandExecutor {

    public NicknameCommand() {
        getCommandManager().registerCommands("nickname", new String[] {
                "ne.nickname.self", "ne.nickname.other"
        });
    }

    private String[] helpAdmin = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Nickname    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("nick", "[player] <nickname>", "Set a nickname"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Nickname    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("nick", "<nickname>", "Set a nickname"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel userModel = getUserRegistry().getUser(player.getUniqueId());

            if (!player.hasPermission("ne.nickname.self") && !player.hasPermission("ne.nickname.other")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    if (player.hasPermission("ne.nickname.other")) {
                        player.sendMessage(helpAdmin);
                        return true;
                    } else {
                        player.sendMessage(help);
                        return true;
                    }
                case 1:
                    if (!player.hasPermission("ne.nickname.self")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    String nickname = ChatColor.translateAlternateColorCodes('&', args[0]);
                    userModel.setDisplayName(nickname);
                    player.setDisplayName(nickname);
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have set your nickname to " + nickname + GRAY + "."));
                    return true;
                case 2:
                    if (!player.hasPermission("ne.nickname.other")) {
                        player.sendMessage(getMessages().getChatTag(NO_PERMS));
                        return true;
                    }

                    String name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                    if (!getUserRegistry().userExists(offlinePlayer.getUniqueId())) {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                        return true;
                    }

                    userModel = getUserRegistry().getUser(offlinePlayer.getUniqueId());

                    nickname = ChatColor.translateAlternateColorCodes('&', args[1]);
                    userModel.setDisplayName(nickname);
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have set " + GREEN + offlinePlayer.getName() + "'s "
                            + GRAY + " nickname to " + nickname + GRAY + "."));
                    if (offlinePlayer.isOnline()) {
                        Player target = offlinePlayer.getPlayer();
                        target.setDisplayName(nickname);
                        target.sendMessage(getMessages().getChatMessage(GRAY + "Your nickname has been set to " + nickname + GRAY + "."));
                    }
                    return true;
            }
        }
        return false;
    }
}
