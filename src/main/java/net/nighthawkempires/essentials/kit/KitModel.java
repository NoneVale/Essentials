package net.nighthawkempires.essentials.kit;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import net.nighthawkempires.core.datasection.DataSection;
import net.nighthawkempires.core.datasection.Model;
import net.nighthawkempires.essentials.EssentialsPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.nighthawkempires.essentials.EssentialsPlugin.*;

public class KitModel implements Model {

    private String key;

    private long cooldown;

    private List<ItemStack> items;

    private Map<UUID, Long> activeCooldowns;

    public KitModel(String key) {
        this.key = key;
        this.items = Lists.newArrayList();

        this.cooldown = 0L;

        this.activeCooldowns = Maps.newHashMap();
    }

    public KitModel(String key, DataSection data) {
        this.key = key;

        this.items = Lists.newArrayList();
        for (String s : data.getStringList("items")) {
            try {
                this.items.add(itemFrom64(s));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        this.cooldown = data.getLong("cooldown");

        this.activeCooldowns = Maps.newHashMap();
        if (data.isSet("active-cooldowns")) {
            DataSection cooldowns = data.getSectionNullable("active-cooldowns");
            for (String s : cooldowns.keySet()) {
                this.activeCooldowns.put(UUID.fromString(s), cooldowns.getLong(s));
            }
        }
    }
    
    public ImmutableList<ItemStack> getItems() {
        return ImmutableList.copyOf(this.items);
    }
    
    public void setItems(PlayerInventory playerInventory) {
        this.items = Lists.newArrayList();

        for (ItemStack itemStack : playerInventory.getContents()) {
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                this.items.add(itemStack);
            }
        }

        getKitRegistry().register(this);
    }

    public long getCooldown() {
        return this.cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;

        getKitRegistry().register(this);
    }

    public String getKey() {
        return this.key;
    }

    public boolean canUse(UUID uuid) {
        if (!activeCooldowns.containsKey(uuid)) return true;
        else {
            if (activeCooldowns.get(uuid) < System.currentTimeMillis()) {
                activeCooldowns.remove(uuid);
                getKitRegistry().register(this);
                return true;
            }
        }
        return false;
    }

    public String timeLeft(UUID uuid) {
        if (activeCooldowns.containsKey(uuid)) {
            long difference = activeCooldowns.get(uuid) - System.currentTimeMillis();

            int seconds = (int) (difference / 1000) % 60 ;
            int minutes = (int) ((difference / (1000*60)) % 60);
            int hours   = (int) ((difference / (1000*60*60)) % 24);
            int days = (int) (difference / (1000*60*60*24));

            StringBuilder timeLeft = new StringBuilder();

            if (days > 0) {
                timeLeft.append(days).append(" days, ");
            }

            if (hours > 0) {
                timeLeft.append(hours).append(" hours, ");
            }

            if (minutes > 0) {
                timeLeft.append(minutes).append(" minutes, ");
            }

            if (seconds > 0) {
                timeLeft.append(seconds).append(" seconds");
            }

            String time = timeLeft.toString().trim();
            if (time.endsWith(","))
                time = time.substring(0, time.length() - 1);

            return time;
        }
        return "";
    }

    public void addCooldown(UUID uuid) {
        activeCooldowns.put(uuid, System.currentTimeMillis() + getCooldown());
        getKitRegistry().register(this);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = Maps.newHashMap();

        List<String> itemList = Lists.newArrayList();
        for (ItemStack itemStack : items) {
            if (itemStack.getType() != Material.AIR && itemStack.getType() != Material.CAVE_AIR
                    && itemStack.getType() != Material.VOID_AIR && itemStack.getType() != null
                    && itemStack != null) {
                itemList.add(itemTo64(itemStack));
            }
        }
        /*List<Map<String, Object>> itemList = Lists.newArrayList();
        for (ItemStack itemStack : items) {
            if (!itemStack.getType().isAir()) {
                itemList.add(itemStack.serialize());
            }
        }*/
        map.put("items", itemList);

        map.put("cooldown", cooldown);

        Map<String, Long> activeCooldownMap = Maps.newHashMap();
        for (UUID uuid : activeCooldowns.keySet()) {
            if (activeCooldowns.get(uuid) > System.currentTimeMillis()) {
                activeCooldownMap.put(uuid.toString(), activeCooldowns.get(uuid));
            }
        }
        map.put("active-cooldowns", activeCooldownMap);

        return map;
    }

    private String itemTo64(ItemStack stack) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(stack);

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        }
        catch (Exception e) {
            throw new IllegalStateException("Unable to save item stack.", e);
        }
    }

    private ItemStack itemFrom64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            try {
                return (ItemStack) dataInput.readObject();
            } finally {
                dataInput.close();
            }
        }
        catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}