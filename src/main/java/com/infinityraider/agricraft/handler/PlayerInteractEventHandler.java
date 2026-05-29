package com.infinityraider.agricraft.handler;

import com.agricraft.agricore.util.TypeHelper;
import com.infinityraider.agricraft.api.v1.AgriApi;
import com.infinityraider.agricraft.api.v1.crop.IAgriCrop;
import com.infinityraider.agricraft.blocks.BlockCrop;
import com.infinityraider.agricraft.blocks.BlockGrate;
import com.infinityraider.agricraft.init.AgriBlocks;
import com.infinityraider.agricraft.reference.AgriCraftConfig;
import com.infinityraider.agricraft.reference.WaterPadCompatMode;
import com.infinityraider.agricraft.utility.StackHelper;
import com.infinityraider.infinitylib.utility.MessageUtil;
import infinityraider.infinitylib.Tags;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class PlayerInteractEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void handleRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        if (!StackHelper.isValid(stack)) {
            return;
        }

        final Item item = stack.getItem();
        final BlockPos pos = event.getPos();
        final World world = event.getWorld();
        final IBlockState state = world.getBlockState(pos);
        final Block block = state.getBlock();

        // Disable vanilla farming mechanic
        if (AgriCraftConfig.disableVanillaFarming) {
            // Player not holding registered seed
            if (!AgriApi.getSeedRegistry().hasAdapter(stack)) {
                return;
            }
            // Player interacting with agri crop
            if (block instanceof IAgriCrop) {
                return;
            }
            // Extra check performed for IPlantable seeds
            if (item instanceof IPlantable && !block.canSustainPlant(state, world, pos, EnumFacing.UP, (IPlantable) item)) {
                return;
            }
            // Deny placement (or similar interactions)
            event.setUseItem(Event.Result.DENY);
            if (!event.getSide().isClient() && AgriCraftConfig.showDisabledVanillaFarmingWarning) {
                MessageUtil.messagePlayer(event.getEntityPlayer(), "`7Vanilla planting is disabled!`r");
            }
            return; // Short-circuit
        }
        // Water Pad creation mechanic
        // Only perform when player is holding a shovel and interacting with a farmland
        if (TypeHelper.isType(item, ItemSpade.class) && block == Blocks.FARMLAND) {
            final WaterPadCompatMode mode = AgriCraftConfig.getWaterPadCompatMode();
            // In trowel-only mode with compatibility
            if (!mode.usesShovel()) {
                return;
            }
            // In require shift mode the player must be sneaking for the shovel to trigger
            if (mode.requiresShift() && !event.getEntityPlayer().isSneaking()) {
                return;
            }
            // Deny interaction
            event.setCanceled(true);
            event.setCancellationResult(EnumActionResult.SUCCESS);
            if (!event.getSide().isClient()) {
                // Replace with water pad
                world.setBlockState(pos, AgriBlocks.getInstance().WATER_PAD.getDefaultState(), 3);
                // Damage the shovel
                stack.damageItem(1, event.getEntityPlayer());
            }
            return; // Short-circuit
        }
        // Vine placement denier
        // Only perform when player is not shifting, holding vines and interacting with grates
        if (!event.getEntityPlayer().isSneaking() &&
                block instanceof BlockGrate &&
                item == Item.getItemFromBlock(Blocks.VINE)) {
            // setUseBlock not manipulated as BlockGrate#onBlockActivated needs to be called
            event.setUseItem(Event.Result.DENY);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void denyBonemeal(BonemealEvent event) {
        if (!AgriCraftConfig.allowBonemealByHand &&
                event.getBlock().getBlock() instanceof BlockCrop &&
                event.getStack().getItem() == Items.DYE &&
                event.getStack().getItemDamage() == EnumDyeColor.WHITE.getDyeDamage()) {
            event.setCanceled(true);
        }
    }

}
