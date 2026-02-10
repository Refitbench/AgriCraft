package com.infinityraider.infinitylib.proxy;

import com.infinityraider.infinitylib.InfinityMod;
import com.infinityraider.infinitylib.block.IInfinityBlock;
import com.infinityraider.infinitylib.block.IInfinityBlockWithTile;
import com.infinityraider.infinitylib.config.InfinityConfigurationHandler;
import com.infinityraider.infinitylib.entity.EntityRegistryEntry;
import com.infinityraider.infinitylib.item.IInfinityItem;
import com.infinityraider.infinitylib.modules.Module;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import com.infinityraider.infinitylib.sound.SidedSoundDelegate;
import com.infinityraider.infinitylib.sound.SoundDelegateServer;
import com.infinityraider.infinitylib.utility.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import com.infinityraider.infinitylib.utility.IRecipeRegisterer;

public interface IProxy extends IProxyBase {
    default void registerRegistries(InfinityMod mod, RegistryEvent.NewRegistry registrar) {}

    default void registerBlocks(InfinityMod mod, IForgeRegistry<Block> registry) {
        // If the mod block registry is missing, skip.
        if (mod.getModBlockRegistry() != null) {
            // Register Blocks
            ReflectionHelper.forEachValueIn(mod.getModBlockRegistry(), IInfinityBlock.class, (IInfinityBlock block) -> {
                if ((block instanceof Block) && block.isEnabled()) {
                    mod.getLogger().debug("Registering Block: " + block.getInternalName());
                    String unlocalized = mod.getModId().toLowerCase() + ':' + block.getInternalName();
                    ((Block) block).setTranslationKey(unlocalized);
                    register(mod, registry, (Block) block, block.getInternalName());
                    for (String tag : block.getOreTags()) {
                        OreDictionary.registerOre(tag, (Block) block);
                    }
                }
            });
            // Register Tile Entities
            ReflectionHelper.forEachValueIn(mod.getModBlockRegistry(), IInfinityBlockWithTile.class, (IInfinityBlockWithTile block) -> {
                if (block.isEnabled()) {
                    mod.getLogger().debug("Registering Tile for Block: " + block.getInternalName());
                    TileEntity te = block.createNewTileEntity(null, 0);
                    assert (te != null);
                    GameRegistry.registerTileEntity(te.getClass(), mod.getModId().toLowerCase() + ":tile." + block.getInternalName());
                }
            });
        }
    }

    default void registerItems(InfinityMod mod, IForgeRegistry<Item> registry) {
        // Blocks
        if (mod.getModBlockRegistry() != null) {
            ReflectionHelper.forEachValueIn(mod.getModBlockRegistry(), IInfinityBlock.class, (IInfinityBlock block) -> {
                if (block.isEnabled()) {
                    block.getItemBlock().ifPresent(item -> {
                        mod.getLogger().debug("Registering ItemBlock: " + block.getInternalName());
                        final String unlocalized = mod.getModId().toLowerCase() + ":" + block.getInternalName();
                        item.setTranslationKey(unlocalized);
                        register(mod, registry, item, block.getInternalName());
                    });
                }
            });
        }

        //items
        if (mod.getModItemRegistry() != null) {
            ReflectionHelper.forEachValueIn(mod.getModItemRegistry(), IInfinityItem.class, (IInfinityItem item) -> {
                if ((item instanceof Item) && item.isEnabled()) {
                    mod.getLogger().debug("Registering Item: " + item.getInternalName());
                    String unlocalized = mod.getModId().toLowerCase() + ':' + item.getInternalName();
                    ((Item) item).setTranslationKey(unlocalized);
                    register(mod, registry, (Item) item, item.getInternalName());
                    for (String tag : item.getOreTags()) {
                        OreDictionary.registerOre(tag, (Item) item);
                    }
                }
            });
        }
    }
    
    default void registerRecipes(InfinityMod mod, IForgeRegistry<IRecipe> registry) {
        // Block Recipes
        if (mod.getModBlockRegistry() != null) {
            ReflectionHelper.forEachValueIn(mod.getModBlockRegistry(), IRecipeRegisterer.class, (r) -> r.registerRecipes(registry));
        }
        
        // Item Recipes
        if (mod.getModItemRegistry() != null) {
            ReflectionHelper.forEachValueIn(mod.getModItemRegistry(), IRecipeRegisterer.class, (r) -> r.registerRecipes(registry));
        }
    }

    default void registerBiomes(InfinityMod mod, IForgeRegistry<Biome> registry) {
        if (mod.getModBiomeRegistry() != null) {
            ReflectionHelper.forEachValueIn(mod.getModBiomeRegistry(), Biome.class,
                    biome -> register(mod, registry, biome, biome.getBiomeName()));
        }
    }

    default void registerEnchantments(InfinityMod mod, IForgeRegistry<Enchantment> registry) {
        if (mod.getModEnchantmentRegistry() != null) {
            ReflectionHelper.forEachValueIn(mod.getModEnchantmentRegistry(), Enchantment.class,
                    enchantment -> register(mod, registry, enchantment, enchantment.getName()));
        }
    }

    default void registerEntities(InfinityMod mod, IForgeRegistry<EntityEntry> registry) {
        if (mod.getModEntityRegistry() != null) {
            ReflectionHelper.forEachValueIn(mod.getModEntityRegistry(), EntityRegistryEntry.class,
                    (EntityRegistryEntry entry) -> {
                        if (entry.isEnabled()) {
                            entry.register(mod, registry);
                        }
                    });
        }
    }

    default void registerPotions(InfinityMod mod, IForgeRegistry<Potion> registry) {
        if (mod.getModPotionRegistry() != null) {
            ReflectionHelper.forEachValueIn(mod.getModPotionRegistry(), Potion.class,
                    potion -> register(mod, registry, potion, potion.getName()));
        }
    }

    default void registerPotionTypes(InfinityMod mod, IForgeRegistry<PotionType> registry) {
        if (mod.getModPotionTypeRegistry() != null) {
            ReflectionHelper.forEachValueIn(mod.getModPotionTypeRegistry(), PotionType.class,
                    type -> register(mod, registry, type, type.getNamePrefixed("type")));
        }
    }

    default void registerSounds(InfinityMod mod, IForgeRegistry<SoundEvent> registry) {
        if (mod.getModSoundRegistry() != null) {
            ReflectionHelper.forEachValueIn(mod.getModSoundRegistry(), SoundEvent.class,
                    sound -> register(mod, registry, sound, sound.getSoundName().getPath()));
        }
    }

    default void registerVillagerProfessions(InfinityMod mod, IForgeRegistry<VillagerRegistry.VillagerProfession> registry) {
        if (mod.getModVillagerProfessionRegistry() != null) {
            ReflectionHelper.forEachValueIn(mod.getModVillagerProfessionRegistry(), VillagerRegistry.VillagerProfession.class,
                    profession -> register(mod, registry, profession, profession.toString()));
        }
    }

    default <T extends IForgeRegistryEntry<T>> void register(InfinityMod mod, IForgeRegistry<T> registry, T object, String name) {
        object.setRegistryName(new ResourceLocation(mod.getModId().toLowerCase(), name.toLowerCase()));
        registry.register(object);
    }

    @Override
    default void initEnd(FMLInitializationEvent event) {
        Module.getActiveModules().forEach(Module::init);
    }

    @Override
    default void postInitEnd(FMLPostInitializationEvent event) {
        Module.getActiveModules().forEach(Module::postInit);
    }

    default void initModConfiguration(InfinityConfigurationHandler handler) {
        handler.initializeConfiguration();
    }

    default SidedSoundDelegate getSoundDelegate() {
        return new SoundDelegateServer();
    }

    @Override
    default void registerEventHandlers() {
        for (Module module : Module.getActiveModules()) {
            module.getCommonEventHandlers().forEach(this::registerEventHandler);
        }
    }

    @Override
    default void registerCapabilities() {
        for (Module module : Module.getActiveModules()) {
            module.getCapabilities().forEach(this::registerCapability);
        }
    }

    @Override
    default void activateRequiredModules() {}
}
