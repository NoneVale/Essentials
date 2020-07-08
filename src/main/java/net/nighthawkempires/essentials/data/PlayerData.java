package net.nighthawkempires.essentials.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerData {

    private HashMap<UUID, Location> previousLocationMap;
    private HashMap<UUID, UUID> replyMap;
    private HashMap<UUID, List<UUID>> teleportRequestsMap;
    private HashMap<UUID, String> commandsWarmingUpMap;
    private HashMap<UUID, String> commandsWarmedUpMap;

    private List<UUID> commandWarmupList;
    private List<UUID> flyList;
    private List<UUID> godmodeList;
    private List<UUID> vanishList;

    public PlayerData() {
        this.previousLocationMap = Maps.newHashMap();
        this.replyMap = Maps.newHashMap();
        this.teleportRequestsMap = Maps.newHashMap();
        this.commandsWarmingUpMap = Maps.newHashMap();
        this.commandsWarmedUpMap = Maps.newHashMap();

        this.commandWarmupList = Lists.newArrayList();
        this.flyList = Lists.newArrayList();
        this.godmodeList = Lists.newArrayList();
        this.vanishList = Lists.newArrayList();
    }

    public HashMap<UUID, Location> getPreviousLocationMap() {
        return previousLocationMap;
    }

    public HashMap<UUID, UUID> getReplyMap() {
        return replyMap;
    }

    public HashMap<UUID, List<UUID>> getTeleportRequestsMap() {
        return teleportRequestsMap;
    }

    public HashMap<UUID, String> getCommandsWarmingUpMap() {
        return commandsWarmingUpMap;
    }

    public HashMap<UUID, String> getCommandsWarmedUpMap() {
        return commandsWarmedUpMap;
    }

    public List<UUID> getCommandWarmupList() {
        return commandWarmupList;
    }

    public List<UUID> getFlyList() {
        return flyList;
    }

    public List<UUID> getGodmodeList() {
        return godmodeList;
    }

    public List<UUID> getVanishList() {
        return vanishList;
    }
}
