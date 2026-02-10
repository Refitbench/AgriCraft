package com.infinityraider.infinitylib.sound;

import com.infinityraider.infinitylib.utility.IStoppable;
import net.minecraft.client.audio.ISound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IModSound extends ISound, IStoppable {
    boolean repeat();

    int repeatDelay();

    IModSound setVolume(float volume);

    IModSound setPitch(float pitch);

    IModSound setRepeat(boolean repeat);

    IModSound setRepeatDelay(int ticks);

    String getUUID();
}