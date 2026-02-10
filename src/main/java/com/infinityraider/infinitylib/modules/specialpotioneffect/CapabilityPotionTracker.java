package com.infinityraider.infinitylib.modules.specialpotioneffect;

import com.infinityraider.infinitylib.capability.ICapabilityImplementation;
import infinityraider.infinitylib.Tags;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilityPotionTracker implements ICapabilityImplementation<EntityLivingBase, PotionTracker> {
    private static final CapabilityPotionTracker INSTANCE = new CapabilityPotionTracker();

    public static CapabilityPotionTracker getInstance() {
        return INSTANCE;
    }

    public static ResourceLocation KEY = new ResourceLocation(Tags.LIBRARY_ID, "potion_tracker");

    @CapabilityInject(value = PotionTracker.class)
    public static Capability<PotionTracker> CAPABILITY_POTION_TRACKER = null;

    private CapabilityPotionTracker() {}

    @Override
    public Capability<PotionTracker> getCapability() {
        return CAPABILITY_POTION_TRACKER;
    }

    @Override
    public boolean shouldApplyCapability(EntityLivingBase carrier) {
        return true;
    }

    @Override
    public PotionTracker createNewValue(EntityLivingBase carrier) {
        return new PotionTracker(carrier);
    }

    @Override
    public ResourceLocation getCapabilityKey() {
        return KEY;
    }

    @Override
    public Class<EntityLivingBase> getCarrierClass() {
        return EntityLivingBase.class;
    }

    @Override
    public Class<PotionTracker> getCapabilityClass() {
        return PotionTracker.class;
    }

    public static PotionTracker getPotionTracker(EntityLivingBase entity) {
        return entity.hasCapability(CAPABILITY_POTION_TRACKER, null) ? entity.getCapability(CAPABILITY_POTION_TRACKER, null) : null;
    }
}
