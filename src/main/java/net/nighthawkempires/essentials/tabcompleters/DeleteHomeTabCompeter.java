package net.nighthawkempires.essentials.tabcompleters;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.location.player.PlayerLocationModel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;

public class DeleteHomeTabCompeter implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = Lists.newArrayList();
        if (sender instanceof Player player) {
            if (!player.hasPermission("ne.home")) return completions;

            switch (args.length) {
                case 1 -> {
                    PlayerLocationModel playerLocationModel = CorePlugin.getPlayerLocationRegistry().getPlayerLocations(player.getUniqueId());
                    List<String> options = Lists.newArrayList(playerLocationModel.getHomes());
                    StringUtil.copyPartialMatches(args[0], options, completions);
                    Collections.sort(completions);
                }
                default -> {
                    return completions;
                }
            }
        }
        return completions;
    }
}
