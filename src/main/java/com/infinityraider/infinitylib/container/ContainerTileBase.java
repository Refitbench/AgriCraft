package com.infinityraider.infinitylib.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

import java.util.Objects;

public class ContainerTileBase<T extends TileEntity> extends ContainerBase {

    private final T tile;

    public ContainerTileBase(T tile, InventoryPlayer inventory, int xOffset, int yOffset) {
        super(inventory, xOffset, yOffset);
        // Set the TileEntity associated with the container.
        this.tile = Objects.requireNonNull(tile, "The TileEntity associated with a TileEntity Container may not be null!");
    }

    /**
     * Fetches the TileEntity associated with this container.
     *
     * @return The TileEntity associated with this container.
     */
    public T getTile() {
        return this.tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return !getTile().isInvalid();
    }
}
