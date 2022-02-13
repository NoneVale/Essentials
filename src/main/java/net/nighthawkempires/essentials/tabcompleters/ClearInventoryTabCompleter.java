package net.nighthawkempires.essentials.tabcompleters;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;

import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.CHAT_FOOTER;
import static net.nighthawkempires.core.lang.Messages.CHAT_HEADER;
import static org.bukkit.ChatColor.DARK_GRAY;
import static org.bukkit.ChatColor.GRAY;

public class ClearInventoryTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = Lists.newArrayList();
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.clearinventory.self") && !player.hasPermission("ne.clearinventory.other")) {
                return completions;
            }

            switch (args.length) {
                case 1:
                    if (!player.hasPermission("ne.clearinventory.other")) return completions;
                    List<String> arggs = Lists.newArrayList();
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        arggs.add(players.getName());
                    }
                    StringUtil.copyPartialMatches(args[0], arggs, completions);
                    Collections.sort(completions);
                    return completions;
            }
        }
        return completions;
    }
}
