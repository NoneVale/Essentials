package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.lang.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.checkerframework.checker.units.qual.C;

public class ServerBooksCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("ne.server.books")) {
                player.sendMessage(CorePlugin.getMessages().getMessage(Messages.NO_PERMS));
                return true;
            }

            ItemStack welcome = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta bookMeta = (BookMeta) welcome.getItemMeta();
            bookMeta.setTitle("Welcome to Nighthawk Empires");
            bookMeta.setAuthor("Nighthawk Empires");

            String contents = "Welcome to Nighthawk Empires, we are happy that you are willing to give our server a chance.  We strive to provide the best gameplay experience possible for our player base.  We have custom plugins, that have been made to offer players a unique experience, with updates to current plugins, and more plugins planned for the future.  If you need help with anything, our Helpers and Moderators will be glad to assist you.  We hope that you enjoy yourself!";
            String[] words = contents.split(" ");
            StringBuilder builder = new StringBuilder();
            for (String s : words) {
                if (builder.length() + s.length() < 256) {
                    builder.append(s).append(" ");
                } else {
                    bookMeta.addPage(builder.toString());
                    builder = new StringBuilder();
                }
            }

            if (builder.length() > 0) {
                bookMeta.addPage(builder.toString());
                builder = new StringBuilder();
            }

            welcome.setItemMeta(bookMeta);
            player.getInventory().addItem(welcome);
        }
        return true;
    }
}
