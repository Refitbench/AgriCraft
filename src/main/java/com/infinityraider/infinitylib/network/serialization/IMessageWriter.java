package com.infinityraider.infinitylib.network.serialization;

import io.netty.buffer.ByteBuf;

public interface IMessageWriter<T> {
    /**
     * writes data to a byte buffer
     * @param buf the byte buffer
     * @param data the data to write
     */
    void writeData(ByteBuf buf, T data);
}
