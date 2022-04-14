package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.CorePlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DiscordCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Here is the link for the Discord: " + ChatColor.AQUA + "https://discord.gg/ZR3qZfHA"));
        }
        return false;
    }
}
