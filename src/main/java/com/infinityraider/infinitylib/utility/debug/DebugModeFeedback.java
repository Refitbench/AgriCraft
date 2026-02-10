package com.infinityraider.infinitylib.utility.debug;

import com.infinityraider.infinitylib.InfinityLib;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.function.Consumer;
import net.minecraftforge.fml.relauncher.Side;

public class DebugModeFeedback extends DebugMode {

    @Override
    public String debugName() {
        return "feedback";
    }

    @Override
    public void debugActionBlockClicked(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        getDebugData(world, pos, l -> {
            InfinityLib.instance.getLogger().debug(l);
            player.sendMessage(new TextComponentString(l));
        });
    }

    @Override
    public void debugActionClicked(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {}

    @Override
    public void debugActionEntityClicked(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {}

    /**
     * Gets strings representing the debug information for the
     * provided location.
     *
     * @param world the world object
     * @param pos the block position
     * @param consumer a consumer accepting the lines of debug data.
     */
    private void getDebugData(World world, BlockPos pos, Consumer<String> consumer) {
        final Side side = world.isRemote ? Side.CLIENT : Side.SERVER;

        final TileEntity tile = world.getTileEntity(pos);

        consumer.accept("------------------");
        consumer.accept(side + " debug info:");
        consumer.accept("------------------");
        
        consumer.accept("Clicked block at (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")");

        if (tile instanceof IDebuggable) {
            IDebuggable debuggable = (IDebuggable) tile;
            if (side == Side.CLIENT) {
                debuggable.addClientDebugInfo(consumer);
            } else {
                debuggable.addServerDebugInfo(consumer);
            }
        } else {
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            consumer.accept("Block: " + Block.REGISTRY.getNameForObject(block));
            consumer.accept("Meta: " + block.getMetaFromState(state));
        }

        consumer.accept(" ");
    }
}
