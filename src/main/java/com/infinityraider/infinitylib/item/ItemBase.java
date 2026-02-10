package com.infinityraider.infinitylib.item;

import net.minecraft.item.Item;

import java.util.Collections;
import java.util.List;

public abstract class ItemBase extends Item implements IInfinityItem {

	private final String internalName;

	public ItemBase(String name) {
		super();
		this.internalName = name;
	}

	public boolean isEnabled() {
		return true;
	}

	public String getInternalName() {
		return internalName;
	}

	public List<String> getIgnoredNBT() {
		return Collections.emptyList();
	}
    
}
