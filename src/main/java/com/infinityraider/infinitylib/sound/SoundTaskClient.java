package com.infinityraider.infinitylib.sound;

import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundTaskClient extends SoundTask {
    SoundTaskClient(String uuid, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        super(uuid, sound, category, volume, pitch);
    }

    @Override
    public SoundTaskClient setVolume(float volume) {
        super.setVolume(volume);
        return this;
    }

    @Override
    public SoundTaskClient setPitch(float pitch) {
        super.setPitch(pitch);
        return this;
    }

    @Override
    public SoundTaskClient setRepeat(boolean repeat) {
        super.setRepeat(repeat);
        return this;
    }

    @Override
    public SoundTaskClient setRepeatDelay(int ticks) {
        super.setRepeatDelay(ticks);
        return this;
    }
}