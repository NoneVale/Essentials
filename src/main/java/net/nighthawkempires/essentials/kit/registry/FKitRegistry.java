package net.nighthawkempires.essentials.kit.registry;

import net.nighthawkempires.core.datasection.AbstractFileRegistry;
import net.nighthawkempires.essentials.kit.KitModel;

import java.util.Map;

public class FKitRegistry extends AbstractFileRegistry<KitModel> implements KitRegistry {
    private static final boolean SAVE_PRETTY = true;

    public FKitRegistry() {
        super("empires/kits", SAVE_PRETTY, -1);
    }

    @Override
    public Map<String, KitModel> getRegisteredData() {
        return REGISTERED_DATA.asMap();
    }
}
