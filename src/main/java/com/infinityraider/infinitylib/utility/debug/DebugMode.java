package com.infinityraider.infinitylib.utility.debug;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Class to aid with debugging
 */
public abstract class DebugMode {

    public abstract String debugName();

    public abstract void debugActionBlockClicked(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ);

    public abstract void debugActionClicked(ItemStack stack, World world, EntityPlayer player, EnumHand hand);

    public abstract void debugActionEntityClicked(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand);

}
