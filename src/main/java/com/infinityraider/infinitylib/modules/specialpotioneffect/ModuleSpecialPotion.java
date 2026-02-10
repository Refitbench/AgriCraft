package com.infinityraider.infinitylib.modules.specialpotioneffect;

import com.google.common.collect.ImmutableList;
import com.infinityraider.infinitylib.capability.ICapabilityImplementation;
import com.infinityraider.infinitylib.modules.Module;
import com.infinityraider.infinitylib.network.INetworkWrapper;

import java.util.List;

public class ModuleSpecialPotion extends Module {
    private static final ModuleSpecialPotion INSTANCE = new ModuleSpecialPotion();

    public static final CapabilityPotionTracker CAPABILITY = CapabilityPotionTracker.getInstance();
    public static final PotionEffectHandler HANDLER = PotionEffectHandler.getInstance();

    public static ModuleSpecialPotion getInstance() {
        return INSTANCE;
    }

    @Override
    public void registerMessages(INetworkWrapper wrapper) {
        wrapper.registerMessage(MessageSyncPotions.class);
    }

    @Override
    public List<Object> getCommonEventHandlers() {
        return ImmutableList.of(HANDLER);
    }

    @Override
    public List<ICapabilityImplementation> getCapabilities() {
        return ImmutableList.of(CAPABILITY);
    }
}
