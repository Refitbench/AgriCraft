package com.infinityraider.infinitylib.network.serialization;

public class MessageSerializerGeneric<T> implements IMessageSerializer<T> {

    public static <T> IMessageSerializer<T> createSerializer(Class<T> clazz, IMessageWriter<T> writer, IMessageReader<T> reader) {
        return new MessageSerializerGeneric<>(clazz, writer, reader);
    }

    private final Class<T> clazz;
    private final IMessageWriter<T> writer;
    private final IMessageReader<T> reader;

    public MessageSerializerGeneric(Class<T> clazz, IMessageWriter<T> writer, IMessageReader<T> reader) {
        this.clazz = clazz;
        this.writer = writer;
        this.reader = reader;
    }

    @Override
    public boolean accepts(Class<T> clazz) {
        return clazz == this.clazz;
    }

    @Override
    public IMessageWriter<T> getWriter(Class<T> clazz) {
        return this.writer;
    }

    @Override
    public IMessageReader<T> getReader(Class<T> clazz) {
        return this.reader;
    }
}
