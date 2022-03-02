package net.nighthawkempires.essentials.tabcompleters;

import com.google.common.collect.Lists;
import net.nighthawkempires.essentials.EssentialsPlugin;
import net.nighthawkempires.essentials.kit.KitModel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;

public class DeleteKitTabCompleter implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = Lists.newArrayList();
        if (sender instanceof Player player) {
            if (!player.hasPermission("ne.kits.manage")) return completions;

            switch (args.length) {
                case 1 -> {
                    List<String> options = Lists.newArrayList();
                    EssentialsPlugin.getKitRegistry().getKits().forEach(kit -> options.add(kit.getKey()));
                    StringUtil.copyPartialMatches(args[0], options, completions);
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
