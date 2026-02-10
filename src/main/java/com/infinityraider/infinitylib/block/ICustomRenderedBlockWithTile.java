package com.infinityraider.infinitylib.block;

import com.infinityraider.infinitylib.block.tile.TileEntityBase;
import com.infinityraider.infinitylib.render.block.ITileRenderingHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Implemented in a Block class to have special rendering handling for the block
 * @param <T> TileEntity class for this block, can be simple TileEntity if this block doesn't have a tile entity
 */
public interface ICustomRenderedBlockWithTile<T extends TileEntityBase> extends ICustomRenderedBlock {
    /**
     * Gets called to create the IBlockRenderingHandler instance to render this block with
     * @return a new IBlockRenderingHandler object for this block
     */
    @Override
    @SideOnly(Side.CLIENT)
    ITileRenderingHandler<? extends BlockBase, T> getRenderer();
}
