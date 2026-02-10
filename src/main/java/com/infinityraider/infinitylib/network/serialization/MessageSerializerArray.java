package com.infinityraider.infinitylib.network.serialization;

import java.lang.reflect.Array;

@SuppressWarnings("OptionalGetWithoutIsPresent, unchecked")
public class MessageSerializerArray<T> implements IMessageSerializer<T> {
    public static final IMessageSerializer INSTANCE = new MessageSerializerArray();

    private MessageSerializerArray() {}

    @Override
    public boolean accepts(Class<T> clazz) {
        return clazz.isArray() && MessageSerializerStore.getMessageSerializer(clazz.getComponentType()).isPresent();
    }

    @Override
    public IMessageWriter<T> getWriter(Class<T> clazz) {
        IMessageSerializer element = MessageSerializerStore.getMessageSerializer(clazz.getComponentType()).get();
        IMessageWriter writer = element.getWriter(clazz.getComponentType());
        return (buf, data) -> {
            int size = Array.getLength(data);
            buf.writeInt(size);
            for (int i = 0; i < size; i++) {
                writer.writeData(buf, Array.get(data, i));
            }
        };
    }

    @Override
    public IMessageReader<T> getReader(Class<T> clazz) {
        IMessageSerializer element = MessageSerializerStore.getMessageSerializer(clazz.getComponentType()).get();
        IMessageReader reader = element.getReader(clazz.getComponentType());
        return (buf) -> {
            int size = buf.readInt();
            Object array = Array.newInstance(clazz.getComponentType(), size);
            for (int i = 0; i < size; i++) {
                Array.set(array, i, reader.readData(buf));
            }
            return (T) array;
        };
    }
}
