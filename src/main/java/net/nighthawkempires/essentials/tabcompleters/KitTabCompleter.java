package net.nighthawkempires.essentials.tabcompleters;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.essentials.EssentialsPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;

public class KitTabCompleter implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = Lists.newArrayList();
        if (sender instanceof Player player) {
            if (!player.hasPermission("ne.kit")) return completions;

            switch (args.length) {
                case 1 -> {
                    List<String> options = Lists.newArrayList();
                    EssentialsPlugin.getKitRegistry().getKits().stream().filter(kit -> player.hasPermission("ne.kit" + kit.getKey().toLowerCase()))
                            .filter(kit -> kit.canUse(player.getUniqueId())).forEach(kit -> options.add(kit.getKey().toLowerCase()));
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
