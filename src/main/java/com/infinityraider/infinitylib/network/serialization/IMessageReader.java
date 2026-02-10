package com.infinityraider.infinitylib.network.serialization;

import io.netty.buffer.ByteBuf;

public interface IMessageReader<T> {
    /**
     * Reads data from a byte buffer
     * @param buf the byte buffer
     * @return the read data
     */
    T readData(ByteBuf buf);
}
