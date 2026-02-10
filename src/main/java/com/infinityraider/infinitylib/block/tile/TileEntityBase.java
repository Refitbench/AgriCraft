package com.infinityraider.infinitylib.block.tile;

import com.infinityraider.infinitylib.network.MessageSyncTile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.Random;

@SuppressWarnings("unused")
public abstract class TileEntityBase extends TileEntity {
    public IBlockState getState() {
        return this.getWorld().getBlockState(this.getPos());
    }

    public final int xCoord() {
        return this.getPos().getX();
    }

    public final int yCoord() {
        return this.getPos().getY();
    }

    public final int zCoord() {
        return this.getPos().getZ();
    }

    public Random getRandom() {
        return this.getWorld().rand;
    }

    public boolean isRemote() {
        return this.getWorld().isRemote;
    }

    public Chunk getChunk() {
        return this.getWorld().getChunk(this.getPos());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return tag;
    }

    //read data from packet
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
        this.getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        this.writeTileNBT(tag);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.readTileNBT(tag);
    }

    protected abstract void writeTileNBT(NBTTagCompound tag);

    protected abstract void readTileNBT(NBTTagCompound tag);

    public void markForUpdate() {
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        this.getWorld().notifyBlockUpdate(getPos(), state, state, 3);
        this.markDirty();
    }

    public void syncToClient() {
        this.syncToClient(false);
    }

    public void syncToClient(boolean renderUpdate) {
        if(!this.getWorld().isRemote) {
            new MessageSyncTile(this, renderUpdate).sendToAllAround(this.getWorld(), this.xCoord(), this.yCoord(), this.zCoord(), 128);
        }
    }
}
