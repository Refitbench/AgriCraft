package com.infinityraider.infinitylib.network.serialization;

import com.infinityraider.infinitylib.InfinityLib;
import com.infinityraider.infinitylib.network.MessageBase;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Field;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class MessageElement<T> {
    private final Field field;
    private final IMessageWriter<T> writer;
    private final IMessageReader<T> reader;

    private MessageElement(Field field, IMessageWriter<T> writer, IMessageReader<T> reader) {
        this.field = field;
        this.writer = writer;
        this.reader = reader;
    }

    public void writeToByteBuf(ByteBuf buf, MessageBase msg) {
        T data = null;
        try {
            data = (T) this.field.get(msg);
        } catch (Exception e) {
            InfinityLib.instance.getLogger().error("Failed getting field data, (enable debug mode in the config for more info)");
            InfinityLib.instance.getLogger().printStackTrace(e);
        }
        if(data != null) {
            ByteBufUtil.writeBoolean(buf, true);
            this.writer.writeData(buf, data);
        } else {
            ByteBufUtil.writeBoolean(buf, false);
        }
    }

    public void readFromByteBuf(ByteBuf buf, MessageBase msg) {
        boolean shouldRead = buf.readBoolean();
        if (shouldRead) {
            T data = this.reader.readData(buf);
            if(data != null) {
                try {
                    this.field.set(msg, data);
                } catch(Exception e) {
                    InfinityLib.instance.getLogger().error("Failed setting field data, (enable debug mode in the config for more info)");
                    InfinityLib.instance.getLogger().printStackTrace(e);
                }
            } else {
                InfinityLib.instance.getLogger().debug("Object was null, did not set field " 
                                + this.field.getDeclaringClass().getName()
                                + "." + this.field.getName());
            }
        }
    }

    public static Optional<MessageElement> createNewElement(Field field) {
        field.setAccessible(true);
        Class clazz = field.getType();
        Optional<IMessageSerializer> serializer = MessageSerializerStore.getMessageSerializer(clazz);
        if (serializer.isPresent()) {
            MessageElement element = new MessageElement(field, serializer.get().getWriter(clazz), serializer.get().getReader(clazz));
            return Optional.of(element);
        } else {
            return Optional.empty();
        }
    }
}
