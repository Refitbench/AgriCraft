package com.infinityraider.infinitylib.network;

import com.infinityraider.infinitylib.InfinityLib;
import com.infinityraider.infinitylib.network.serialization.IMessageReader;
import com.infinityraider.infinitylib.network.serialization.IMessageSerializer;
import com.infinityraider.infinitylib.network.serialization.IMessageWriter;
import com.infinityraider.infinitylib.network.serialization.MessageSerializerStore;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public final class NetworkWrapperDummy implements INetworkWrapper {
    private static final NetworkWrapperDummy INSTANCE = new NetworkWrapperDummy();

    public static NetworkWrapperDummy getInstance() {
        return INSTANCE;
    }

    private NetworkWrapperDummy() {}
    
    private void logErrorMessage(MessageBase msg) {
        InfinityLib.instance.getLogger().error("ATTEMPTED TO SEND AN UNREGISTERED MESSAGE: " + msg.getClass());
        InfinityLib.instance.getLogger().error("notify this to the mod author");
    }
    
    @Override
    public final void sendToAll(MessageBase message) {
        this.logErrorMessage(message);
    }

    @Override
    public final void sendTo(MessageBase message, EntityPlayerMP player) {
        this.logErrorMessage(message);
    }

    @Override
    public final void sendToAllAround(MessageBase message, World world, double x, double y, double z, double range) {
        this.logErrorMessage(message);
    }

    @Override
    public final void sendToAllAround(MessageBase message, int dimension, double x, double y, double z, double range) {
        this.logErrorMessage(message);
    }

    @Override
    public final void sendToAllAround(MessageBase message, NetworkRegistry.TargetPoint point) {
        this.logErrorMessage(message);
    }

    @Override
    public final void sendToDimension(MessageBase message, World world) {
        this.logErrorMessage(message);
    }

    @Override
    public final void sendToDimension(MessageBase message, int dimensionId) {
        this.logErrorMessage(message);
    }

    @Override
    public final void sendToServer(MessageBase message) {
        this.logErrorMessage(message);
    }

    @Override
    public final <REQ extends MessageBase<REPLY>, REPLY extends IMessage> void registerMessage(Class<? extends REQ> message) {
        // > . >
    }

    @Override
    public final <T> void registerDataSerializer(Class<T> clazz, IMessageWriter<T> writer, IMessageReader<T> reader) {
        MessageSerializerStore.registerMessageSerializer(clazz, writer, reader);
    }

    @Override
    public <T> void registerDataSerializer(IMessageSerializer<T> serializer) {
        MessageSerializerStore.registerMessageSerializer(serializer);
    }
}
