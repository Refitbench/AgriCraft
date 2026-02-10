/*
 */
package com.infinityraider.infinitylib.utility;

import com.google.common.base.Preconditions;
import java.util.Objects;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 *
 */
public class HashableBlockState {

    @Nonnull
    private final Block block;
    @Nullable
    private final EnumFacing side;
    @Nonnull
    private final ImmutableMap<IProperty<?>, Comparable<?>> listedProperties;
    @Nonnull
    private final ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties;

    public HashableBlockState(@Nonnull IBlockState state) {
        this(state, null);
    }

    public HashableBlockState(@Nonnull IBlockState state, @Nullable EnumFacing side) {
        this(
                state.getBlock(),
                state.getProperties(),
                (state instanceof IExtendedBlockState) ? ((IExtendedBlockState) state).getUnlistedProperties() : ImmutableMap.of(),
                side
        );
    }

    public HashableBlockState(
            @Nonnull Block block,
            @Nonnull ImmutableMap<IProperty<?>, Comparable<?>> listedProperties,
            @Nonnull ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties,
            @Nullable EnumFacing side
    ) {
        this.block = Preconditions.checkNotNull(block);
        this.listedProperties = Preconditions.checkNotNull(listedProperties);
        this.unlistedProperties = Preconditions.checkNotNull(unlistedProperties);
        this.side = side;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HashableBlockState) {
            final HashableBlockState other = (HashableBlockState) obj;
            return Objects.equals(this.block, other.block)
                    && Objects.equals(this.side, other.side)
                    && Objects.equals(this.listedProperties, other.listedProperties)
                    && Objects.equals(this.unlistedProperties, other.unlistedProperties);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(block);
        hash = 31 * hash + Objects.hashCode(side);
        hash = 31 * hash + Objects.hashCode(listedProperties);
        hash = 31 * hash + Objects.hashCode(unlistedProperties);
        return hash;
    }

}
