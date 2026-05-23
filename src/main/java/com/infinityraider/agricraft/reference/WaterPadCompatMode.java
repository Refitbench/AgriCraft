package com.infinityraider.agricraft.reference;

/**
 * Controls how Water Pads can be created via player interaction when in compatibility mode.
 */
public enum WaterPadCompatMode {

    NONE,
    TROWEL,
    SHIFT,
    BOTH;

    /**
     * Whether the shovel event handler is used in Water Pad creation.
     */
    public boolean usesShovel() {
        return this == NONE || this == SHIFT || this == BOTH;
    }

    /**
     * Whether the shovel path requires the player to be sneaking.
     */
    public boolean requiresShift() {
        return this == SHIFT || this == BOTH;
    }

    /**
     * Whether the trowel should be able to create Water Pads.
     */
    public boolean usesTrowel() {
        return this == TROWEL || this == BOTH;
    }

}
