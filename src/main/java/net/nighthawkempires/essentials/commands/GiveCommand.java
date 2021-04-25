package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.essentials.EssentialsPlugin;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.*;
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

public class GiveCommand implements CommandExecutor {

    public GiveCommand() {
        getCommandManager().registerCommands("give", new String[] {
                "ne.give"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Give    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("give", "[player] <item> [amount]", "Give item command"),
            getMessages().getMessage(CHAT_FOOTER),
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.give")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                case 1:
                    Material material = getMaterials().getMaterial(args[0]);
                    if (material == null) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but that is not a valid item."));
                        return true;
                    }

                    player.getInventory().addItem(new ItemStack(material, material.getMaxStackSize()));
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have been given " + GOLD + material.getMaxStackSize()
                            + AQUA + " " + enumName(args[0]) + GRAY + "."));
                    return true;
                case 2:
                    String name = args[0];
                    material = getMaterials().getMaterial(name);
                    if (material == null) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                        if (!offlinePlayer.isOnline()) {
                            player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                            return true;
                        } else {
                            String matName = args[1];
                            material = getMaterials().getMaterial(matName);

                            if (material == null) {
                                player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but that is not a valid item."));
                                return true;
                            }

                            Player target = offlinePlayer.getPlayer();
                            target.getInventory().addItem(new ItemStack(material, material.getMaxStackSize()));
                            player.sendMessage(getMessages().getChatMessage(GRAY + "You have added " + GOLD + material.getMaxStackSize()
                                    + AQUA + " " + enumName(matName) + GRAY + " to " + GREEN + target.getName() + "'s " + GRAY + "inventory."));
                            target.sendMessage(getMessages().getChatMessage(GRAY + "You have been given " + GOLD + material.getMaxStackSize()
                                    + AQUA + " " + enumName(matName) + GRAY + "."));
                            return true;
                        }
                    } else {
                        if (!NumberUtils.isNumber(args[1])) {
                            player.sendMessage(getMessages().getChatMessage(ChatColor.GRAY + "The amount must be a number instead of a string."));
                            return true;
                        }

                        int amount = Integer.parseInt(args[1]);

                        player.getInventory().addItem(new ItemStack(material, amount));
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have been given " + GOLD + amount
                                + AQUA + " " + enumName(args[0]) + GRAY + "."));
                        return true;
                    }
                case 3:
                    name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);

                    if (!offlinePlayer.isOnline()) {
                        player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }

                    String matName = args[1];
                    material = getMaterials().getMaterial(matName);
                    if (material == null) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but that is not a valid item."));
                        return true;
                    }

                    if (!NumberUtils.isNumber(args[2])) {
                        player.sendMessage(getMessages().getChatMessage(ChatColor.GRAY + "The amount must be a number instead of a string."));
                        return true;
                    }

                    int amount = Integer.parseInt(args[2]);

                    Player target = offlinePlayer.getPlayer();
                    target.getInventory().addItem(new ItemStack(material, amount));
                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have added " + GOLD + amount
                            + AQUA + " " + enumName(matName) + GRAY + " to " + GREEN + target.getName() + "'s " + GRAY + "inventory."));
                    target.sendMessage(getMessages().getChatMessage(GRAY + "You have been given " + GOLD + amount
                            + AQUA + " " + enumName(matName) + GRAY + "."));
                    return true;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            help = new String[] {
                    getMessages().getMessage(CHAT_HEADER),
                    DARK_GRAY + "Command" + GRAY + ": Give    " + DARK_GRAY + "    [Optional], <Required>",
                    getMessages().getMessage(CHAT_FOOTER),
                    getMessages().getCommand("give", "<player> <item> [amount]", "Give item command"),
                    getMessages().getMessage(CHAT_FOOTER),
            };

            switch (args.length) {
                case 0:
                case 1:
                    sender.sendMessage(help);
                    return true;
                case 2:
                    String name = args[0];
                    Material material = getMaterials().getMaterial(name);
                    if (material == null) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                        if (!offlinePlayer.isOnline()) {
                            sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                            return true;
                        } else {
                            String matName = args[1];
                            material = getMaterials().getMaterial(matName);

                            if (material == null) {
                                sender.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but that is not a valid item."));
                                return true;
                            }

                            Player target = offlinePlayer.getPlayer();
                            target.getInventory().addItem(new ItemStack(material, material.getMaxStackSize()));
                            sender.sendMessage(getMessages().getChatMessage(GRAY + "You have added " + GOLD + material.getMaxStackSize()
                                    + AQUA + " " + enumName(matName) + GRAY + " to " + GREEN + target.getName() + "'s " + GRAY + "inventory."));
                            target.sendMessage(getMessages().getChatMessage(GRAY + "You have been given " + GOLD + material.getMaxStackSize()
                                    + AQUA + " " + enumName(matName) + GRAY + "."));
                            return true;
                        }
                    }
                case 3:
                    name = args[0];
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);

                    if (!offlinePlayer.isOnline()) {
                        sender.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                        return true;
                    }

                    String matName = args[1];
                    material = getMaterials().getMaterial(matName);
                    if (material == null) {
                        sender.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but that is not a valid item."));
                        return true;
                    }

                    if (!NumberUtils.isNumber(args[2])) {
                        sender.sendMessage(getMessages().getChatMessage(ChatColor.GRAY + "The amount must be a number instead of a string."));
                        return true;
                    }

                    int amount = Integer.parseInt(args[2]);

                    Player target = offlinePlayer.getPlayer();
                    target.getInventory().addItem(new ItemStack(material, amount));
                    sender.sendMessage(getMessages().getChatMessage(GRAY + "You have added " + GOLD + amount
                            + AQUA + " " + enumName(matName) + GRAY + " to " + GREEN + target.getName() + "'s " + GRAY + "inventory."));
                    target.sendMessage(getMessages().getChatMessage(GRAY + "You have been given " + GOLD + amount
                            + AQUA + " " + enumName(matName) + GRAY + "."));
                    return true;
            }
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