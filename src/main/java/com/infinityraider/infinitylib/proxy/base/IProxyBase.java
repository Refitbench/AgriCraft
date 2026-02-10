package com.infinityraider.infinitylib.proxy.base;

import com.infinityraider.infinitylib.capability.CapabilityHandler;
import com.infinityraider.infinitylib.capability.ICapabilityImplementation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings("unused")
public interface IProxyBase {
    /**
     * -----------------------------
     * FML MOD LOADING CYCLE METHODS
     * -----------------------------
     */

    /**
     * Performs all needed operations for the proxy's side during FML's pre init stage, called before InfinityLib does its standard operations
     */
    default void preInitStart(FMLPreInitializationEvent event) {}

    /**
     * Performs all needed operations for the proxy's side during FML's pre init stage, called after InfinityLib has done its standard operations
     */
    default void preInitEnd(FMLPreInitializationEvent event) {}

    /**
     * Performs all needed operations for the proxy's side during FML's init stage, called before InfinityLib does its standard operations
     */
    default void initStart(FMLInitializationEvent event) {}

    /**
     * Performs all needed operations for the proxy's side during FML's init stage, called after InfinityLib has done its standard operations
     */
    default void initEnd(FMLInitializationEvent event) {}

    /**
     * Performs all needed operations for the proxy's side during FML's post init stage, called before InfinityLib does its standard operations
     */
    default void postInitStart(FMLPostInitializationEvent event) {}

    /**
     * Performs all needed operations for the proxy's side during FML's post init stage, called after InfinityLib has done its standard operations
     */
    default void postInitEnd(FMLPostInitializationEvent event) {}

    /**
     * Performs all needed operations for the proxy's side when the server is about to start, called before InfinityLib does its standard operations
     */
    default void onServerAboutToStart(FMLServerAboutToStartEvent event) {}

    /**
     * Performs all needed operations for the proxy's side when the server is starting, called before InfinityLib does its standard operations
     */
    default void onServerStarting(FMLServerStartingEvent event) {}

    /**
     * Performs all needed operations for the proxy's side when the server is started, called before InfinityLib does its standard operations
     */
    default void onServerStarted(FMLServerStartedEvent event) {}

    /**
     * Performs all needed operations for the proxy's side when the server is stopping, called before InfinityLib does its standard operations
     */
    default void onServerStopping(FMLServerStoppingEvent event) {}

    /**
     * Performs all needed operations for the proxy's side when the server is stopped, called before InfinityLib does its standard operations
     */
    default void onServerStopped(FMLServerStoppedEvent event) {}

    /**
     * -------------------
     * REGISTERING METHODS
     * -------------------
     */

    /**
     * Called to register the event handlers
     */
    void registerEventHandlers();

    /**
     * Called to activate all the necessary InfinityLib modules for this mod
     */
    void activateRequiredModules();

    /**
     * Called to register the capabilities for this mod
     */
    void registerCapabilities();

    /** Registers an event handler */
    default void registerEventHandler(Object handler) {
        MinecraftForge.EVENT_BUS.register(handler);
    }

    /** Registers a capability */
    default void registerCapability(ICapabilityImplementation capability) {
        CapabilityHandler.getInstance().registerCapability(capability);
    }

    /**
     * ---------------
     * UTILITY METHODS
     * ---------------
     */

    /**
     * @return The physical side, is always Side.SERVER on the server and Side.CLIENT on the client
     */
    Side getPhysicalSide();

    /**
     * @return The effective side, on the server, this is always Side.SERVER, on the client it is dependent on the thread
     */
    Side getEffectiveSide();

    /**
     * @return The minecraft server instance
     */
    default MinecraftServer getMinecraftServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    /**
     * @return the instance of the EntityPlayer on the client, null on the server
     */
    EntityPlayer getClientPlayer();

    /**
     * @return the client World object on the client, null on the server
     */
    World getClientWorld();

    /**
     * Returns the World object corresponding to the dimension id
     * @param dimension dimension id
     * @return world object
     */
    World getWorldByDimensionId(int dimension);

    /**
     * Returns the entity in that dimension with that id
     * @param dimension dimension id
     * @param id entity id
     * @return the entity
     */
    default Entity getEntityById(int dimension, int id) {
        return getEntityById(getWorldByDimensionId(dimension), id);
    }

    /**
     *  @return  the entity in that World object with that id
     */
    default Entity getEntityById(World world, int id) {
        return world == null ? null : world.getEntityByID(id);
    }

    /** Queues a task to be executed on this side */
    void queueTask(Runnable task);
}
