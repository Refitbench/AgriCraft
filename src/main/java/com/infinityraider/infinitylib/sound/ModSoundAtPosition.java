package com.infinityraider.infinitylib.sound;

import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModSoundAtPosition extends ModSound {
    private final Vec3d position;

    protected ModSoundAtPosition(SoundDelegateClient delegate, Vec3d position, SoundTaskClient task) {
        super(delegate, task);
        this.position = position;
    }

    @Override
    protected void updateSound() {
        this.xPosF = (float) this.position.x;
        this.yPosF = (float) this.position.y;
        this.zPosF = (float) this.position.z;
    }
}