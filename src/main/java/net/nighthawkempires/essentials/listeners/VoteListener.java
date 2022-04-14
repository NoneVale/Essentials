package net.nighthawkempires.essentials.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.server.ServerType;
import net.nighthawkempires.core.user.UserModel;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.logging.log4j.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class VoteListener implements Listener {

    @EventHandler
    public void onVote(VotifierEvent event) {
        Vote vote = event.getVote();
        Player player = Bukkit.getPlayer(vote.getUsername());
        if (player != null) {
            Bukkit.getServer().broadcastMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GREEN + player.getName() + ChatColor.GRAY + " has voted on "
                    + ChatColor.AQUA + vote.getServiceName() + ChatColor.GRAY + "."));

            UserModel userModel = CorePlugin.getUserRegistry().getUser(player.getUniqueId());

            double voteBoost = 0;
            boolean boosted = false;
            for (PermissionAttachmentInfo info : player.getEffectivePermissions()) {
                if (info.getPermission().toLowerCase().startsWith("ne.voteboost.")) {
                    boosted = true;
                    String voteBoostString = info.getPermission().replaceFirst("ne.voteboost.", "");

                    if (NumberUtils.isDigits(voteBoostString)) {
                        int parsed = Integer.parseInt(voteBoostString);
                        double boost = (double) parsed / 100;

                        if (boost > voteBoost) voteBoost = boost;
                    }
                }
            }

            int tokens = (int) (2 * voteBoost) + 2;
            int money = (int) (20 * voteBoost) + 20;

            userModel.addServerBalance(ServerType.SURVIVAL, money);
            userModel.addTokens(tokens);
            userModel.addVoteCount(1);
            player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Thank you for voting, you have received " + ChatColor.YELLOW + "$" + ChatColor.GREEN
                    + money + ChatColor.GRAY  + " and " + ChatColor.GOLD + tokens + " tokens" + ChatColor.GRAY + "."));
            if (boosted) {
                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You received a " + ChatColor.GOLD + (voteBoost * 100) + "% " + ChatColor.GRAY
                        + " vote boost for being a donator."));
            }

            int voteCount = userModel.getVoteCount();
            if (voteCount % 5 == 0) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "soulchest give " + player.getName() + " 1");
            }
        }
    }
}
