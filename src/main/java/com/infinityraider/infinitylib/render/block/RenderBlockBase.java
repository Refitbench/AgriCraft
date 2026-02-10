package com.infinityraider.infinitylib.render.block;

import com.infinityraider.infinitylib.block.BlockBase;
import com.infinityraider.infinitylib.block.ICustomRenderedBlock;
import com.infinityraider.infinitylib.render.RenderUtilBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class RenderBlockBase<B extends BlockBase & ICustomRenderedBlock> extends RenderUtilBase implements IBlockRenderingHandler<B> {
    private final B block;
    private final boolean inv;

    protected RenderBlockBase(B block, boolean inv) {
        this.block = block;
        this.inv = inv;
    }

    @Override
    public B getBlock() {
        return block;
    }

    @Override
    public boolean doInventoryRendering() {
        return inv;
    }
}
