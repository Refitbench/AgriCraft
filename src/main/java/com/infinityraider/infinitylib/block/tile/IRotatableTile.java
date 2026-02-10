package com.infinityraider.infinitylib.block.tile;

import net.minecraft.util.EnumFacing;

@SuppressWarnings("unused")
public interface IRotatableTile {
    EnumFacing getOrientation();

    void setOrientation(EnumFacing facing);

    void incrementRotation(int amount);
}
