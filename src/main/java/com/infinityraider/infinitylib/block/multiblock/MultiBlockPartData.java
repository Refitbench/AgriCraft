package com.infinityraider.infinitylib.block.multiblock;

import com.infinityraider.infinitylib.reference.Names;
import net.minecraft.nbt.NBTTagCompound;

public class MultiBlockPartData implements IMultiBlockPartData {
    private int posX;
    private int posY;
    private int posZ;
    private int sizeX;
    private int sizeY;
    private int sizeZ;

    public MultiBlockPartData(int posX, int posY, int posZ, int sizeX, int sizeY, int sizeZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    @Override
    public int posX() {
        return posX;
    }

    @Override
    public int posY() {
        return posY;
    }

    @Override
    public int posZ() {
        return posZ;
    }

    @Override
    public int sizeX() {
        return sizeX;
    }

    @Override
    public int sizeY() {
        return sizeY;
    }

    @Override
    public int sizeZ() {
        return sizeZ;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger(Names.NBT.X1, posX());
        tag.setInteger(Names.NBT.Y1, posY());
        tag.setInteger(Names.NBT.Z1, posZ());
        tag.setInteger(Names.NBT.X2, sizeX());
        tag.setInteger(Names.NBT.Y2, sizeY());
        tag.setInteger(Names.NBT.Z2, sizeZ());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        this.posX = tag.getInteger(Names.NBT.X1);
        this.posY = tag.getInteger(Names.NBT.Y1);
        this.posZ = tag.getInteger(Names.NBT.Z1);
        this.sizeX = tag.getInteger(Names.NBT.X2);
        this.sizeY = tag.getInteger(Names.NBT.Y2);
        this.sizeZ = tag.getInteger(Names.NBT.Z2);
    }

    @Override
    public String toString() {
        return String.format("(x: %1$d / %2$d, y: %3$d / %4$d, z: %5$d / %6$d)", posX() + 1, sizeX(), posY() + 1, sizeY(), posZ() + 1, sizeZ());
    }
}
