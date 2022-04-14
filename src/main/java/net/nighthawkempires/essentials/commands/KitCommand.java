package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.essentials.EssentialsPlugin;
import net.nighthawkempires.essentials.kit.KitModel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.essentials.EssentialsPlugin.*;
import static org.bukkit.ChatColor.*;

public class KitCommand implements CommandExecutor {

    public KitCommand() {
        getCommandManager().registerCommands("kit", new String[] {
                "ne.kit"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Kit    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("kit", "<name>", "Redeem a kit"),
            getMessages().getMessage(CHAT_FOOTER),
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {

            if (!player.hasPermission("ne.kit")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    return true;
                case 1:
                    String name = args[0];

                    if (!getKitRegistry().kitExists(name)) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but there isn't a kit that exists with that name."));
                        return true;
                    }

                    KitModel kitModel = getKitRegistry().getKit(name);

                    if (!player.hasPermission("ne.kit." + kitModel.getKey().toLowerCase())) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but you do not have permission to use this kit."));
                        return true;
                    }

                    if (!kitModel.canUse(player.getUniqueId())) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but you have " + GOLD + kitModel.timeLeft(player.getUniqueId())
                                + GRAY + " before you can use this kit again."));
                        return true;
                    }

                    for (ItemStack itemStack : kitModel.getItems()) {
                        player.getInventory().addItem(itemStack);
                    }

                    kitModel.addCooldown(player.getUniqueId());
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have redeemed kit " + AQUA + kitModel.getKey() + GRAY + "."));
                    return true;
                default:
                    player.sendMessage(getMessages().getChatTag(INVALID_SYNTAX));
                    return true;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(getMessages().getChatMessage(GRAY + "This command is not available from the console."));
            return true;
        }
        return false;
    }
}
