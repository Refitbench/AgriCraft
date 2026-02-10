package com.infinityraider.infinitylib.render.block;

import com.infinityraider.infinitylib.block.BlockBase;
import com.infinityraider.infinitylib.block.ICustomRenderedBlockWithTile;
import com.infinityraider.infinitylib.block.tile.TileEntityBase;

public abstract class RenderBlockWithTileBase<B extends BlockBase & ICustomRenderedBlockWithTile<T>, T extends TileEntityBase> extends RenderBlockBase<B> implements ITileRenderingHandler<B, T> {

    private final T dummy;
    private final boolean statRender;
    private final boolean dynRender;

    protected RenderBlockWithTileBase(B block, T te, boolean inv, boolean statRender, boolean dynRender) {
        super(block, inv);
        this.dummy = te;
        this.statRender = statRender;
        this.dynRender = dynRender;
    }

    @Override
    public T getTileEntity() {
        return dummy;
    }

    @Override
    public boolean hasDynamicRendering() {
        return dynRender;
    }

    @Override
    public boolean hasStaticRendering() {
        return statRender;
    }
}
