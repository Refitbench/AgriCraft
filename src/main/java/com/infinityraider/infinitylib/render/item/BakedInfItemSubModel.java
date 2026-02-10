/*
 */
package com.infinityraider.infinitylib.render.item;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import javax.vecmath.Matrix4f;

import com.infinityraider.infinitylib.render.tessellation.TessellatorBakedQuad;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author RlonRyan
 */
@SideOnly(Side.CLIENT)
public class BakedInfItemSubModel implements IBakedModel {

    @Nonnull
    private final BakedInfItemModel parent;
    @Nonnull
    private final ImmutableList<BakedQuad>[] faceQuads;

    @Nonnull
    private final ItemStack stack;
    @Nullable
    private final World world;
    @Nullable
    private final EntityLivingBase entity;

    public BakedInfItemSubModel(
            @Nonnull BakedInfItemModel parent,
            @Nonnull ItemStack stack,
            @Nullable World world,
            @Nullable EntityLivingBase entity
    ) {
        // Validate and save parameters.
        this.parent = Preconditions.checkNotNull(parent);
        this.stack = Preconditions.checkNotNull(stack);
        this.world = world;
        this.entity = entity;

        // Create sided quad cache.
        this.faceQuads = new ImmutableList[7];
    }

    @Override
    public ImmutableList<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        // Get the corresponding side index.
        final int index = (side == null) ? 6 : side.ordinal();

        // Attempt to fetch cached quads.
        ImmutableList<BakedQuad> quads = this.faceQuads[index];

        // If no cached quads exist, create them.
        if (quads == null) {
            // Get the instance of the tessellator to use.
            final TessellatorBakedQuad tessellator = TessellatorBakedQuad.getInstance();

            // Setup the tessellator.
            tessellator.setCurrentFace(side);
            tessellator.setTextureFunction(this.parent.textureFunction);
            tessellator.startDrawingQuads(this.parent.format);

            // Have the renderer render the item using given tesselator.
            this.parent.renderer.renderItem(tessellator, world, stack, entity);

            // Get the quads from the tesselator.
            quads = tessellator.getQuads();

            // Flush the tessellator.
            tessellator.draw();

            // Update the cache.
            this.faceQuads[index] = quads;
        }

        // Return the quad list.
        return quads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return this.parent.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.parent.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return this.parent.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.parent.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return this.parent.getItemCameraTransforms();
    }

    @Override
    @Nonnull
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }

    @Override
    @Nonnull
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType transform) {
        return Pair.of(this, this.parent.transformer.apply(transform));
    }
}
