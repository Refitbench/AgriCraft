package com.infinityraider.infinitylib.network;

import com.infinityraider.infinitylib.InfinityLib;
import com.infinityraider.infinitylib.InfinityMod;
import com.infinityraider.infinitylib.network.serialization.IMessageReader;
import com.infinityraider.infinitylib.network.serialization.IMessageSerializer;
import com.infinityraider.infinitylib.network.serialization.IMessageWriter;
import com.infinityraider.infinitylib.network.serialization.MessageSerializerStore;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings("unused")
public class NetworkWrapper implements INetworkWrapper {
    private final SimpleNetworkWrapper wrapper;
    private int nextId = 0;

    public NetworkWrapper(InfinityMod mod) {
        String id = mod.getModId();
        if(id.length() > 20) {
            id = id.substring(0, 20);
        }
        this.wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(id);
    }

    @Override
    public void sendToAll(MessageBase message) {
        if(message.getMessageHandlerSide() == Side.CLIENT) {
            this.wrapper.sendToAll(message);
        }
    }

    @Override
    public void sendTo(MessageBase message, EntityPlayerMP player) {
        if(message.getMessageHandlerSide() == Side.CLIENT) {
            this.wrapper.sendTo(message, player);
        }
    }

    @Override
    public void sendToAllAround(MessageBase message, World world, double x, double y, double z, double range) {
        if(message.getMessageHandlerSide() == Side.CLIENT) {
            this.sendToAllAround(message, world.provider.getDimension(), x, y, z, range);
        }
    }

    @Override
    public void sendToAllAround(MessageBase message, int dimension, double x, double y, double z, double range) {
        if(message.getMessageHandlerSide() == Side.CLIENT) {
            this.sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
        }
    }

    @Override
    public void sendToAllAround(MessageBase message, NetworkRegistry.TargetPoint point) {
        if(message.getMessageHandlerSide() == Side.CLIENT) {
            this.wrapper.sendToAllAround(message, point);
        }
    }

    @Override
    public void sendToDimension(MessageBase message, World world) {
        if(message.getMessageHandlerSide() == Side.CLIENT) {
            this.sendToDimension(message, world.provider.getDimension());
        }
    }

    @Override
    public void sendToDimension(MessageBase message, int dimensionId) {
        if(message.getMessageHandlerSide() == Side.CLIENT) {
            this.wrapper.sendToDimension(message, dimensionId);
        }
    }

    @Override
    public void sendToServer(MessageBase message) {
        if(message.getMessageHandlerSide() == Side.SERVER) {
            this.wrapper.sendToServer(message);
        }
    }

    @Override
    public <REQ extends MessageBase<REPLY>, REPLY extends IMessage> void registerMessage(Class<? extends REQ> message) {
        try {
            REQ msg = message.getDeclaredConstructor().newInstance();
            msg.getNecessarySerializers().stream().forEach(this::registerDataSerializer);
            Side side = msg.getMessageHandlerSide();
            wrapper.registerMessage(new MessageHandler<REQ, REPLY>(), message, nextId, side);
            InfinityLib.instance.getLogger().debug("Registered message \"" + message.getName() + "\" with id " + nextId);
            nextId = nextId + 1;
            MessageBase.onMessageRegistered(message, this);
        } catch (Exception e) {
            InfinityLib.instance.getLogger().printStackTrace(e);
        }
    }

    @Override
    public <T> void registerDataSerializer(Class<T> clazz, IMessageWriter<T> writer, IMessageReader<T> reader) {
        MessageSerializerStore.registerMessageSerializer(clazz, writer, reader);
    }

    @Override
    public <T> void registerDataSerializer(IMessageSerializer<T> serializer) {
        MessageSerializerStore.registerMessageSerializer(serializer);
    }

    private static final class MessageHandler<REQ extends MessageBase<REPLY>, REPLY extends IMessage> implements IMessageHandler<REQ, REPLY> {
        protected MessageHandler() {
        }

        @Override
        public final REPLY onMessage(REQ message, MessageContext ctx) {
            InfinityLib.proxy.queueTask(new MessageTask(message, ctx));
            return message.getReply(ctx);
        }
    }

    private static class MessageTask implements Runnable {
        private final MessageBase message;
        private final MessageContext ctx;

        private MessageTask(MessageBase message, MessageContext ctx) {
            this.message = message;
            this.ctx = ctx;
        }

        @Override
        public void run() {
            if(this.message.getMessageHandlerSide() == this.ctx.side) {
                this.message.processMessage(this.ctx);
            }
        }
    }
}
