package com.infinityraider.infinitylib.modules.entitylistener;

import com.google.common.collect.ImmutableList;
import com.infinityraider.infinitylib.modules.Module;

import java.util.List;

public class ModuleEntityListener extends Module {
    private static final ModuleEntityListener INSTANCE = new ModuleEntityListener();

    public static ModuleEntityListener getInstance() {
        return INSTANCE;
    }

    private final EntityJoinOrLeaveWorldHandler HANDLER;

    private ModuleEntityListener() {
        this.HANDLER = EntityJoinOrLeaveWorldHandler.getInstance();
    }

    public void registerListener(IEntityLeaveOrJoinWorldListener listener) {
        this.HANDLER.registerListener(listener);
    }

    public List<Object> getCommonEventHandlers() {
        return ImmutableList.of(this.HANDLER);
    }
}
