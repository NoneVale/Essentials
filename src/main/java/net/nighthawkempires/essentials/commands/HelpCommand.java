package net.nighthawkempires.essentials.commands;

import com.google.common.collect.Lists;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.core.lang.Messages.*;
import static org.bukkit.ChatColor.*;

public class HelpCommand implements CommandExecutor {

    public HelpCommand() {
        getCommandManager().registerCommands("help", new String[] {
                "ne.help"
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.help")) {
                player.sendMessage(getMessages().getChatTag(NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    sendPage(player, 1);
                    return true;
                case 1:
                    for (String pluginName : getPlugins()) {
                        if (args[0].toLowerCase().equals(pluginName.toLowerCase())) {
                            Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);

                            if (plugin == null) {
                                player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but that is not a valid help topic."));
                                return true;
                            }

                            PluginDescriptionFile description = plugin.getDescription();

                            StringBuilder authorBuilder = new StringBuilder();
                            List<String> authors = description.getAuthors();
                            if (authors != null && !authors.isEmpty()) {
                                for (int i = 0; i < authors.size(); i++) {
                                    authorBuilder.append(BLUE).append(authors.get(i));

                                    if (i < authors.size() - 1) {
                                        authorBuilder.append(DARK_GRAY).append(", ");
                                    }
                                }
                            }

                            if (authors.isEmpty()) {
                                authorBuilder.append(RED).append("None");
                            }

                            StringBuilder commandBuilder = new StringBuilder();
                            List<String> commands = Lists.newArrayList(description.getCommands().keySet());
                            List<String> availableCommands = getCommands(player);
                            if (commands != null && !commands.isEmpty()) {
                                for (int i = 0; i < commands.size(); i++) {
                                    if (availableCommands.contains(commands.get(i))) {
                                        commandBuilder.append(GREEN).append(commands.get(i));

                                        if (i < commands.size() - 1) {
                                            commandBuilder.append(DARK_GRAY).append(", ");
                                        }
                                    }
                                }
                            }

                            if (commands.isEmpty()) {
                                commandBuilder.append(RED).append("None");
                            }

                            String[] pluginInfo = {
                                    getMessages().getMessage(CHAT_HEADER),
                                    getMessages().getHelpTopic(plugin.getName()),
                                    getMessages().getMessage(CHAT_FOOTER),
                                    DARK_GRAY + "Plugin Name" + GRAY + ": " + BLUE + plugin.getName(),
                                    DARK_GRAY + "Version" + GRAY + ": " + GOLD + plugin.getDescription().getVersion(),
                                    DARK_GRAY + "Author(s)" + GRAY + ": " + authorBuilder.toString().trim(),
                                    DARK_GRAY + "Description" + GRAY + ": " + GRAY + (plugin.getDescription().getDescription() != null ? plugin.getDescription().getDescription() : ""),
                                    DARK_GRAY + "Commands" + GRAY + ": " + commandBuilder.toString().trim(),
                                    getMessages().getMessage(CHAT_FOOTER)
                            };
                            player.sendMessage(pluginInfo);
                            return true;
                        }
                    }
                    for (String commandName : getCommands(player)) {
                        if (args[0].toLowerCase().equals(commandName.toLowerCase())) {
                            PluginCommand pluginCommand = Bukkit.getPluginCommand(commandName);

                            if (pluginCommand == null) {
                                player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but that is not a valid help topic."));
                                return true;
                            }

                            StringBuilder aliasBuilder = new StringBuilder();
                            List<String> aliases = pluginCommand.getAliases();
                            for (int i = 0; i < aliases.size(); i++) {
                                aliasBuilder.append(GREEN).append(aliases.get(i));

                                if (i < aliases.size() - 1) {
                                    aliasBuilder.append(DARK_GRAY).append(", ");
                                }
                            }

                            if (aliases.isEmpty()) {
                                aliasBuilder.append(RED).append("None");
                            }

                            String[] commandInfo = {
                                    getMessages().getMessage(CHAT_HEADER),
                                    getMessages().getHelpTopic(pluginCommand.getName()),
                                    getMessages().getMessage(CHAT_FOOTER),
                                    DARK_GRAY + "Name" + GRAY + ": " + GREEN + pluginCommand.getName(),
                                    DARK_GRAY + "Description" + GRAY + ": " + pluginCommand.getDescription(),
                                    DARK_GRAY + "Aliases" + GRAY + ": " + aliasBuilder.toString().trim(),
                                    getMessages().getMessage(CHAT_FOOTER),
                            };
                            player.sendMessage(commandInfo);
                            return true;
                        }
                    }

                    if (NumberUtils.isDigits(args[0])) {
                        int page = Integer.parseInt(args[0]);
                        if (page > 0 && page <= getTotalPages(player)) {
                            sendPage(player, page);
                            return true;
                        } else {
                            player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but that is an invalid page number."));
                            return true;
                        }
                    } else {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but that is not a valid help topic."));
                        return true;
                    }
            }
        }
        return false;
    }

    private List<String> getPlugins() {
        List<String> plugins = Lists.newArrayList();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (!plugin.getDescription().getCommands().isEmpty()) {
                plugins.add(plugin.getName());
            }
        }

        plugins.sort(Collator.getInstance());
        return plugins;
    }

    private List<String> getCommands(CommandSender sender) {
        List<String> commands = Lists.newArrayList();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (!plugin.getDescription().getCommands().isEmpty()) {
                for (String command : plugin.getDescription().getCommands().keySet()) {
                    if (getCommandManager().isRegistered(command)) {
                        for (String permission : getCommandManager().getCommandPermissions().get(command)) {
                            if (sender.hasPermission(permission) && !commands.contains(command)) {
                                commands.add(command);
                            }
                        }
                    } else {
                        if (Bukkit.getPluginCommand(command).getPermission() != null) {
                            if (sender.hasPermission(Bukkit.getPluginCommand(command).getPermission())) {
                                commands.add(command);
                            }
                        }
                    }
                }
            }
        }

        commands.sort(Collator.getInstance());
        return commands;
    }

    private List<String> getHelpTopics(CommandSender sender) {
        List<String> plugins = Lists.newArrayList();
        List<String> commands = Lists.newArrayList();
        for (String pluginName : getPlugins()) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
            plugins.add(BLUE + plugin.getName() + DARK_GRAY + " - " + GRAY +
                    (plugin.getDescription().getDescription() != null ? plugin.getDescription().getDescription() :
                            ""));
        }

        for (String command : getCommands(sender)) {
            commands.add(AQUA + "/" + command + DARK_GRAY + " - " + GRAY +
                    (Bukkit.getPluginCommand(command).getDescription() != null ?
                            Bukkit.getPluginCommand(command).getDescription() : ""));
        }

        plugins.sort(Collator.getInstance());
        commands.sort(Collator.getInstance());

        List<String> together = Lists.newArrayList();
        together.addAll(plugins);
        together.addAll(commands);

        return together;
    }

    private int getTotalPages(CommandSender sender) {
        return (int) Math.ceil((double) getHelpTopics(sender).size() / 10);
    }

    private void sendPage(CommandSender sender, int page) {

        int displayPage = page;
        page = page - 1;

        int start = 10 * page;
        int finish;

        if (start + 10 > getHelpTopics(sender).size()) {
            finish = getHelpTopics(sender).size();
        } else {
            finish = start + 10;
        }

        List<String> helpTopics = getHelpTopics(sender);

        String[] help = new String[]{
                getMessages().getMessage(CHAT_HEADER),
                getHelpLine(displayPage, getTotalPages(sender)),
                getMessages().getMessage(CHAT_HEADER),
        };
        sender.sendMessage(help);

        for (int i = start; i < finish; i++) {
            sender.sendMessage(helpTopics.get(i));
        }

        sender.sendMessage(getMessages().getMessage(CHAT_HEADER));
    }

    public String getHelpLine(int page, int total) {
        return DARK_GRAY + "Help Topic Page" + GRAY + ": " + DARK_GRAY + "[" + GOLD + ""
                + UNDERLINE + page + DARK_GRAY + "/" + GOLD + "" + UNDERLINE + total + DARK_GRAY + "]";
    }
}