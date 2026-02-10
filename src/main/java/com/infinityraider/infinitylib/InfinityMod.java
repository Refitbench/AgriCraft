package com.infinityraider.infinitylib;

import com.infinityraider.agricraft.AgriCraft;
import com.infinityraider.infinitylib.config.IModConfiguration;
import com.infinityraider.infinitylib.config.InfinityConfigurationHandler;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.infinitylib.network.NetworkWrapper;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import com.infinityraider.infinitylib.utility.InfinityLogger;
import com.infinityraider.infinitylib.utility.ModEventHandlerHack;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * This interface should be implemented in a mod's main class to have the registering of Items, Blocks, Renderers, etc. handled by InfinityLib
 * When implementing this interface, the mod must also be annotated with @InfinityMod
 */
@SuppressWarnings("unused")
public abstract class InfinityMod {
    private final InfinityLogger logger;
    private final INetworkWrapper networkWrapper;
    private final InfinityConfigurationHandler configurationHandler;

    public InfinityMod() {
        this.logger = new InfinityLogger(this);
        this.networkWrapper = new NetworkWrapper(this);
        this.configurationHandler = new InfinityConfigurationHandler(this);
        MinecraftForge.EVENT_BUS.register(this);
        ModEventHandlerHack.doHack(this);   //you ain't seen nothing
        this.initializeAPI();
    }

    public final InfinityLogger getLogger() {
        return this.logger;
    }

    public final INetworkWrapper getNetworkWrapper() {
        return this.networkWrapper;
    }

    public final InfinityConfigurationHandler getConfigurationHandler() {
        return this.configurationHandler;
    }

    /**
     * @return The sided proxy object for this mod
     */
    public abstract IProxyBase proxy();

    /**
     * @return The mod ID of the mod
     */
    public abstract String getModId();

    public abstract IModConfiguration getConfiguration();

    /**
     * Used to register the Blocks, recipes, renderers, TileEntities, etc. for all the mod's blocks.
     * The object returned by this should have a field for each of its blocks
     * @return Block registry object or class
     */
    public Object getModBlockRegistry() {
        return null;
    }

    /**
     * Used to register the Items, recipes, renderers, etc. for all the mod's items.
     * The object returned by this should have a field for each of its items
     * @return Item registry object or class
     */
    public Object getModItemRegistry() {
        return null;
    }

    /**
     * Used to register the Biomes for all the mod's biomes.
     * The object returned by this should have a field for each of its biomes
     * @return Biome registry object or class
     */
    public Object getModBiomeRegistry() {
        return null;
    }

    /**
     * Used to register the Enchantments for all the mod's enchantments.
     * The object returned by this should have a field for each of its enchantments
     * @return Enchantment registry object or class
     */
    public  Object getModEnchantmentRegistry() {
        return null;
    }

    /**
     * Used to register the Entities for all the mod's entities.
     * The object returned by this should have a field for each of its entities
     * @return Entity registry object or class
     */
    public  Object getModEntityRegistry() {
        return null;
    }

    /**
     * Used to register the Potions for all the mod's potions.
     * The object returned by this should have a field for each of its Potions
     * @return Potion registry object or class
     */
    public Object getModPotionRegistry() {
        return null;
    }

    /**
     * Used to register the PotionTypes for all the mod's potion type.
     * The object returned by this should have a field for each of its PotionTypes
     * @return PotionType registry object or class
     */
    public Object getModPotionTypeRegistry() {
        return null;
    }

    /**
     * Used to register the Sounds for all the mod's sounds.
     * The object returned by this should have a field for each of its SoundEvents
     * @return SoundEvent registry object or class
     */
    public Object getModSoundRegistry() {
        return null;
    }

    /**
     * Used to register the VillagerProfessions for all the mod's villager professions.
     * The object returned by this should have a field for each of its VillagerProfessions
     * @return VillagerProfession registry object or class
     */
    public Object getModVillagerProfessionRegistry() {
        return null;
    }

    /**
     * Register all messages added by this mod
     * @param wrapper NetworkWrapper instance to register messages to
     */
    public void registerMessages(INetworkWrapper wrapper) {}

    /**
     * Use to initialize the mod API
     */
    public void initializeAPI() {}

    /*
     * ----------------------------
     * Registering events
     * ----------------------------
     */

    @SubscribeEvent
    public final void initRegistries(RegistryEvent.NewRegistry event) {
        InfinityLib.proxy.registerRegistries(this, event);
    }

    @SubscribeEvent
    public final void registerBlocks(RegistryEvent.Register<Block> event) {
        InfinityLib.proxy.registerBlocks(this, event.getRegistry());
    }

    @SubscribeEvent
    public final void registerItems(RegistryEvent.Register<Item> event) {
        InfinityLib.proxy.registerItems(this, event.getRegistry());
    }
    
    @SubscribeEvent
    public final void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        AgriCraft.proxy.initCustomWoodTypes();

        InfinityLib.proxy.registerRecipes(this, event.getRegistry());
    }

    @SubscribeEvent
    public final void registerBiomes(RegistryEvent.Register<Biome> event) {
        InfinityLib.proxy.registerBiomes(this, event.getRegistry());
    }

    @SubscribeEvent
    public final void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        InfinityLib.proxy.registerEnchantments(this, event.getRegistry());
    }

    @SubscribeEvent
    public final void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        InfinityLib.proxy.registerEntities(this, event.getRegistry());
    }

    @SubscribeEvent
    public final void registerPotions(RegistryEvent.Register<Potion> event) {
        InfinityLib.proxy.registerPotions(this, event.getRegistry());
    }

    @SubscribeEvent
    public final void registerPotionTypes(RegistryEvent.Register<PotionType> event) {
        InfinityLib.proxy.registerPotionTypes(this, event.getRegistry());
    }

    @SubscribeEvent
    public final void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        InfinityLib.proxy.registerSounds(this, event.getRegistry());
    }

    @SubscribeEvent
    public final void registerVillagerProfessions(RegistryEvent.Register<VillagerRegistry.VillagerProfession> event) {
        InfinityLib.proxy.registerVillagerProfessions(this, event.getRegistry());
    }


    /**
     * ----------------------------
     * FML Mod loading cycle events
     * ----------------------------
     */

    @Mod.EventHandler
    public final void preInit(FMLPreInitializationEvent event) {
        this.getLogger().debug("Starting Pre-Initialization");
        this.proxy().registerEventHandler(this);
        InfinityLib.proxy.initModConfiguration(this.getConfigurationHandler());
        proxy().preInitStart(event);
        proxy().activateRequiredModules();
        proxy().preInitEnd(event);
        this.getLogger().debug("Pre-Initialization Complete");
    }

    @Mod.EventHandler
    public final void init(FMLInitializationEvent event) {
        this.getLogger().debug("Starting Initialization");
        proxy().initStart(event);
        proxy().registerCapabilities();
        proxy().registerEventHandlers();
        registerMessages(this.getNetworkWrapper());
        proxy().initEnd(event);
        this.getLogger().debug("Initialization Complete");
    }

    @Mod.EventHandler
    public final void postInit(FMLPostInitializationEvent event) {
        this.getLogger().debug("Starting Post-Initialization");
        proxy().postInitStart(event);
        // Well looks like there is nothing to do here...
        proxy().postInitEnd(event);
        this.getLogger().debug("Post-Initialization Complete");
    }

    @Mod.EventHandler
    public final void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        proxy().onServerAboutToStart(event);
    }

    @Mod.EventHandler
    public final void onServerStarting(FMLServerStartingEvent event) {
        proxy().onServerStarting(event);
    }

    @Mod.EventHandler
    public final void onServerStarted(FMLServerStartedEvent event) {
        proxy().onServerStarted(event);
    }

    @Mod.EventHandler
    public final void onServerStopping(FMLServerStoppingEvent event) {
        proxy().onServerStopping(event);
    }

    @Mod.EventHandler
    public final void onServerStopped(FMLServerStoppedEvent event) {
        proxy().onServerStopped(event);
    }


    /**
     * --------------------------
     * Proxy utility method calls
     * --------------------------
     */

    /**
     * @return The physical side, is always Side.SERVER on the server and Side.CLIENT on the client
     */
    public final Side getPhysicalSide() {
        return this.proxy().getPhysicalSide();
    }

    /**
     * @return The effective side, on the server, this is always Side.SERVER, on the client it is dependent on the thread
     */
    public final Side getEffectiveSide() {
        return this.proxy().getEffectiveSide();
    }

    /**
     * @return The minecraft server instance
     */
    public final MinecraftServer getMinecraftServer() {
        return this.proxy().getMinecraftServer();
    }

    /**
     * @return the instance of the EntityPlayer on the client, null on the server
     */
    public final EntityPlayer getClientPlayer() {
        return this.proxy().getClientPlayer();
    }

    /**
     * @return the client World object on the client, null on the server
     */
    public final World getClientWorld() {
        return this.proxy().getClientWorld();
    }

    /**
     * Returns the World object corresponding to the dimension id
     * @param dimension dimension id
     * @return world object
     */
    public final World getWorldByDimensionId(int dimension) {
        return this.proxy().getWorldByDimensionId(dimension);
    }

    /**
     * Returns the entity in that dimension with that id
     * @param dimension dimension id
     * @param id entity id
     * @return the entity
     */
    public final Entity getEntityById(int dimension, int id) {
        return this.proxy().getEntityById(dimension, id);
    }

    /**
     *  @return  the entity in that World object with that id
     */
    public final Entity getEntityById(World world, int id) {
        return this.proxy().getEntityById(world, id);
    }

    /** Queues a task to be executed on this side */
    public final void queueTask(Runnable task) {
        this.proxy().queueTask(task);
    }
}
