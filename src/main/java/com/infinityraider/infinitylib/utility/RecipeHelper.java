package com.infinityraider.infinitylib.utility;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import java.util.Iterator;

public abstract class RecipeHelper {

    public static void removeRecipe(ItemStack stack) {
        Iterator<IRecipe> iterator = CraftingManager.REGISTRY.iterator();
        while (iterator.hasNext()) {
            IRecipe recipe = iterator.next();
            ItemStack result = recipe.getRecipeOutput();
            if (result.getItem().equals(stack.getItem()) && result.getItemDamage() == stack.getItemDamage()) {
                iterator.remove();
            }
        }
    }

}
