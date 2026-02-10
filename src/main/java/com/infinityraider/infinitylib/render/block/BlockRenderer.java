package com.infinityraider.infinitylib.render.block;

import com.infinityraider.infinitylib.block.BlockBase;
import com.infinityraider.infinitylib.block.ICustomRenderedBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.*;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class BlockRenderer<B extends BlockBase & ICustomRenderedBlock> implements IModel {

    private final B block;
    private final IBlockRenderingHandler<B> renderer;

    public BlockRenderer(IBlockRenderingHandler<B> renderer) {
        this.block = renderer.getBlock();
        this.renderer = renderer;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return renderer.getAllTextures();
    }

    public B getBlock() {
        return block;
    }

    public IBlockRenderingHandler<B> getRenderer() {
        return renderer;
    }

    @Override
    public BakedInfBlockModel<B> bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return new BakedInfBlockModel<>(block, format, renderer, bakedTextureGetter, renderer.doInventoryRendering());
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }

}
