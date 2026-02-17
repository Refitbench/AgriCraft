package com.infinityraider.agricraft.blocks.irrigation;

import com.infinityraider.agricraft.api.v1.misc.IAgriConnectable;
import com.infinityraider.agricraft.api.v1.util.AgriSideMetaMatrix;
import com.infinityraider.agricraft.blocks.BlockCustomWood;
import com.infinityraider.agricraft.items.blocks.ItemBlockCustomWood;
import com.infinityraider.agricraft.reference.AgriCraftConfig;
import com.infinityraider.agricraft.renderers.blocks.RenderTank;
import com.infinityraider.agricraft.tiles.irrigation.TileEntityTank;
import com.infinityraider.agricraft.utility.CustomWoodType;
import com.infinityraider.infinitylib.utility.WorldHelper;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockWaterTank extends BlockCustomWood<TileEntityTank> {

    protected static final AxisAlignedBB AABB_BASE = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
    protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);

    private final ItemBlockCustomWood itemBlock;

    public BlockWaterTank() {
        super("water_tank");
        this.setTickRandomly(false);
        this.itemBlock = new ItemBlockCustomWood(this);
    }

    @Override
    public Optional<ItemBlockCustomWood> getItemBlock() {
        return Optional.of(this.itemBlock);
    }

    @Override
    public TileEntityTank createNewTileEntity(World world, int meta) {
        return new TileEntityTank();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        // Attempt to interact with fluid stuff.
        FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);
        // Figure out if this is a fluid thing or not.
        final ItemStack stack = player.getHeldItem(hand);
        final FluidStack fluid = FluidUtil.getFluidContained(stack);
        // Return depending if holding a fluid stack to prevent accidental water placement.
        return (fluid != null);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        // Call supermethod.
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        // Update tile.
        WorldHelper.getTile(world, pos, IAgriConnectable.class)
                .ifPresent(IAgriConnectable::refreshConnections);
        // Update neighbors.
        WorldHelper.getTileNeighbors(world, pos, IAgriConnectable.class)
                .forEach(IAgriConnectable::refreshConnections);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        WorldHelper.getTile(world, pos, IAgriConnectable.class)
                .ifPresent(IAgriConnectable::refreshConnections);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        // Call Super Method.
        super.breakBlock(world, pos, state);

        // Notify neighbors.
        WorldHelper.getTileNeighbors(world, pos, IAgriConnectable.class)
                .forEach(IAgriConnectable::refreshConnections);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RenderTank getRenderer() {
        return new RenderTank(this);
    }

    @Override
    protected void addUnlistedProperties(Consumer<IUnlistedProperty> consumer) {
        super.addUnlistedProperties(consumer);
        AgriSideMetaMatrix.addUnlistedProperties(consumer);
    }

    @Override
    protected IExtendedBlockState getExtendedCustomWoodState(IExtendedBlockState state, Optional<TileEntityTank> tile) {
        return tile.map(IAgriConnectable::getConnections).orElseGet(AgriSideMetaMatrix::new).writeToBlockState(state);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public boolean isEnabled() {
        return AgriCraftConfig.enableIrrigation;
    }

    @Override
    protected void getRecipePattern(CustomWoodType type, NonNullList<ItemStack> stacks) {
        stacks.set(0, type.getStack());
        stacks.set(2, type.getStack());
        stacks.set(3, type.getStack());
        stacks.set(5, type.getStack());
        stacks.set(6, type.getStack());
        stacks.set(7, type.getStack());
        stacks.set(8, type.getStack());
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        if (worldIn.getBlockState(pos.offset(EnumFacing.DOWN)) != state) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BASE);
        }
        if (worldIn.getBlockState(pos.offset(EnumFacing.WEST)) != state) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_WEST);
        }
        if (worldIn.getBlockState(pos.offset(EnumFacing.NORTH)) != state) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_NORTH);
        }
        if (worldIn.getBlockState(pos.offset(EnumFacing.EAST)) != state) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_EAST);
        }
        if (worldIn.getBlockState(pos.offset(EnumFacing.SOUTH)) != state) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_SOUTH);
        }
    }

}
