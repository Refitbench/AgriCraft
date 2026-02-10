/*
 */
package com.infinityraider.infinitylib.render.item;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.function.Function;
import javax.vecmath.Matrix4f;

import com.infinityraider.infinitylib.render.DefaultTransforms;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

@SideOnly(Side.CLIENT)
public class BakedInfItemModel implements IBakedModel, IItemOverriden {

    @Nonnull
    protected final VertexFormat format;
    @Nonnull
    protected final IItemRenderingHandler renderer;
    @Nonnull
    protected final Function<ResourceLocation, TextureAtlasSprite> textureFunction;

    @Nonnull
    protected final DefaultTransforms.Transformer transformer;

    @Nonnull
    private final ItemOverrideList overrides;
    @Nonnull
    private final Map<Object, BakedInfItemSubModel> cache;

    public BakedInfItemModel(
            @Nonnull VertexFormat format,
            @Nonnull IItemRenderingHandler renderer,
            @Nonnull Function<ResourceLocation, TextureAtlasSprite> textures
    ) {
        // Validate and save parameters.
        this.format = Preconditions.checkNotNull(format);
        this.renderer = Preconditions.checkNotNull(renderer);
        this.textureFunction = Preconditions.checkNotNull(textures);

        // Save the transformer.
        this.transformer = Preconditions.checkNotNull(renderer.getPerspectiveTransformer());

        // Create cache and override wrapper instance.
        this.overrides = new IItemOverriden.Wrapper(this);
        this.cache = new HashMap<>();
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        return ImmutableList.of();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType transform) {
        return Pair.of(this, this.transformer.apply(transform));
    }

    @Override
    public final BakedInfItemSubModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
        return this.cache.computeIfAbsent(
                // Get the respective cache key for the item model.
                this.renderer.getItemQuadsCacheKey(world, stack, entity),
                // Compute a new submodel, if one does not already exist for the given cache key.
                k -> new BakedInfItemSubModel(this, stack, world, entity)
        );
    }

    @Override
    public final ItemOverrideList getOverrides() {
        return this.overrides;
    }

}
