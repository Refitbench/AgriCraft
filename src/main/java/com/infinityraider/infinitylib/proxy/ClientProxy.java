package com.infinityraider.infinitylib.proxy;

import com.infinityraider.infinitylib.InfinityMod;
import com.infinityraider.infinitylib.block.ICustomRenderedBlock;
import com.infinityraider.infinitylib.block.IInfinityBlock;
import com.infinityraider.infinitylib.config.InfinityConfigurationHandler;
import com.infinityraider.infinitylib.entity.EntityRegistryEntry;
import com.infinityraider.infinitylib.item.IAutoRenderedItem;
import com.infinityraider.infinitylib.item.ICustomRenderedItem;
import com.infinityraider.infinitylib.item.IInfinityItem;
import com.infinityraider.infinitylib.item.IItemWithModel;
import com.infinityraider.infinitylib.modules.Module;
import com.infinityraider.infinitylib.proxy.base.IClientProxyBase;
import com.infinityraider.infinitylib.render.block.BlockRendererRegistry;
import com.infinityraider.infinitylib.render.item.ItemRendererRegistry;
import com.infinityraider.infinitylib.sound.SoundDelegateClient;
import com.infinityraider.infinitylib.utility.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public class ClientProxy implements IProxy, IClientProxyBase {

    @Override
    public void initEnd(FMLInitializationEvent event) {
        IProxy.super.initEnd(event);
        Module.getActiveModules().forEach(Module::initClient);
    }

    @Override
    public void postInitEnd(FMLPostInitializationEvent event) {
        IProxy.super.postInitEnd(event);
        Module.getActiveModules().forEach(Module::postInitClient);
    }

    @Override
    public void initModConfiguration(InfinityConfigurationHandler handler) {
        IProxy.super.initModConfiguration(handler);
        handler.initializeConfigurationClient();
    }

    @Override
    public void registerBlocks(InfinityMod mod, IForgeRegistry<Block> registry) {
        //blocks
        IProxy.super.registerBlocks(mod, registry);
        //renderers
        if (mod.getModBlockRegistry() != null) {
            ReflectionHelper.forEachValueIn(mod.getModBlockRegistry(), IInfinityBlock.class, (IInfinityBlock block) -> {
                if (block.isEnabled() && (block instanceof ICustomRenderedBlock)) {
                    BlockRendererRegistry.getInstance().registerCustomBlockRenderer((ICustomRenderedBlock) block);
                }
            });
        }
        for (ICustomRenderedBlock block : BlockRendererRegistry.getInstance().getRegisteredBlocks()) {
            mod.getLogger().debug("registered custom renderer for " + block.getBlockModelResourceLocation());
        }
    }

    @Override
    public SoundDelegateClient getSoundDelegate() {
        return new SoundDelegateClient(Minecraft.getMinecraft().getSoundHandler());
    }

    public void registerItems(InfinityMod mod, IForgeRegistry<Item> registry) {
        //items
        IProxy.super.registerItems(mod, registry);
        //renderers
        if (mod.getModItemRegistry() != null) {
            ReflectionHelper.forEachValueIn(mod.getModItemRegistry(), IInfinityItem.class, (IInfinityItem item) -> {
                if ((item instanceof Item) && item.isEnabled()) {
                    if (item instanceof IItemWithModel) {
                        for (Tuple<Integer, ModelResourceLocation> entry : ((IItemWithModel) item).getModelDefinitions()) {
                            ModelLoader.setCustomModelResourceLocation((Item) item, entry.getFirst(), entry.getSecond());
                        }
                    }
                    if (item instanceof IAutoRenderedItem) {
                        ItemRendererRegistry.getInstance().registerCustomItemRendererAuto((Item & IAutoRenderedItem) item);
                    } else if (item instanceof ICustomRenderedItem) {
                        ItemRendererRegistry.getInstance().registerCustomItemRenderer((Item) item, ((ICustomRenderedItem) item).getRenderer());
                    }
                }
            });
        }
        // ItemBlock Renderers
        if (mod.getModBlockRegistry() != null) {
            ReflectionHelper.forEachValueIn(mod.getModBlockRegistry(), IInfinityBlock.class, (IInfinityBlock block) -> {
                if (block.isEnabled()) {
                    block.getItemBlock().ifPresent(item -> {
                        if (item instanceof IItemWithModel) {
                            for (Tuple<Integer, ModelResourceLocation> entry : ((IItemWithModel) item).getModelDefinitions()) {
                                mod.getLogger().debug("Registering model for ItemBlock: {0} meta: {1} location: {2}", item.getInternalName(), entry.getFirst(), entry.getSecond());
                                ModelLoader.setCustomModelResourceLocation(item, entry.getFirst(), entry.getSecond());
                            }
                        }
                        if (item instanceof IAutoRenderedItem) {
                            ItemRendererRegistry.getInstance().registerCustomItemRendererAuto((Item & IAutoRenderedItem) item);
                        } else if (item instanceof ICustomRenderedItem) {
                            ItemRendererRegistry.getInstance().registerCustomItemRenderer(item, ((ICustomRenderedItem) item).getRenderer());
                        }
                    });
                }
            });
        }
    }

    @Override
    public void registerEntities(InfinityMod mod, IForgeRegistry<EntityEntry> registry) {
        if (mod.getModEntityRegistry() != null) {
            ReflectionHelper.forEachValueIn(mod.getModEntityRegistry(), EntityRegistryEntry.class, (EntityRegistryEntry entry) -> {
                if (entry.isEnabled()) {
                    entry.registerClient(mod, registry);
                }
            });
        }
    }

    @Override
    public void registerEventHandlers() {
        IProxy.super.registerEventHandlers();
        for (Module module : Module.getActiveModules()) {
            module.getClientEventHandlers().forEach(this::registerEventHandler);
        }
    }
}
