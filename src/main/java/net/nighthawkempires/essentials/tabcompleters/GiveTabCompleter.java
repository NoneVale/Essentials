package net.nighthawkempires.essentials.tabcompleters;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GiveTabCompleter implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = Lists.newArrayList();
        if (sender instanceof Player player) {
            if (!player.hasPermission("ne.give")) return completions;

            switch (args.length) {
                case 1 -> {
                    List<String> options = Lists.newArrayList();
                    Arrays.stream(Material.values()).forEach(material -> options.add(material.name().toLowerCase()));
                    StringUtil.copyPartialMatches(args[0], options, completions);
                    Collections.sort(completions);
                    return completions;
                }
                case 2 -> {
                    List<String> options = Lists.newArrayList();
                    Arrays.stream(Material.values()).forEach(material -> options.add(material.name().toLowerCase()));
                    StringUtil.copyPartialMatches(args[1], options, completions);
                    Collections.sort(completions);
                    return completions;
                }
                default -> {
                    return completions;
                }
            }
        }
        return completions;
    }
}
