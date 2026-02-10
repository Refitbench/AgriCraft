package com.infinityraider.infinitylib.sound;

import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;

public abstract class SidedSoundDelegate {
    public abstract SoundTask playSoundAtPositionOnce(Vec3d position, SoundEvent sound, SoundCategory category, float volume, float pitch);

    public abstract SoundTask playSoundAtEntityOnce(Entity e, SoundEvent sound, SoundCategory category, float volume, float pitch);

    public abstract SoundTask playSoundAtPositionContinuous(Vec3d position, SoundEvent sound, SoundCategory category, float volume, float pitch);

    public abstract SoundTask playSoundAtEntityContinuous(Entity entity, SoundEvent sound, SoundCategory category, float volume, float pitch);

    public abstract void stopSound(SoundTask task);

    abstract void onSoundMessage(MessagePlaySound message);

    abstract void onSoundMessage(MessageStopSound message);
}