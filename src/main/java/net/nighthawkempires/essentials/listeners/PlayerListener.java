package net.nighthawkempires.essentials.listeners;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.location.PublicLocationModel;
import net.nighthawkempires.essentials.EssentialsPlugin;
import org.apache.logging.log4j.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.HashMap;
import java.util.UUID;

import static net.nighthawkempires.core.CorePlugin.*;
import static net.nighthawkempires.essentials.EssentialsPlugin.*;
import static org.bukkit.ChatColor.*;

public class PlayerListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {

            if (getPlayerData().getGodmodeList().contains(player.getUniqueId())) event.setCancelled(true);

            HashMap<UUID, String> warmingUp = getPlayerData().getCommandsWarmingUpMap();
            if (!event.isCancelled() && warmingUp.containsKey(player.getUniqueId())) {
                String[] parts = warmingUp.get(player.getUniqueId()).split(" ");
                String command = parts[0].toLowerCase();
                player.sendMessage(getMessages().getChatMessage(GRAY + "Command " + DARK_AQUA + "/" + command + GRAY
                        + " is no longer warming up due to you taking damage."));
                warmingUp.remove(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByBlockEvent event) {
        if (event.getEntity() instanceof Player player) {

            if (getPlayerData().getGodmodeList().contains(player.getUniqueId())) event.setCancelled(true);

            HashMap<UUID, String> warmingUp = getPlayerData().getCommandsWarmingUpMap();
            if (!event.isCancelled() && warmingUp.containsKey(player.getUniqueId())) {
                String[] parts = warmingUp.get(player.getUniqueId()).split(" ");
                String command = parts[0].toLowerCase();
                player.sendMessage(getMessages().getChatMessage(GRAY + "Command " + DARK_AQUA + "/" + command + GRAY
                        + " is no longer warming up due to you taking damage."));
                warmingUp.remove(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player) {

            if (getPlayerData().getGodmodeList().contains(player.getUniqueId())) event.setCancelled(true);

            HashMap<UUID, String> warmingUp = getPlayerData().getCommandsWarmingUpMap();
            if (!event.isCancelled() && warmingUp.containsKey(player.getUniqueId())) {
                String[] parts = warmingUp.get(player.getUniqueId()).split(" ");
                String command = parts[0].toLowerCase();
                player.sendMessage(getMessages().getChatMessage(GRAY + "Command " + DARK_AQUA + "/" + command + GRAY
                        + " is no longer warming up due to you taking damage."));
                warmingUp.remove(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        HashMap<UUID, String> warmingUp = getPlayerData().getCommandsWarmingUpMap();
        if (!event.isCancelled() && warmingUp.containsKey(player.getUniqueId())) {
            if (event.getFrom().getX() != event.getTo().getX() ||
                    event.getFrom().getY() != event.getTo().getY() ||
                        event.getFrom().getZ() != event.getTo().getZ()) {
                String[] parts = warmingUp.get(player.getUniqueId()).split(" ");
                String command = parts[0].toLowerCase();
                player.sendMessage(getMessages().getChatMessage(GRAY + "Command " + DARK_AQUA + "/" + command + GRAY
                        + " is no longer warming up due to you moving."));
                warmingUp.remove(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        for (UUID uuid : getPlayerData().getVanishList()) {
            if (player.getUniqueId() == uuid) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    if (!online.hasPermission("ne.operator")) {
                        online.hidePlayer(EssentialsPlugin.getPlugin(), player);
                    }
                }
            }

            if (!player.hasPermission("ne.operator")) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                if (offlinePlayer.isOnline()) {
                    Player target = offlinePlayer.getPlayer();
                    player.hidePlayer(EssentialsPlugin.getPlugin(), target);
                }
            }
        }
    }

    @EventHandler
    public void onPreProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String commandMessage = event.getMessage().replaceFirst("/", "");
        String[] commandParts = event.getMessage().split(" ");

        String command = commandParts[0].replaceFirst("/", "").toLowerCase();

        if (!player.hasPermission("ne.cooldown.bypass")) {
            if (getConfigg().getWarmupCommands().contains(command)) {
                if (getConfigg().getRequiredArgLength(command) <= (commandParts.length - 1)) {
                    HashMap<UUID, String> warmingUp = getPlayerData().getCommandsWarmingUpMap();

                    if (warmingUp.containsKey(player.getUniqueId())) {
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You are already warming a command up!"));
                        event.setCancelled(true);
                    } else {
                        warmingUp.put(player.getUniqueId(), commandMessage);
                        player.sendMessage(getMessages().getChatMessage(GRAY + "You must wait " + GOLD + 3 + " seconds" + GRAY + " for command "
                                + DARK_AQUA + "/" + command + GRAY + " to warm up."));
                        Bukkit.getScheduler().scheduleSyncDelayedTask(EssentialsPlugin.getPlugin(), () -> {
                            if (warmingUp.containsKey(player.getUniqueId())) {
                                if (warmingUp.get(player.getUniqueId()).equalsIgnoreCase(commandMessage)) {
                                    warmingUp.remove(player.getUniqueId());
                                    Bukkit.dispatchCommand(player, commandMessage);
                                }
                            }
                        }, 60L);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.hasPermission("ne.back")) {
            EssentialsPlugin.getPlayerData().getPreviousLocationMap().put(player.getUniqueId(), event.getEntity().getLocation());
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        World world = Bukkit.getWorld("world");
        if (world != null) {
            PublicLocationModel publicLocationModel = CorePlugin.getPublicLocationRegistry().getPublicLocations();

            if (publicLocationModel.hasSpawn(world) && event.getPlayer().getBedSpawnLocation() == null) {
                event.setRespawnLocation(publicLocationModel.getSpawn(world));
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("ne.back")) {
            switch (event.getCause()) {
                case COMMAND, PLUGIN, SPECTATE, UNKNOWN -> {
                    EssentialsPlugin.getPlayerData().getPreviousLocationMap().put(player.getUniqueId(), event.getFrom());
                }
                default -> {}
            }
        }
    }
}