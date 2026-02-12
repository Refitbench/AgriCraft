package com.infinityraider.agricraft.crafting;

import com.infinityraider.agricraft.utility.CustomWoodType;
import infinityraider.infinitylib.Tags;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;

public class CustomWoodShapedRecipe extends ShapedRecipes {

    public CustomWoodShapedRecipe(CustomWoodType type, String name, NonNullList<Ingredient> ingredients, ItemStack result) {
        super("", 3, 3, ingredients, result);
        this.setRegistryName(Tags.MOD_ID, (type + "_" + name).replaceAll(":", "_"));
    }

}
