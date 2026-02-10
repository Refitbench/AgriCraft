package com.infinityraider.infinitylib.sound;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;

public class SoundTaskServer extends SoundTask {
    SoundTaskServer(SoundEvent sound, SoundCategory category, float volume, float pitch) {
        super(MathHelper.getRandomUUID(ThreadLocalRandom.current()).toString(), sound, category, volume, pitch);
    }
}