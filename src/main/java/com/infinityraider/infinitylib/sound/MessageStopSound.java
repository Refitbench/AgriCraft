package com.infinityraider.infinitylib.sound;

import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageStopSound extends MessageBase<IMessage> {
    private String uuid;

    public MessageStopSound() {
        super();
    }

    public MessageStopSound(SoundTask task) {
        this();
        this.uuid = task.getUUID();
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(this.uuid != null && ctx.side == Side.CLIENT) {
            ModSoundHandler.getInstance().onSoundMessage(this);
        }
    }

    public String getUUID() {
        return this.uuid;
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}