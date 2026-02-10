package com.infinityraider.infinitylib.utility;

/**
 * Interface used to ease recipe registering.
 * 
 * @deprecated since 0.15.0, due to changes in the Minecraft recipe system. Using JSON recipes instead is now recommended.
 */
@Deprecated
public interface IRecipeRegister {

    /**
     * Registers the recipes associated with this object.
     */
    public void registerRecipes();

}
