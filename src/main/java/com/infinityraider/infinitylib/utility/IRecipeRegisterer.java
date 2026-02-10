package com.infinityraider.infinitylib.utility;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Interface used to ease recipe registering.
 */
public interface IRecipeRegisterer {

    /**
     * Registers the recipes associated with this object.
     * 
     * @param registry the registry to register the items to.
     */
    public void registerRecipes(IForgeRegistry<IRecipe> registry);

}
