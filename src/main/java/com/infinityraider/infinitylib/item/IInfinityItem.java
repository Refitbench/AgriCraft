package com.infinityraider.infinitylib.item;

import com.infinityraider.infinitylib.utility.IToggleable;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;

public interface IInfinityItem extends IToggleable {

	String getInternalName();

    /**
     * This method should be standard implemented in every Item, used for default implementation in sub-interfaces
     */
    ResourceLocation getRegistryName();

	default List<String> getOreTags() {
		return Collections.emptyList();
	}
}
