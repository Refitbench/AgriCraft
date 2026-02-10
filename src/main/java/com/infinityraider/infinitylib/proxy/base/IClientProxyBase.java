package com.infinityraider.infinitylib.proxy.base;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public interface IClientProxyBase extends IProxyBase {
    @Override
    default EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }

    @Override
    default World getClientWorld() {
        return Minecraft.getMinecraft().world;
    }

    @Override
    default World getWorldByDimensionId(int dimension) {
        Side effectiveSide = FMLCommonHandler.instance().getEffectiveSide();
        if(effectiveSide == Side.SERVER) {
            return FMLClientHandler.instance().getServer().getWorld(dimension);
        } else {
            return getClientWorld();
        }
    }

    @Override
    default Side getPhysicalSide() {
        return Side.CLIENT;
    }

    @Override
    default Side getEffectiveSide() {
        return FMLCommonHandler.instance().getEffectiveSide();
    }

    @Override
    default void queueTask(Runnable task) {
        if(getEffectiveSide() == Side.CLIENT) {
            Minecraft.getMinecraft().addScheduledTask(task);
        } else {
            FMLClientHandler.instance().getServer().addScheduledTask(task);
        }
    }
}
