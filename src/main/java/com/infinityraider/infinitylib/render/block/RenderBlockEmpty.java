package com.infinityraider.infinitylib.render.block;

import com.infinityraider.infinitylib.block.BlockBase;
import com.infinityraider.infinitylib.block.ICustomRenderedBlock;
import com.infinityraider.infinitylib.render.tessellation.ITessellator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class RenderBlockEmpty<B extends BlockBase & ICustomRenderedBlock> implements IBlockRenderingHandler<B> {

    public static <B extends BlockBase & ICustomRenderedBlock> RenderBlockEmpty<B> createEmptyRender(B block) {
        return new RenderBlockEmpty<>(block, false);
    }

    public static <B extends BlockBase & ICustomRenderedBlock> RenderBlockEmpty<B> createEmptyRender(B block, boolean inventory) {
        return new RenderBlockEmpty<>(block, inventory);
    }

    private final B block;
    private final boolean inventory;

    protected RenderBlockEmpty(B block, boolean inventory) {
        this.block = block;
        this.inventory = !inventory;
    }

    @Override
    public B getBlock() {
        return block;
    }

    @Override
    public List<ResourceLocation> getAllTextures() {
        return Collections.emptyList();
    }

    @Override
    public void renderWorldBlockStatic(ITessellator tessellator, IBlockState state, B block, EnumFacing side) {
    }

    @Override
    public void renderInventoryBlock(ITessellator tessellator, World world, IBlockState state, B block,
            ItemStack stack, EntityLivingBase entity, ItemCameraTransforms.TransformType type) {
    }

    @Override
    public TextureAtlasSprite getIcon() {
        return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
    }

    @Override
    public boolean applyAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean doInventoryRendering() {
        return inventory;
    }

}
