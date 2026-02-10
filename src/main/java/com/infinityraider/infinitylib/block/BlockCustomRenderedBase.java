package com.infinityraider.infinitylib.block;

import net.minecraft.block.material.Material;

@SuppressWarnings("unused")
public abstract class BlockCustomRenderedBase extends BlockBase implements ICustomRenderedBlock {
    public BlockCustomRenderedBase(String name, Material blockMaterial) {
        super(name, blockMaterial);
    }
}
