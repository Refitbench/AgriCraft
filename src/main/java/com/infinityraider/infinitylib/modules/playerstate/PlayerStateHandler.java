package com.infinityraider.infinitylib.modules.playerstate;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.ThrowableImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.UUID;

public class PlayerStateHandler {
    private static final PlayerStateHandler INSTANCE = new PlayerStateHandler();

    public static PlayerStateHandler getInstance() {
        return INSTANCE;
    }

    private final HashMap<UUID, State> states;

    private PlayerStateHandler() {
        this.states = new HashMap<>();
    }

    State getState(EntityPlayer player) {
        if(!states.containsKey(player.getUniqueID())) {
            states.put(player.getUniqueID(), new State(player));
        }
        return states.get(player.getUniqueID());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    public void onEntityImpactEvent(ThrowableImpactEvent event) {
        if(event.getRayTraceResult().entityHit instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getRayTraceResult().entityHit;
            if(getState(player).isEthereal()) {
                event.setCanceled(true);
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    public void onEntityHurtEvent(LivingHurtEvent event) {
        if(event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if(getState(player).isInvulnerable()) {
                event.setCanceled(true);
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    public void onPlayerRenderPreEvent(RenderPlayerEvent.Pre event) {
        this.cancelRenderEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    public void onPlayerRenderPostEvent(RenderPlayerEvent.Post event) {
        this.cancelRenderEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    public void onPlayerRenderEvent(RenderPlayerEvent.Specials.Pre event) {
        this.cancelRenderEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    public void onPlayerRenderEvent(RenderPlayerEvent.Specials.Post event) {
        this.cancelRenderEvent(event);
    }

    @SideOnly(Side.CLIENT)
    private void cancelRenderEvent(RenderPlayerEvent event) {
        if(getState(event.getEntityPlayer()).isInvisible()) {
            if(event.isCancelable()) {
                event.setCanceled(true);
            }
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onEntityTargetingEvent(LivingSetAttackTargetEvent event) {
        EntityLivingBase target = event.getTarget();
        EntityLivingBase attacker = event.getEntityLiving();
        if(target == null || attacker == null || !(target instanceof EntityPlayer) || !(attacker instanceof EntityLiving)) {
            return;
        }
        if(getState((EntityPlayer) target).isUndetectable()) {
            ((EntityLiving) attacker).setAttackTarget(null);
        }
    }

}
