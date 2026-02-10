package com.infinityraider.infinitylib.item;

import com.infinityraider.infinitylib.render.item.IItemRenderingHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public interface ICustomRenderedItem extends IInfinityItem {
    /**
     * Gets called to create the IBlockRenderingHandler instance to render this block with
     * @return a new IItemRenderingHandler object for this block
     */
    @SideOnly(Side.CLIENT)
    IItemRenderingHandler getRenderer();
}
