package net.nighthawkempires.essentials.kit.registry;

import com.google.common.collect.ImmutableList;
import net.nighthawkempires.core.datasection.DataSection;
import net.nighthawkempires.core.datasection.Registry;
import net.nighthawkempires.essentials.kit.KitModel;

import java.util.Map;

public interface KitRegistry extends Registry<KitModel> {

    default KitModel fromDataSection(String stringKey, DataSection data) {
        return new KitModel(stringKey, data);
    }

    default KitModel getKit(String name) {
        if (fromKey(name).isPresent()) return fromKey(name).get();

        for (String s : loadAllFromDb().keySet()) {
            if (s.toLowerCase().equals(name.toLowerCase())) return fromKey(s).get();
        }

        return null;
    }

    default KitModel createKit(String name) {
        return register(new KitModel(name));
    }

    default void deleteKit(KitModel kitModel) {
        remove(kitModel);
    }

    default void deleteKit(String name) {
        if (kitExists(name)) deleteKit(getKit(name));
    }

    default ImmutableList<KitModel> getKits() {
        return ImmutableList.copyOf(loadAllFromDb().values());
    }

    @Deprecated
    Map<String, KitModel> getRegisteredData();

    default boolean kitExists(String name) {
        if (fromKey(name).isPresent()) return true;

        for (String s : loadAllFromDb().keySet()) {
            if (s.toLowerCase().equals(name.toLowerCase())) return true;
        }
        return false;
    }
}