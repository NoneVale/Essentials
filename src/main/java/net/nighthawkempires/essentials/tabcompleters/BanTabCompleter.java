package net.nighthawkempires.essentials.tabcompleters;

import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.CHAT_FOOTER;
import static net.nighthawkempires.core.lang.Messages.CHAT_HEADER;
import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.GRAY;

public class BanTabCompleter {

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Ban    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("ban", "help", "Show this help menu"),
            getMessages().getCommand("ban", "<player> [reason]", "Permanently ban a player"),
            getMessages().getCommand("ban", "<player> [time] [reason]", "Temporarily ban a player"),
            getMessages().getMessage(CHAT_FOOTER)
    };

}
