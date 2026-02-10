/*
 */
package com.agricraft.agricore.plant;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author RlonRyan
 */
public enum AgriRenderType {

    @SerializedName(value = "hash", alternate = {"HASH", "#"})
    HASH('#'),
    @SerializedName(value = "cross", alternate = {"CROSS", "x", "X"})
    CROSS('X');

    private final char id;

    private AgriRenderType(char id) {
        this.id = id;
    }

    public char id() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("Render Type: %s (\'%c\')\n", name(), id());
    }

}
