package com.infinityraider.infinitylib.sound;

import net.minecraft.client.audio.MovingSound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class ModSound extends MovingSound implements IModSound {
    private final SoundDelegateClient delegate;
    private final String uuid;

    protected ModSound(SoundDelegateClient delegate, SoundTaskClient task) {
        super(task.getSound(), task.getCategory());
        this.delegate = delegate;
        this.uuid = task.getUUID();
        this.setVolume(task.getVolume());
        this.setPitch(task.getPitch());
        this.setRepeat(task.repeat());
        this.setRepeatDelay(task.repeatDelay());
    }

    @Override
    public final boolean repeat() {
        return this.repeat;
    }

    @Override
    public final int repeatDelay() {
        return this.repeatDelay;
    }

    @Override
    public final ModSound setVolume(float volume) {
        this.volume = volume;
        return this;
    }

    @Override
    public final ModSound setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    @Override
    public final ModSound setRepeat(boolean repeat) {
        this.repeat = repeat;
        return this;
    }

    @Override
    public final ModSound setRepeatDelay(int ticks) {
        this.repeatDelay = ticks;
        return this;
    }

    @Override
    public final String getUUID() {
        return this.uuid;
    }

    @Override
    public final void stop() {
        this.donePlaying = true;
    }

    @Override
    public final void update() {
        this.delegate.onSoundTick(this);
        this.updateSound();
    }

    protected abstract void updateSound();
}