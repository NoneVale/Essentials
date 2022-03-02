package net.nighthawkempires.essentials.tabcompleters;

import com.google.common.collect.Lists;
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

public class BanTabCompleter implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = Lists.newArrayList();
        if (sender instanceof Player player) {
            if (!player.hasPermission("ne.ban.perm") && !player.hasPermission("ne.ban.temp")) return completions;

            switch (args.length) {
                case 1 -> {
                    List<String> options = Lists.newArrayList("help");
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        options.add(online.getName());
                    }

                    StringUtil.copyPartialMatches(args[0], options, completions);
                    Collections.sort(completions);
                    return completions;
                }
                case 2 -> {
                    List<String> options = Lists.newArrayList("-1s", "-1m", "-1h", "-1d", "-2s", "-2m", "-2h", "-2d", "reason");
                    StringUtil.copyPartialMatches(args[1], options, completions);
                    return completions;
                }
                case 3 -> {
                    List<String> options = Lists.newArrayList("reason");
                    StringUtil.copyPartialMatches(args[1], options, completions);
                    return completions;
                }
            }
        }
        return completions;
    }
}
