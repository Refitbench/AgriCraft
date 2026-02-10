package com.infinityraider.infinitylib.network;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSetEntityDead extends MessageBase<IMessage> {
    private Entity entity;

    public MessageSetEntityDead() {
        super();
    }

    public MessageSetEntityDead(Entity entity) {
        this();
        this.entity = entity;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(this.entity != null && !this.entity.isDead) {
            this.entity.setDead();
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
