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

import static net.nighthawkempires.core.CorePlugin.getCommandManager;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class HatCommand implements CommandExecutor {

    public HatCommand() {
        getCommandManager().registerCommands("hat", new String[] {
                "ne.hat"
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.hat")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    PlayerInventory inventory = player.getInventory();

                    if (inventory.getItemInMainHand() == null || inventory.getItemInMainHand().getType() == Material.AIR) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You must have something in your main hand in order to rename it."));
                        return true;
                    }

                    ItemStack itemStack = inventory.getItemInMainHand();

                    if (!itemStack.getType().isBlock()) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have to have a block in your hand in order to do this."));
                        return true;
                    }

                    player.getInventory().setItemInMainHand(player.getInventory().getHelmet());
                    player.getInventory().setHelmet(itemStack);

                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have put " + AQUA + enumName(itemStack.getType().name()) + GRAY + " on your head."));
                    return true;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(getMessages().getChatMessage(GRAY + "This command is not available from the console."));
            return true;
        }
        return false;
    }

    private String enumName(String s) {
        if (s.contains("_")) {
            String[] split = s.split("_");

            StringBuilder matName = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                matName.append(enumName(split[i]));

                if (i < split.length - 1) {
                    matName.append(" ");
                }
            }

            return matName.toString();
        }

        return s.toUpperCase().substring(0, 1) + s.substring(1).toLowerCase();
    }
}