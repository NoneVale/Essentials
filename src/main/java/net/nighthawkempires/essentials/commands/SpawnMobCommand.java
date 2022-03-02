package net.nighthawkempires.essentials.commands;

import com.google.common.collect.Sets;
import net.nighthawkempires.core.lang.Messages;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

import static net.nighthawkempires.core.CorePlugin.getCommandManager;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.*;
import static net.nighthawkempires.core.lang.Messages.INVALID_SYNTAX;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GRAY;

public class SpawnMobCommand implements CommandExecutor {

    public SpawnMobCommand() {
        getCommandManager().registerCommands("spawnmob", new String[]{
                "ne.spawnmob"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(CHAT_HEADER),
            DARK_GRAY + "Command" + GRAY + ": Spawn Mob    " + DARK_GRAY + "    [Optional], <Required>",
            getMessages().getMessage(CHAT_FOOTER),
            getMessages().getCommand("spawnmob", "list", "Show a list of mobs"),
            getMessages().getCommand("spawnmob", "<type> [amount]", "Spawn a mob"),
            getMessages().getMessage(CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {

            if (!player.hasPermission("ne.spawnmob")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                case 1:
                    String typeName = args[0].toUpperCase();

                    if (typeName.toLowerCase().equals("list")) {
                        StringBuilder mobList = new StringBuilder();
                        for (int i = 0; i < EntityType.values().length; i++) {
                            mobList.append(GREEN).append(EntityType.values()[i].name());

                            if (i < EntityType.values().length - 1) {
                                mobList.append(DARK_GRAY).append(", ");
                            }
                        }

                        String[] list = new String[]{
                                getMessages().getMessage(Messages.CHAT_HEADER),
                                ChatColor.translateAlternateColorCodes('&', "&8List&7: Entity Type"),
                                getMessages().getMessage(Messages.CHAT_FOOTER),
                                ChatColor.translateAlternateColorCodes('&', "&8Entity Types&7: "),
                                mobList.toString(),
                                getMessages().getMessage(Messages.CHAT_FOOTER)
                        };
                        player.sendMessage(list);

                        return true;
                    }

                    EntityType type = null;
                    for (EntityType entityType : EntityType.values()) {
                        if (typeName.equals(entityType.name())) {
                            type = entityType;
                            break;
                        }
                    }

                    if (type == null) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but that is not a valid entity type."));
                        return true;
                    }

                    Location targetLocation = getTargetBlock(player);
                    if (targetLocation == null) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but I couldn't find the block you're looking at."));
                        return true;
                    }

                    try {
                        player.getWorld().spawnEntity(targetLocation, type);
                    } catch (Exception e) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but that entity type is not spawnabled."));
                        return true;
                    }

                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have spawned " + GOLD + "1 " + BLUE + enumName(type.name()) + GRAY + "."));
                    return true;
                case 2:
                    typeName = args[0].toUpperCase();
                    type = null;
                    for (EntityType entityType : EntityType.values()) {
                        if (typeName.equals(entityType.name())) {
                            type = entityType;
                            break;
                        }
                    }

                    if (type == null) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but that is not a valid entity type."));
                        return true;
                    }

                    if (!NumberUtils.isDigits(args[1])) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but the amount must be a valid number."));
                        return true;
                    }

                    int amount = Integer.parseInt(args[1]);

                    targetLocation = getTargetBlock(player);
                    if (targetLocation == null) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but I couldn't find the block you're looking at."));
                        return true;
                    }

                    try {
                        for (int i = 0; i < amount; i++) {
                            player.getWorld().spawnEntity(targetLocation, type);
                        }
                    } catch (Exception e) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but that entity type is not spawnabled."));
                        return true;
                    }

                    player.sendMessage(getMessages().getChatMessage(GRAY + "You have spawned " + GOLD + amount + " " + BLUE + enumName(type.name()) + GRAY + "."));
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

    private Location getTargetBlock(Player player) {
        Set<Material> materialSet = Sets.newConcurrentHashSet();
        for (Material material : Material.values()) {
            if (!material.isAir() && material.isSolid()) {
                materialSet.add(material);
            }
        }
        List<Block> blocks = player.getLineOfSight(materialSet, 15);
        Block block = blocks.get(0);
        return player.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ()).getLocation();
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
