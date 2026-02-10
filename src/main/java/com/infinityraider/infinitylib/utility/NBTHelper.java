package com.infinityraider.infinitylib.utility;

import net.minecraft.nbt.NBTTagCompound;

public class NBTHelper {
    public static NBTTagCompound writeBoolArray(String key, NBTTagCompound tag, boolean[] array) {
        int[] ints = new int[array.length];
        for(int i = 0; i < array.length; i++) {
            ints[i] = array[i] ? 1 : 0;
        }
        tag.setIntArray(key, ints);
        return tag;
    }

    public static boolean[] readBoolArray(String key, NBTTagCompound tag) {
        int[] ints = tag.getIntArray(key);
        boolean[] array = new boolean[ints.length];
        for(int i = 0; i < ints.length; i++) {
            array[i] = ints[i] > 0;
        }
        return array;
    }
}
