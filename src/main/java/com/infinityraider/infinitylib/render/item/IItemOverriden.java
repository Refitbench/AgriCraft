/*
 */
package com.infinityraider.infinitylib.render.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FunctionalInterface
public interface IItemOverriden {

	BakedInfItemSubModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity);

	final class Wrapper extends ItemOverrideList {

		public final IItemOverriden override;

		public Wrapper(IItemOverriden override) {
			super(ImmutableList.of());
			this.override = override;
		}

		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
			return this.override.handleItemState(originalModel, stack, world, entity);
		}

	}

}
