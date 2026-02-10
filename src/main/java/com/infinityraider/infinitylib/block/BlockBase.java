package com.infinityraider.infinitylib.block;

import com.infinityraider.infinitylib.block.blockstate.InfinityProperty;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;

import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public abstract class BlockBase extends Block implements IInfinityBlock {
    private final String internalName;

    public BlockBase(String name, Material blockMaterial) {
        super(blockMaterial);
        this.internalName = name;
        this.setDefaultState();
    }

    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getInternalName() {
        return this.internalName;
    }

    @Override
    protected final BlockStateContainer createBlockState() {
        InfinityProperty[] propertyArray = this.getPropertyArray();
        IProperty[] properties = new IProperty[propertyArray.length];
        for(int i = 0; i < properties.length; i++) {
            properties[i] = propertyArray[i].getProperty();
        }
        IUnlistedProperty[] uprops = getUnlistedPropertyArray();
        if (uprops.length < 1) {
            return new BlockStateContainer(this, properties);
        } else {
            return new ExtendedBlockState(this, properties, uprops);
        }
    }

    private void setDefaultState() {
        IBlockState state = this.blockState.getBaseState();
        for(InfinityProperty property : this.getPropertyArray()) {
            state = property.applyToBlockState(state);
        }
        this.setDefaultState(state);
    }

    /**
     * @return a property array containing all properties for this block's state
     */
    protected abstract InfinityProperty[] getPropertyArray();
    
    /**
     * @return a property array containing all properties for this block's state
     */
    protected IUnlistedProperty[] getUnlistedPropertyArray() {
        return new IUnlistedProperty[0];
    }

    @Override
    @SuppressWarnings("deprecation")
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
