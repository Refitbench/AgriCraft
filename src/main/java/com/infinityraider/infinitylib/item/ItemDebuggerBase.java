package com.infinityraider.infinitylib.item;

import com.infinityraider.infinitylib.reference.Names;
import com.infinityraider.infinitylib.utility.debug.DebugMode;
import com.infinityraider.infinitylib.utility.debug.DebugModeFeedback;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
/**
 * Base class for a debug item, allows a single item to have multiple debug modes
 */
public abstract class ItemDebuggerBase extends ItemBase {
    private final List<DebugMode> DEBUG_MODES;

    public ItemDebuggerBase() {
        this(true);
    }

    public ItemDebuggerBase(boolean isVanilla) {
        super("debugger");
        this.DEBUG_MODES = new ArrayList<>();
        this.DEBUG_MODES.add(new DebugModeFeedback());
        this.DEBUG_MODES.addAll(getDebugModes());
    }

    protected abstract List<DebugMode> getDebugModes();

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking()) {
            if (!world.isRemote) {
                DebugMode mode = this.changeDebugMode(stack);
                player.sendMessage(new TextComponentString("Set debug mode to " + mode.debugName()));
            }
        } else {
            this.getDebugMode(stack).debugActionClicked(stack, world, player, hand);
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!player.isSneaking()) {
            ItemStack stack = player.getHeldItem(hand);
            this.getDebugMode(stack).debugActionBlockClicked(stack, player, world, pos, hand, side, hitX, hitY, hitZ);
        }
        return EnumActionResult.PASS;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if(!player.isSneaking()) {
            this.getDebugMode(stack).debugActionEntityClicked(stack, player, target, hand);
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        DebugMode mode = getDebugMode(stack);
        tooltip.add("Right Click to use the debugger in its current mode");
        tooltip.add("Shift + Right Click to cycle debug modes");
        tooltip.add("Current debug mode: "  + (mode == null ? "null" : mode.debugName()));
    }

    private DebugMode getDebugMode(ItemStack stack) {
        NBTTagCompound tag;
        if(!stack.hasTagCompound()) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        } else {
            tag = stack.getTagCompound();
        }
        if(!tag.hasKey(Names.NBT.COUNT)) {
            tag.setInteger(Names.NBT.COUNT, 0);
        }
        return DEBUG_MODES.get(tag.getInteger(Names.NBT.COUNT) % DEBUG_MODES.size());
    }

    private DebugMode changeDebugMode(ItemStack stack) {
        NBTTagCompound tag;
        if(!stack.hasTagCompound()) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        } else {
            tag = stack.getTagCompound();
        }
        int index;
        if(!tag.hasKey(Names.NBT.COUNT)) {
            index = 1;
        } else {
            index = (tag.getInteger(Names.NBT.COUNT) + 1 ) % DEBUG_MODES.size();
        }
        tag.setInteger(Names.NBT.COUNT, index);
        return DEBUG_MODES.get(index);
    }
}
