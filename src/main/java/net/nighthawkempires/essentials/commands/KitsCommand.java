package net.nighthawkempires.essentials.commands;

import net.nighthawkempires.core.lang.Messages;
import net.nighthawkempires.essentials.kit.KitModel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.getCommandManager;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static org.bukkit.ChatColor.*;
import static net.nighthawkempires.essentials.EssentialsPlugin.*;

public class KitsCommand implements CommandExecutor {

    public KitsCommand() {
        getCommandManager().registerCommands("kits", new String[] {
                "ne.kits"
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            switch (args.length) {
                case 0:
                    StringBuilder kitBuilder = new StringBuilder();
                    kitBuilder.append(ChatColor.translateAlternateColorCodes('&', "&8 - "));
                    for (int i = 0; i < getKitRegistry().loadAllFromDb().keySet().size(); i++) {
                        KitModel kitModel = getKitRegistry().getKits().get(i);

                        if (player.hasPermission("ne.kit." + kitModel.getKey().toLowerCase())) {
                            kitBuilder.append(kitModel.getKey());

                            if (i < getKitRegistry().getKits().size() - 1) {
                                kitBuilder.append(DARK_GRAY).append(", ");
                            }
                        }
                    }

                    if (getKitRegistry().getKits().isEmpty()) {
                        kitBuilder.append(RED + "None");
                    }

                    String kits = kitBuilder.toString().trim();
                    if (kits.endsWith(",")) {
                        kits = kits.substring(0, kits.length() - 1);
                    }

                    String[] list = new String[] {
                            getMessages().getMessage(Messages.CHAT_HEADER),
                            ChatColor.translateAlternateColorCodes('&', "&8List&7: Kits"),
                            getMessages().getMessage(Messages.CHAT_FOOTER),
                            ChatColor.translateAlternateColorCodes('&', "&8Kits&7: "),
                            kits,
                            getMessages().getMessage(Messages.CHAT_FOOTER)
                    };
                    player.sendMessage(list);
                    return true;
            }
        }
        return false;
    }
}
