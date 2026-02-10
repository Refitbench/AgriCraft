package com.infinityraider.infinitylib.network.serialization;

public interface IMessageSerializer<T> {
    boolean accepts(Class<T> clazz);

    IMessageWriter<T> getWriter(Class<T> clazz);

    IMessageReader<T> getReader(Class<T> clazz);
}
