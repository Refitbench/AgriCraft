package com.infinityraider.infinitylib.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Interface used to ease model registering
 */
public interface IItemWithModel extends IInfinityItem {
    /**
     * @return a list with metadata values and ModelResourceLocations corresponding with it.
     */
    @SideOnly(Side.CLIENT)
    default List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        return ImmutableList.of(
                new Tuple<>(0, new ModelResourceLocation(this.getRegistryName().toString(), "inventory"))
        );
    }
}