package net.nighthawkempires.essentials.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;

public class RenameCommand implements CommandExecutor {

    public RenameCommand() {
        getCommandManager().registerCommands("rename", new String[] {
                "ne.rename"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Rename    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("rename", "<name>", "Rename the item in your hand."),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.rename")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                default:
                    PlayerInventory inventory = player.getInventory();

                    if (inventory.getItemInMainHand() == null || inventory.getItemInMainHand().getType() == Material.AIR) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You must have something in your main hand in order to rename it."));
                        return true;
                    }

                    StringBuilder itemNameBuilder = new StringBuilder();
                    for (int i = 0; i < args.length; i++) {
                        itemNameBuilder.append(args[i]);

                        if (i < args.length - 1) {
                            itemNameBuilder.append(" ");
                        }
                    }

                    String name = translateAlternateColorCodes('&', itemNameBuilder.toString().trim());
                    String stripped = stripColor(name);
                    if (stripped.length() > 25) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but that name is too long."));
                        return true;
                    }

                    ItemStack itemStack = inventory.getItemInMainHand();
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(name);
                    itemStack.setItemMeta(itemMeta);
                    inventory.setItemInMainHand(itemStack);
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have renamed the item in your main hand to " + name + GRAY + "."));
                    return true;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(getMessages().getChatMessage(GRAY + "This command is not available from the console."));
            return true;
        }
        return false;
    }
}