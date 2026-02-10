package com.infinityraider.infinitylib.block.blockstate;

import com.infinityraider.infinitylib.reference.Names;
import com.infinityraider.infinitylib.utility.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.Arrays;

public class SidedConnection {
    private boolean[] connections;

    public SidedConnection() {
        this.connections = new boolean[EnumFacing.values().length + 1];
    }

    public boolean isConnected(EnumFacing side) {
        return connections[this.getIndex(side)];
    }

    public SidedConnection setConnected(EnumFacing side, boolean value) {
        this.connections[this.getIndex(side)] = value;
        return this;
    }

    private int getIndex(EnumFacing side) {
        return side == null ? EnumFacing.values().length : side.ordinal();
    }

    @Override
    public boolean equals(Object object) {
        if(object == this) {
            return true;
        }
        if(object instanceof SidedConnection) {
            return Arrays.equals(this.connections, ((SidedConnection) object).connections);
        }
        return false;
    }

    @Override
    public String toString() {
        return Arrays.toString(connections);
    }

    public void writeToNBT(NBTTagCompound tag) {
        NBTHelper.writeBoolArray(Names.NBT.CONNECTION, tag, this.connections);
    }

    public void readFromNBT(NBTTagCompound tag) {
        boolean[] connections =  NBTHelper.readBoolArray(Names.NBT.CONNECTION, tag);
        if(connections.length == this.connections.length) {
            this.connections = connections;
        }
    }

    public static class Property implements IUnlistedProperty<SidedConnection> {
        private final String name;

        public Property(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public boolean isValid(SidedConnection value) {
            return true;
        }

        @Override
        public Class<SidedConnection> getType() {
            return SidedConnection.class;
        }

        @Override
        public String valueToString(SidedConnection value) {
            return value.toString();
        }
    }
}
