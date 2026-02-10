package com.infinityraider.infinitylib.block;

import com.infinityraider.infinitylib.render.block.IBlockRenderingHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Implemented in a Block class to have special rendering handling for the block
 */
public interface ICustomRenderedBlock extends IInfinityBlock {
    /**
     * Gets called to create the IBlockRenderingHandler instance to render this
     * block with
     *
     * @return a new IBlockRenderingHandler object for this block
     */
    @SideOnly(Side.CLIENT)
    IBlockRenderingHandler getRenderer();

    /**
     * Gets an array of ResourceLocations used for the model of this block, all
     * block states for this block will use this as key in the model registry
     *
     * @return a unique ModelResourceLocation for this block
     */
    @SideOnly(Side.CLIENT)
    default ModelResourceLocation getBlockModelResourceLocation() {
        return new ModelResourceLocation(this.getRegistryName() + "");
    }
}
