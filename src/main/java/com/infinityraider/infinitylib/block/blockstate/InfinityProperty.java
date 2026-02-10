package com.infinityraider.infinitylib.block.blockstate;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;

public class InfinityProperty<T extends Comparable<T>> {

    private final IProperty<T> property;
    private final T defaultValue;

    public InfinityProperty(IProperty<T> property, T defaultValue) {
        this.property = property;
        this.defaultValue = defaultValue;
    }

    public IProperty<T> getProperty() {
        return property;
    }

    public T getDefault() {
        return defaultValue;
    }

    public <B extends IBlockState> B applyToBlockState(B state) {
        return applyToBlockState(state, getDefault());
    }

    public <B extends IBlockState> B applyToBlockState(B state, T value) {
        return (B) state.withProperty(getProperty(), value);
    }

    public T getValue(IBlockState state) {
        if (state.getPropertyKeys().contains(this.getProperty())) {
            return state.getValue(getProperty());
        } else {
            return this.defaultValue;
        }
    }
}
