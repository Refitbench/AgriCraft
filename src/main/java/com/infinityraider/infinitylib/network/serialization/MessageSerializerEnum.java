package com.infinityraider.infinitylib.network.serialization;

public class MessageSerializerEnum<T extends Enum> implements IMessageSerializer<T> {
    public static final MessageSerializerEnum INSTANCE = new MessageSerializerEnum();

    private MessageSerializerEnum() {}

    @Override
    public boolean accepts(Class<T> clazz) {
        return clazz.isEnum();
    }

    @Override
    public IMessageWriter<T> getWriter(Class<T> clazz) {
        return (buf, data) -> buf.writeInt(data.ordinal());
    }

    @Override
    public IMessageReader<T> getReader(Class<T> clazz) {
        return (buf) -> clazz.getEnumConstants()[buf.readInt()];
    }
}
