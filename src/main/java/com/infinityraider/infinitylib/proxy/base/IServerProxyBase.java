package com.infinityraider.infinitylib.proxy.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.server.FMLServerHandler;

public interface IServerProxyBase extends IProxyBase {
    @Override
    default EntityPlayer getClientPlayer() {
        return null;
    }

    @Override
    default World getClientWorld() {
        return null;
    }

    @Override
    default World getWorldByDimensionId(int dimension) {
        return FMLServerHandler.instance().getServer().getWorld(dimension);
    }

    @Override
    default Side getPhysicalSide() {
        return Side.SERVER;
    }

    @Override
    default Side getEffectiveSide() {
        return getPhysicalSide();
    }

    @Override
    default void queueTask(Runnable task) {
        FMLServerHandler.instance().getServer().addScheduledTask(task);
    }
}
