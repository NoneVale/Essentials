package net.nighthawkempires.essentials.tabcompleters;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.location.player.PlayerLocationModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;

import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.CorePlugin.getPlayerLocationRegistry;
import static net.nighthawkempires.core.lang.Messages.PLAYER_NOT_FOUND;

public class HomeTabCompleter implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = Lists.newArrayList();
        if (sender instanceof Player player) {
            if (!player.hasPermission("ne.home")) return completions;

            switch (args.length) {
                case 1 -> {
                    PlayerLocationModel locationModel = CorePlugin.getPlayerLocationRegistry().getPlayerLocations(player.getUniqueId());
                    List<String> options = Lists.newArrayList(locationModel.getHomes());
                    StringUtil.copyPartialMatches(args[0], options, completions);
                    Collections.sort(completions);
                    return completions;
                }
                case 2 -> {
                    if (!player.hasPermission("ne.home.other")) return completions;

                    String targetName = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetName);
                    if (!getPlayerLocationRegistry().playerLocationsExist(offlinePlayer.getUniqueId())) return completions;

                    PlayerLocationModel locationModel = CorePlugin.getPlayerLocationRegistry().getPlayerLocations(offlinePlayer.getUniqueId());
                    List<String> options = Lists.newArrayList(locationModel.getHomes());
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
