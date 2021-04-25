package net.nighthawkempires.essentials.tabcompleters;

import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.CHAT_FOOTER;
import static net.nighthawkempires.core.lang.Messages.CHAT_HEADER;
import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.GRAY;

public class ClearInventoryTabCompleter {

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Clear Inventory    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("clearinv", "[player]", "Clear a player's inventory"),
            getMessages().getMessage(CHAT_FOOTER)
    };
}
