package com.infinityraider.infinitylib.modules.specialpotioneffect;

import com.google.common.primitives.Ints;
import com.infinityraider.infinitylib.reference.Names;
import com.infinityraider.infinitylib.utility.ISerializable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;

import java.util.*;
import java.util.stream.Collectors;

public class PotionTracker implements ISerializable {
    private final EntityLivingBase entity;

    private Set<Integer> activeEffects;

    protected PotionTracker(EntityLivingBase entity) {
        this.activeEffects = new HashSet<>();
        this.entity = entity;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }

    public void updatePotionEffects(List<Potion> potions) {
        boolean update = false;
        //remove previously active potions from the potion list
        Iterator<Integer> it = activeEffects.iterator();
        while(it.hasNext()) {
            int id = it.next();
            Potion potion = Potion.getPotionById(id);
            if(!potions.contains(potion)) {
                it.remove();
                update = true;
            } else {
                potions.remove(potion);
            }
        }
        //add newly activated potions to the potion list
        if(potions.size() > 0) {
            activeEffects.addAll(potions.stream().map(Potion::getIdFromPotion).collect(Collectors.toList()));
            update = true;
        }
        //send changes to the client
        if(update) {
            this.syncToClient();
        }
    }

    public List<Potion> getActivePotions() {
        return this.activeEffects.stream().map(Potion::getPotionById).collect(Collectors.toList());
    }

    protected void syncToClient() {
        new MessageSyncPotions(this).sendToAll();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        this.activeEffects.clear();
        if(tag.hasKey(Names.NBT.POTIONS)) {
            this.activeEffects = Arrays.stream(tag.getIntArray(Names.NBT.POTIONS)).boxed().collect(Collectors.toSet());
        }
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setIntArray(Names.NBT.POTIONS, Ints.toArray(this.activeEffects));
        return tag;
    }
}
