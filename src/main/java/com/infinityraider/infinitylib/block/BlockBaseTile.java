package com.infinityraider.infinitylib.block;

import com.infinityraider.infinitylib.block.multiblock.IMultiBlockComponent;
import com.infinityraider.infinitylib.block.tile.IRotatableTile;
import com.infinityraider.infinitylib.block.tile.TileEntityBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockBaseTile<T extends TileEntityBase> extends BlockBase implements IInfinityBlockWithTile<T> {

    public BlockBaseTile(String name, Material blockMaterial) {
        super(name, blockMaterial);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    /**
     * Sets the block's orientation based upon the direction the player is
     * looking when the block is placed.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        TileEntity te = world.getTileEntity(pos);
        if(te != null && te instanceof TileEntityBase) {
            TileEntityBase tile = (TileEntityBase) world.getTileEntity(pos);
            if(tile instanceof IRotatableTile) {
                EnumFacing dir = entity.getHorizontalFacing();
                ((IRotatableTile) tile).setOrientation(dir.getOpposite());
            }
            if((tile instanceof IMultiBlockComponent) && !world.isRemote) {
                IMultiBlockComponent component = (IMultiBlockComponent) tile;
                component.getMultiBlockManager().onBlockPlaced(world, pos, component);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if((tile instanceof IMultiBlockComponent) && !world.isRemote) {
            IMultiBlockComponent component = (IMultiBlockComponent) tile;
            component.getMultiBlockManager().onBlockBroken(world, pos, component);
        }
        super.breakBlock(world, pos, state);
        world.removeTileEntity(pos);
    }

    @Override
    public boolean eventReceived(IBlockState state, World world, BlockPos pos, int id, int data) {
        super.eventReceived(state, world, pos, id, data);
        TileEntity tileentity = world.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, data);
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        TileEntity tile = world.getTileEntity(pos);
        if(axis.getAxis().isHorizontal() && tile instanceof IRotatableTile) {
            final int offset = axis.getYOffset();
            ((IRotatableTile) tile).incrementRotation(offset);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public abstract T createNewTileEntity(World world, int meta);

    @SuppressWarnings("unchecked")
    public T getTileEntity(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile == null) {
            IBlockState state = world.getBlockState(pos);
            tile = this.createNewTileEntity(world, this.getMetaFromState(state));
            world.setTileEntity(pos, tile);
        }
        return (T) tile;
    }
}
