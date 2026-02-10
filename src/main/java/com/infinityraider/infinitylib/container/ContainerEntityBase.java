package com.infinityraider.infinitylib.container;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;

import java.util.Objects;

public class ContainerEntityBase<E extends Entity> extends ContainerBase {

    private final E entity;

    public ContainerEntityBase(E entity, InventoryPlayer inventory, int xOffset, int yOffset) {
        super(inventory, xOffset, yOffset);
        // Set the Entity associated with the container.
        this.entity = Objects.requireNonNull(entity, "The Entity associated with an Entity Container may not be null!");
    }

    /**
     * Fetches the Entity associated with this container.
     *
     * @return The Entity associated with this container.
     */
    public E getEntity() {
        return this.entity;
    }
}
