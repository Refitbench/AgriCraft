package com.infinityraider.infinitylib.modules.dualwield;

import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageAttackDualWielded extends MessageBase<IMessage> {
    private boolean left;
    private boolean shift;
    private boolean ctrl;
    private Entity entity;

    public MessageAttackDualWielded() {
        super();
    }

    public MessageAttackDualWielded(Entity entity, boolean left, boolean shift, boolean ctrl) {
        this();
        this.left = left;
        this.shift = shift;
        this.ctrl = ctrl;
        this.entity = entity;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.SERVER;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().player;
        if(player != null) {
            ItemStack stack = player.getHeldItem(left ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if(stack != null && stack.getItem() instanceof IDualWieldedWeapon) {
                IDualWieldedWeapon weapon = (IDualWieldedWeapon) stack.getItem();
                EnumHand hand = left ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
                weapon.onItemAttack(stack, player, entity, shift, ctrl, hand);
                ModuleDualWield.getInstance().attackTargetEntityWithCurrentItem(player, entity, weapon, stack, hand);
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
