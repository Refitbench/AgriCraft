/*
 */
package com.infinityraider.infinitylib.item;

import java.util.ArrayList;
import java.util.List;

import com.infinityraider.infinitylib.render.item.ItemModelTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IAutoRenderedItem {
	
	@SideOnly(Side.CLIENT)
	String getModelId(ItemStack stack);
	
	@SideOnly(Side.CLIENT)
	String getBaseTexture(ItemStack stack);
	
	@SideOnly(Side.CLIENT)
	default List<ItemModelTexture> getOverlayTextures(ItemStack stack) {
		return new ArrayList<>();
	}
	
	@SideOnly(Side.CLIENT)
	List<ResourceLocation> getAllTextures();
	
}
