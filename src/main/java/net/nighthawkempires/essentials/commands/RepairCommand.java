package net.nighthawkempires.essentials.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;

public class RepairCommand implements CommandExecutor {

    public RepairCommand() {
        getCommandManager().registerCommands("repair", new String[] {
                "ne.repair"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Repair    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("repair", "Repair the item in your hand"),
            getMessages().getCommand("repair", "all", "Repair all items in your inventory"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.repair")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    PlayerInventory inventory = player.getInventory();

                    if (inventory.getItemInMainHand() == null || inventory.getItemInMainHand().getType() == Material.AIR) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You must have something in your main hand in order to repair it"));
                        return true;
                    }

                    if (!canRepair(inventory.getItemInMainHand()) || getDamage(inventory.getItemInMainHand()) == 0) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "The item in your hand is not able to be repaired."));
                        return true;
                    }

                    repair(inventory.getItemInMainHand());
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have repaired the item in your main hand."));
                    return true;
                case 1:
                    if (args[0].toLowerCase().equals("all")) {
                        inventory = player.getInventory();

                        for (ItemStack itemStack : inventory.getContents()) {
                            repair(itemStack);
                        }

                        for (ItemStack itemStack : inventory.getArmorContents()) {
                            repair(itemStack);
                        }

                        for (ItemStack itemStack : inventory.getExtraContents()) {
                            repair(itemStack);
                        }

                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have repaired all items in your inventory."));
                        return true;
                    } else {
                        player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                        return true;
                    }
                default:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        }
        return false;
    }

    public void repair(ItemStack itemStack) {
        if (itemStack != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta instanceof Damageable) {
                ((Damageable) itemMeta).setDamage(0);
                itemStack.setItemMeta(itemMeta);
            }
        }
    }

    public int getDamage(ItemStack itemStack) {
        if (itemStack != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta instanceof Damageable) {
                return ((Damageable) itemMeta).getDamage();
            }
        }
        return 0;
    }

    public boolean canRepair(ItemStack itemStack) {
        if (itemStack != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            return itemMeta instanceof Damageable;
        }
        return false;
    }
}
