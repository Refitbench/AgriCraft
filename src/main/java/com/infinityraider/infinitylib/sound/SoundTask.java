package com.infinityraider.infinitylib.sound;

import com.infinityraider.infinitylib.utility.IStoppable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public abstract class SoundTask implements IStoppable {
    private final String id;
    private final SoundEvent sound;
    private final SoundCategory category;

    private float volume;
    private float pitch;

    private boolean repeat;
    private int repeatDelay;

    protected SoundTask(String uuid, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        this. id = uuid;
        this.sound = sound;
        this.category = category;
        this.setVolume(volume);
        this.setPitch(pitch);
    }

    public final String getUUID() {
        return this. id;
    }

    public final SoundEvent getSound() {
        return this.sound;
    }

    public final SoundCategory getCategory() {
        return this.category;
    }

    public float getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }

    public boolean repeat() {
        return this.repeat;
    }

    public int repeatDelay() {
        return this.repeatDelay;
    }

    public SoundTask setVolume(float volume) {
        this.volume = volume;
        return this;
    }

    public SoundTask setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public SoundTask setRepeat(boolean repeat) {
        this.repeat = repeat;
        return this;
    }

    public SoundTask setRepeatDelay(int ticks) {
        this.repeatDelay = ticks;
        return this;
    }

    @Override
    public final void stop() {
        ModSoundHandler.getInstance().stopSound(this);
    }
}