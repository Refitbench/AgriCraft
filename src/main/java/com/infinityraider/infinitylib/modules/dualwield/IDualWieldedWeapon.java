package com.infinityraider.infinitylib.modules.dualwield;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * Interface implemented in Item classes which should be dual wielding weapons,
 * If you have this implemented in a class onItemUseFirst() and onItemRightClick() will no longer be called,
 * instead onLeftItemUsed() will be used if the player left clicks and onRightItemUsed() will be called if the player right clicks.
 * This is done automatically via event handling and the methods will be called on the client and on the server.
 *
 * There will also be a weapon rendered in the left hand, both in first person and third person,
 * the appropriate coordinate transformations are automatically taken care of, there is still the possibility to scale, translate and rotate the
 * item after this.
 *
 * IMPORTANT:
 * If you want to use this dual wielding interface, you have to activate the dual wielding module in your proxy (ModuleDualWield in this package)
 */
public interface IDualWieldedWeapon {
    /**
     * Called when the player uses the weapon
     * @param stack stack with this item
     * @param player Player using this item
     * @param shift if shift was held
     * @param ctrl if ctrl was held
     * @param hand the hand with which the item was used
     */
    void onItemUsed(ItemStack stack, EntityPlayer player, boolean shift, boolean ctrl, EnumHand hand);

    /**
     * Called when an entity is attacked with the weapon, can only be cancelled client side.
     * If cancelled client side this will not be called server side.
     * If not cancelled client side, returning true server side will not cancel the attack
     *
     * Damage should not be applied from this method, this method is primarily to determine if the attack should happen or not,
     * If the attack happens, logic is further mimicked and calls are forwarded to the conventional Item methods.
     *
     * @param stack ItemStack holding this item
     * @param player attacking player
     * @param e attacked entity
     * @param shift if shift was held
     * @param ctrl if ctrl was held
     * @param hand the hand with which the item was used to attack
     * @return if the attack should be cancelled
     */
    boolean onItemAttack(ItemStack stack, EntityPlayer player, Entity e, boolean shift, boolean ctrl, EnumHand hand);

    /**
     * When attacking a player using an axe, and if the player is blocking with a shield, it is possible to break the target's blocking.
     *
     * @return true from this method if the weapon should have this behaviour (wihtout having to extend ItemAxe)
     */
    default boolean isEffectiveAgainstShield() {
        return false;
    }
}
