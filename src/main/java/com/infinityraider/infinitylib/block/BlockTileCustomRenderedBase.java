package com.infinityraider.infinitylib.block;

import com.infinityraider.infinitylib.block.tile.TileEntityBase;
import net.minecraft.block.material.Material;

@SuppressWarnings("unused")
public abstract class BlockTileCustomRenderedBase<T extends TileEntityBase> extends BlockBaseTile<T> implements ICustomRenderedBlockWithTile<T> {

	public BlockTileCustomRenderedBase(String name, Material blockMaterial) {
		super(name, blockMaterial);
	}

}
