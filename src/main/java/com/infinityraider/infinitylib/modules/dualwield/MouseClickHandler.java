package com.infinityraider.infinitylib.modules.dualwield;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.GameType;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class MouseClickHandler {
    private static final MouseClickHandler INSTANCE = new MouseClickHandler();

    private PlayerControllerMP playerController;

    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

    private static final int LMB = 0;
    private static final int RMB = 1;

    private MouseClickHandler() {}

    public static MouseClickHandler getInstance() {
        return INSTANCE;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onLeftClick(MouseEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        ItemStack stack = player.getHeldItemOffhand();
        if(event.getButton() != LMB) {
            return;
        }
        leftButtonPressed = !leftButtonPressed;
        if(stack == null) {
            return;
        }
        if(stack.getItem() instanceof IDualWieldedWeapon) {
            if(leftButtonPressed) {
                boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
                boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
                IDualWieldedWeapon weapon = (IDualWieldedWeapon) stack.getItem();
                attackEntity(weapon, player, stack, true, shift, ctrl, EnumHand.OFF_HAND);
                weapon.onItemUsed(stack, player, shift, ctrl, EnumHand.OFF_HAND);
                new MessageMouseButtonPressed(true, shift, ctrl).sendToServer();
                Minecraft.getMinecraft().player.swingArm(EnumHand.OFF_HAND);
            }
            event.setResult(Event.Result.DENY);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onRightClick(MouseEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        ItemStack stack = player.getHeldItemMainhand();
        if(event.getButton() != RMB) {
            return;
        }
        rightButtonPressed = !rightButtonPressed;
        if(stack == null) {
            return;
        }
        if(stack.getItem() instanceof IDualWieldedWeapon) {
            if(rightButtonPressed) {
                boolean shift = Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown();
                boolean ctrl = Minecraft.getMinecraft().gameSettings.keyBindSprint.isKeyDown();
                IDualWieldedWeapon weapon = (IDualWieldedWeapon) stack.getItem();
                attackEntity(weapon, player, stack, false, shift, ctrl, EnumHand.MAIN_HAND);
                weapon.onItemUsed(stack, player, shift, ctrl, EnumHand.MAIN_HAND);
                new MessageMouseButtonPressed(false, shift, ctrl).sendToServer();
                Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
            }
            event.setResult(Event.Result.DENY);
            event.setCanceled(true);
        }
    }

    private void attackEntity(IDualWieldedWeapon weapon, EntityPlayerSP player, ItemStack stack, boolean left, boolean shift, boolean ctrl, EnumHand hand) {
        if(Minecraft.getMinecraft().objectMouseOver == null) {
            return;
        }
        Entity target =  Minecraft.getMinecraft().objectMouseOver.entityHit;
        if(target != null) {
            if(!weapon.onItemAttack(stack, player, target, shift, ctrl, left ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND)) {
                if(this.playerController == null) {
                    this.playerController = Minecraft.getMinecraft().playerController;
                }
                if(this.playerController != null) {
                    new MessageAttackDualWielded(target, left, shift, ctrl).sendToServer();
                    if(this.playerController.getCurrentGameType() != GameType.SPECTATOR) {
                        ModuleDualWield.getInstance().attackTargetEntityWithCurrentItem(player, target, weapon, stack, hand);
                        player.resetCooldown();
                    }
                }
            }
        }
    }
}
