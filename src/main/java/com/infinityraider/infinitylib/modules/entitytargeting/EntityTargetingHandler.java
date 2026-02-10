package com.infinityraider.infinitylib.modules.entitytargeting;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityTargetingHandler {
    private static final EntityTargetingHandler INSTANCE = new EntityTargetingHandler();

    static EntityTargetingHandler getInstance() {
        return INSTANCE;
    }

    private Map<Class<? extends EntityCreature>, List<Class<? extends Entity>>> targetMap;

    private EntityTargetingHandler() {
        this.targetMap = new HashMap<>();
    }

    void registerEntityTargetingRule(Class<? extends Entity> target, Class<? extends EntityCreature> aggressor) {
        if(!this.targetMap.containsKey(aggressor)) {
            this.targetMap.put(aggressor, new ArrayList<>());
        }
        this.targetMap.get(aggressor).add(target);
    }

    @SubscribeEvent
    @SuppressWarnings({"unused", "unchecked"})
    public void onZombieSpawn(LivingSpawnEvent event) {
        if(!(event.getEntity() instanceof EntityCreature)) {
            return;
        }
        EntityCreature entity = (EntityCreature) event.getEntity();
        //if i replace this with a stream it stops working for some reason >>
        for(Map.Entry<Class<? extends EntityCreature>, List<Class<? extends Entity>>> entry : this.targetMap.entrySet()) {
            if (entry.getKey().isAssignableFrom(entity.getClass())) {
                for (Class<? extends Entity> entityClass : entry.getValue()) {
                    entity.targetTasks.addTask(2, new EntityAINearestAttackableTarget(entity, entityClass, true));
                }
            }
        }
    }
}
