package com.infinityraider.infinitylib.render.block;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.infinityraider.infinitylib.block.BlockBase;
import com.infinityraider.infinitylib.block.ICustomRenderedBlock;
import com.infinityraider.infinitylib.block.ICustomRenderedBlockWithTile;
import com.infinityraider.infinitylib.block.tile.TileEntityBase;
import com.infinityraider.infinitylib.render.tile.TesrWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class BlockRendererRegistry implements ICustomModelLoader {

    private static final BlockRendererRegistry INSTANCE = new BlockRendererRegistry();

    public static BlockRendererRegistry getInstance() {
        return INSTANCE;
    }

    private final Map<ResourceLocation, IModel> renderers;
    private final List<ICustomRenderedBlock> blocks;

    private BlockRendererRegistry() {
        this.renderers = new HashMap<>();
        this.blocks = new ArrayList<>();
        ModelLoaderRegistry.registerLoader(this);
    }

    @Override
    public boolean accepts(ResourceLocation loc) {
        return renderers.containsKey(loc);
    }

    @Override
    public IModel loadModel(ResourceLocation loc) throws Exception {
        return renderers.get(loc);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
    }

    public List<ICustomRenderedBlock> getRegisteredBlocks() {
        return ImmutableList.copyOf(blocks);
    }

    @SideOnly(Side.CLIENT)
    public void registerCustomBlockRenderer(ICustomRenderedBlock customRenderedBlock) {
        if (customRenderedBlock == null || !(customRenderedBlock instanceof BlockBase)) {
            return;
        }
        //set custom state mapper
        StateMapperBase stateMapper = new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return customRenderedBlock.getBlockModelResourceLocation();
            }
        };
        ModelLoader.setCustomStateMapper((BlockBase) customRenderedBlock, stateMapper);
        //register renderers
        if (customRenderedBlock instanceof ICustomRenderedBlockWithTile) {
            registerTileRenderer((ICustomRenderedBlockWithTile) customRenderedBlock);
        } else {
            registerBlockRenderer(customRenderedBlock);
        }
    }

    @SuppressWarnings("unchecked")
    private void registerBlockRenderer(ICustomRenderedBlock customRenderedBlock) {
        IBlockRenderingHandler renderer = customRenderedBlock.getRenderer();
        if (renderer != null) {
            BlockRenderer instance = new BlockRenderer(renderer);
            ModelResourceLocation blockModel = customRenderedBlock.getBlockModelResourceLocation();
            //static rendering
            renderers.put(blockModel, instance);
            //inventory rendering
            registerInventoryRendering(renderer, blockModel, instance);
            blocks.add(customRenderedBlock);
        }
    }

    @SuppressWarnings("unchecked")
    private void registerTileRenderer(ICustomRenderedBlockWithTile<? extends TileEntityBase> customRenderedBlock) {
        ITileRenderingHandler renderer = customRenderedBlock.getRenderer();
        if (renderer != null) {
            BlockWithTileRenderer instance = new BlockWithTileRenderer(renderer);
            ModelResourceLocation blockModel = customRenderedBlock.getBlockModelResourceLocation();
            //static rendering
            if (renderer.hasStaticRendering()) {
                renderers.put(blockModel, instance);
            }
            //dynamic rendering
            TileEntity tile = renderer.getTileEntity();
            if (renderer.hasDynamicRendering() && tile != null) {
                ClientRegistry.bindTileEntitySpecialRenderer(tile.getClass(), new TesrWrapper<>(instance));
            }
            //inventory rendering
            registerInventoryRendering(renderer, blockModel, instance);
            blocks.add(customRenderedBlock);
        }
    }

    private void registerInventoryRendering(@Nonnull IBlockRenderingHandler renderer, @Nonnull ModelResourceLocation loc, @Nonnull IModel model) {
        // Validate.
        Preconditions.checkNotNull(renderer);
        Preconditions.checkNotNull(loc);
        Preconditions.checkNotNull(model);

        // Do the thing.
        if (renderer.doInventoryRendering()) {
            ModelResourceLocation itemModel = new ModelResourceLocation(loc.getNamespace() + ":" + loc.getPath(), "inventory");
            renderers.put(itemModel, model);
            // Get the item.
            final Item item = Item.getItemFromBlock(renderer.getBlock());
            // Check that Item exists.
            if (item != null) { // HACK because something, I don't know what, but something changed.
                ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(renderer.getBlock()), stack -> itemModel);
            }
        }
    }
}
