package com.infinityraider.infinitylib.modules.dualwield;

import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSwingArm extends MessageBase<IMessage> {
    private EntityPlayer player;
    private int hand;

    public MessageSwingArm() {
        super();
    }

    public MessageSwingArm(EntityPlayer player, EnumHand hand) {
        this();
        this.player = player;
        this.hand = hand.ordinal();
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(this.player != null) {
            ArmSwingHandler.getInstance().swingArm(this.player, EnumHand.values()[this.hand]);
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
