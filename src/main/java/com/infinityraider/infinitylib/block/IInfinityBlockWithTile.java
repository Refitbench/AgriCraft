package com.infinityraider.infinitylib.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface IInfinityBlockWithTile<T extends TileEntity> extends IInfinityBlock, ITileEntityProvider {
    @Override
    T createNewTileEntity(World worldIn, int meta);
}
