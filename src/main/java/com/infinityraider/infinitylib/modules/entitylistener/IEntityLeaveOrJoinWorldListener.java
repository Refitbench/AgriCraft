package com.infinityraider.infinitylib.modules.entitylistener;

import net.minecraft.entity.Entity;

public interface
IEntityLeaveOrJoinWorldListener {
    void onEntityJoinWorld(Entity entity);

    void onEntityLeaveWorld(Entity entity);
}
