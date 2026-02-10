package com.infinityraider.infinitylib.modules.specialpotioneffect;

import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSyncPotions extends MessageBase<IMessage> {
    private Entity entity;
    private NBTTagCompound nbt;

    public MessageSyncPotions() {
        super();
    }

    public MessageSyncPotions(PotionTracker tracker) {
        this();
        this.entity = tracker.getEntity();
        this.nbt = tracker.writeToNBT();
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(this.entity instanceof EntityLivingBase && this.nbt != null) {
            PotionTracker tracker = CapabilityPotionTracker.getPotionTracker((EntityLivingBase) this.entity);
            if(tracker != null) {
                tracker.readFromNBT(this.nbt);
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
