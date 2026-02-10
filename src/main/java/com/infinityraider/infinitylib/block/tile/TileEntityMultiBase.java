package com.infinityraider.infinitylib.block.tile;

import com.infinityraider.infinitylib.block.multiblock.IMultiBlockComponent;
import com.infinityraider.infinitylib.block.multiblock.IMultiBlockManager;
import com.infinityraider.infinitylib.block.multiblock.IMultiBlockPartData;
import com.infinityraider.infinitylib.reference.Names;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileEntityMultiBase<M extends IMultiBlockManager<D>, D extends IMultiBlockPartData> extends TileEntityBase implements IMultiBlockComponent<M, D> {
    protected final void writeTileNBT(NBTTagCompound tag) {
        if (tag.hasKey(Names.NBT.MULTI)) {
            NBTTagCompound multiBlockTag = tag.getCompoundTag(Names.NBT.MULTI);
            ((IMultiBlockComponent) this).getMultiBlockData().readFromNBT(multiBlockTag);
        }
        this.writeMultiTileNBT(tag);
    }

    protected final void readTileNBT(NBTTagCompound tag) {
        if (tag.hasKey(Names.NBT.MULTI)) {
            NBTTagCompound multiBlockTag = tag.getCompoundTag(Names.NBT.MULTI);
            ((IMultiBlockComponent) this).getMultiBlockData().readFromNBT(multiBlockTag);
        }
        this.readMultiTileNBT(tag);
    }

    protected abstract void writeMultiTileNBT(NBTTagCompound tag);

    protected abstract void readMultiTileNBT(NBTTagCompound tag);
}
