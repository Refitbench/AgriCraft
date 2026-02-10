package com.infinityraider.infinitylib.sound;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModSoundAtEntity extends ModSound {
    private final Entity entity;

    public ModSoundAtEntity(SoundDelegateClient delegate, Entity entity, SoundTaskClient task) {
        super(delegate, task);
        this.entity = entity;
    }

    @Override
    public void updateSound() {
        if(this.entity.isEntityAlive()) {
            this.xPosF = (float) this.entity.posX;
            this.yPosF = (float) this.entity.posY;
            this.zPosF = (float) this.entity.posZ;
        } else {
            this.donePlaying = true;
        }
    }
}