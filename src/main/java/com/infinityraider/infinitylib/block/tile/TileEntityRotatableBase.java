package com.infinityraider.infinitylib.block.tile;

import com.infinityraider.infinitylib.reference.Names;
import javax.annotation.Nonnull;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public abstract class TileEntityRotatableBase extends TileEntityBase implements IRotatableTile {

    @Nonnull
    private EnumFacing direction = EnumFacing.NORTH;

    @Override
    protected final void writeTileNBT(NBTTagCompound tag) {
        tag.setByte(Names.NBT.DIRECTION, (byte) this.direction.getHorizontalIndex());
        this.writeRotatableTileNBT(tag);
    }

    @Override
    protected final void readTileNBT(NBTTagCompound tag) {
        if (tag.hasKey(Names.NBT.DIRECTION)) {
            this.setDirection(tag.getByte(Names.NBT.DIRECTION));
        }
        this.readRotatableTileNBT(tag);
    }

    protected abstract void readRotatableTileNBT(NBTTagCompound tag);

    protected abstract void writeRotatableTileNBT(NBTTagCompound tag);

    @Override
    public final EnumFacing getOrientation() {
        return this.direction;
    }

    @Override
    public final void setOrientation(EnumFacing facing) {
        this.direction = (facing != null && facing.getAxis().isHorizontal()) ? facing : this.direction;
    }

    @Override
    public final void incrementRotation(int amount) {
        this.setDirection(this.getOrientation().getHorizontalIndex() + amount);
    }

    // Notice, the ordinal follows the backwards orientation.
    private void setDirection(int cardinal) {
        // EnumFacing can actually handle all this stuff!
        this.setOrientation(EnumFacing.byIndex(cardinal));
    }
}
