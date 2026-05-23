package com.infinityraider.agricraft.items;

import com.agricraft.agricore.config.AgriConfigCategory;
import com.agricraft.agricore.config.AgriConfigurable;
import com.agricraft.agricore.core.AgriCore;
import com.google.common.collect.ImmutableList;
import com.infinityraider.agricraft.api.v1.AgriApi;
import com.infinityraider.agricraft.api.v1.crop.IAgriCrop;
import com.infinityraider.agricraft.api.v1.items.IAgriTrowelItem;
import com.infinityraider.agricraft.api.v1.seed.AgriSeed;
import com.infinityraider.agricraft.init.AgriBlocks;
import com.infinityraider.agricraft.items.tabs.AgriTabs;
import com.infinityraider.agricraft.reference.AgriCraftConfig;
import com.infinityraider.agricraft.reference.AgriNBT;
import com.infinityraider.agricraft.reference.WaterPadCompatMode;
import com.infinityraider.agricraft.utility.StackHelper;
import com.infinityraider.infinitylib.item.IItemWithModel;
import com.infinityraider.infinitylib.item.ItemBase;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTrowel extends ItemBase implements IAgriTrowelItem, IItemWithModel {

    @AgriConfigurable(
            category = AgriConfigCategory.TOOLS,
            key = "Enable Trowel",
            comment = "Set to false to disable the Trowel."
    )
    public static boolean enableTrowel = true;
    
    @AgriConfigurable(
            category = AgriConfigCategory.TOOLS,
            key = "Trowel Debuff",
            min = "0",
            max = "1000",
            comment = "The number of growth stages a plant loses when moved with the trowel."
    )
    public static int trowelDebuff = 1;

    public ItemTrowel() {
        super("trowel");
        this.maxStackSize = 1;
        this.setCreativeTab(AgriTabs.TAB_AGRICRAFT);
    }

    //I'm overriding this just to be sure
    @Override
    public boolean canItemEditBlocks() {
        return true;
    }

    // this is called when you right click with this item in hand
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitx, float hity, float hitz) {
        ItemStack stack = player.getHeldItem(hand);

        // Pass if water pad compat is enabled with trowel mode, create waterpad.
        final WaterPadCompatMode mode = AgriCraftConfig.getWaterPadCompatMode();
        if (mode.usesTrowel()) {
            final IBlockState state = world.getBlockState(pos);
            if (state.getBlock() == Blocks.FARMLAND && !(world.getTileEntity(pos.up()) instanceof IAgriCrop)) {
                if (!world.isRemote) {
                    world.setBlockState(pos, AgriBlocks.getInstance().WATER_PAD.getDefaultState(), 3);
                    if (!player.capabilities.isCreativeMode) {
                        stack.damageItem(1, player);
                    }
                }
                return EnumActionResult.SUCCESS;
            }
        }

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IAgriCrop) {
            IAgriCrop crop = (IAgriCrop) te;
            Optional<AgriSeed> trowel_seed = AgriApi.getSeedRegistry().valueOf(stack);
            if (crop.isCrossCrop()) {
                // Cross-crops cannot hold seeds, so the trowel cannot extract from or insert into them.
                return EnumActionResult.FAIL;
            } else if (crop.hasSeed() && !trowel_seed.isPresent()) {
                final int growthstage = Math.max(0, crop.getGrowthStage() - trowelDebuff);
                trowel_seed = Optional.ofNullable(crop.getSeed());
                crop.setSeed(null);
                if (trowel_seed.isPresent()) {
                    NBTTagCompound tag = new NBTTagCompound();
                    tag.setString(AgriNBT.SEED, trowel_seed.get().getPlant().getId());
                    tag.setInteger(AgriNBT.LEVEL, growthstage);
                    trowel_seed.get().getStat().writeToNBT(tag);
                    stack.setTagCompound(tag);
                    stack.setItemDamage(1);
                    return EnumActionResult.SUCCESS;
                } else {
                    return EnumActionResult.FAIL;
                }
            } else if (trowel_seed.isPresent() && !crop.hasSeed()) {
                if (crop.setSeed(trowel_seed.get())) {
                    final int growthstage = StackHelper.getTag(stack).getInteger(AgriNBT.LEVEL);
                    crop.setGrowthStage(growthstage);
                    stack.setTagCompound(new NBTTagCompound());
                    stack.setItemDamage(0);
                    return EnumActionResult.SUCCESS;
                } else {
                    return EnumActionResult.FAIL;
                }
            }
        }
        return EnumActionResult.PASS;
    }

    @Override
    public boolean isEnabled() {
        return enableTrowel;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flag) {
        if (AgriCraftConfig.getWaterPadCompatMode().usesTrowel()) {
            tooltip.add(AgriCore.getTranslator().translate("agricraft_tooltip.trowel_waterpad"));
        }
    }

    @Override
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        return ImmutableList.of(
                new Tuple<>(0, new ModelResourceLocation(this.getRegistryName() + "")),
                new Tuple<>(1, new ModelResourceLocation(this.getRegistryName() + "_full"))
        );
    }

}
