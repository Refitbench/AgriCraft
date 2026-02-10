package com.infinityraider.infinitylib.modules.entitylistener;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;

public class EntityJoinOrLeaveWorldHandler implements IWorldEventListener {
    private static final EntityJoinOrLeaveWorldHandler INSTANCE = new EntityJoinOrLeaveWorldHandler();

    static EntityJoinOrLeaveWorldHandler getInstance() {
        return INSTANCE;
    }

    private final List<IEntityLeaveOrJoinWorldListener> listeners;

    private EntityJoinOrLeaveWorldHandler() {
        this.listeners = Lists.newArrayList();
    }

    public void registerListener(IEntityLeaveOrJoinWorldListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void onEntityAdded(Entity entity) {
        this.listeners.stream().forEach(listener -> listener.onEntityJoinWorld(entity));
    }

    @Override
    public void onEntityRemoved(Entity entity) {
        this.listeners.stream().forEach(listener -> listener.onEntityLeaveWorld(entity));
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onWorldLoad(WorldEvent.Load event) {
        event.getWorld().addEventListener(this);
    }

    @Override
    public void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags) {}

    @Override
    public void notifyLightSet(BlockPos pos) {}

    @Override
    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {}

    @Override
    public void playSoundToAllNearExcept(@Nullable EntityPlayer player, SoundEvent soundIn, SoundCategory category,
                                         double x, double y, double z, float volume, float pitch) {}
    @Override
    public void playRecord(SoundEvent soundIn, BlockPos pos) {}

    @Override
    public void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord,
                              double xSpeed, double ySpeed, double zSpeed, int... parameters) {}

    @Override
    public void spawnParticle(int id, boolean ignoreRange, boolean p_190570_3_, double x, double y, double z,
                              double xSpeed, double ySpeed, double zSpeed, int... parameters) {}

    @Override
    public void broadcastSound(int soundID, BlockPos pos, int data) {}

    @Override
    public void playEvent(EntityPlayer player, int type, BlockPos blockPosIn, int data) {}

    @Override
    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {}
}
