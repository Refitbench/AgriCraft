package com.infinityraider.infinitylib.modules.playerstate;

import com.google.common.collect.ImmutableList;
import com.infinityraider.infinitylib.modules.Module;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ModulePlayerState extends Module {
    private static final ModulePlayerState INSTANCE = new ModulePlayerState();
    private static final PlayerStateHandler STATE_HANDLER = PlayerStateHandler.getInstance();

    public static ModulePlayerState getInstance() {
        return INSTANCE;
    }

    public State getState(EntityPlayer player) {
        return STATE_HANDLER.getState(player);
    }

    @Override
    public void registerMessages(INetworkWrapper wrapper) {
        wrapper.registerMessage(MessageSyncState.class);
    }

    @Override
    public List<Object> getCommonEventHandlers() {
        return ImmutableList.of(STATE_HANDLER);
    }

}
