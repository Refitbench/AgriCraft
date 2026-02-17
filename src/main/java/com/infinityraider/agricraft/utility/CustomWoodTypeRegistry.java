/*
 */
package com.infinityraider.agricraft.utility;

import com.agricraft.agricore.core.AgriCore;
import com.agricraft.agricore.util.TypeHelper;
import com.infinityraider.agricraft.api.v1.util.FuzzyStack;
import com.infinityraider.agricraft.reference.AgriNBT;
import com.infinityraider.agricraft.reference.AgriProperties;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;

import infinityraider.infinitylib.Tags;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.oredict.OreDictionary;

/**
 * The master registry for the materials a custom wood block can be made of.
 */
public class CustomWoodTypeRegistry {

    public static final CustomWoodType DEFAULT = new CustomWoodType(Blocks.PLANKS.getDefaultState());
    public static final FuzzyStack DEFAULT_STACK = new FuzzyStack(DEFAULT.getStack(), false, true);

    private static final Map<String, CustomWoodType> WOOD_TYPES = new HashMap<>();

    public static void init() {
        if (!WOOD_TYPES.isEmpty()) {
            return;
        }
        for (ItemStack stack : OreDictionary.getOres("plankWood")) {
            Item item = stack.getItem();
            if (item instanceof ItemBlock) {
                ItemBlock itemBlock = (ItemBlock) item;
                int meta = stack.getMetadata();
                Block block = itemBlock.getBlock();
                if (meta == OreDictionary.WILDCARD_VALUE) {
                    // Scan all valid states
                    for (IBlockState state : block.getBlockState().getValidStates()) {
                        CustomWoodType type = new CustomWoodType(state);
                        WOOD_TYPES.put(type.toString(), type);
                    }
                } else {
                    IBlockState state = itemBlock.getBlock().getStateFromMeta(stack.getMetadata());
                    CustomWoodType type = new CustomWoodType(state);
                    WOOD_TYPES.put(type.toString(), type);
                }
            } else {
                AgriCore.getLogger(Tags.MOD_ID).error("Unable to create wood type for {0}!", item.getRegistryName().toString());
            }
        }
    }

    public static Optional<CustomWoodType> getFromStack(ItemStack stack) {
        if (StackHelper.hasKey(stack, AgriNBT.MATERIAL, AgriNBT.MATERIAL_META)) {
            return getFromNbt(stack.getTagCompound());
        } else if (StackHelper.isValid(stack, ItemBlock.class)) {
            final ItemBlock itemBlock = (ItemBlock) stack.getItem();
            return getFromBlockAndMeta(itemBlock.getBlock(), itemBlock.getMetadata(stack));
        } else {
            return Optional.empty();
        }
    }

    public static Optional<CustomWoodType> getFromNbt(NBTTagCompound tag) {
        // The old code was much too annoying, in terms of log spam.
        if (NBTHelper.hasKey(tag, AgriNBT.MATERIAL, AgriNBT.MATERIAL_META)) {
            return getFromIdAndMeta(tag.getString(AgriNBT.MATERIAL), tag.getInteger(AgriNBT.MATERIAL_META));
        } else {
            return Optional.empty();
        }
    }

    public static Optional<CustomWoodType> getFromBlockAndMeta(Block block, int meta) {
        return getFromIdAndMeta(block.getRegistryName().toString(), meta);
    }

    public static Optional<CustomWoodType> getFromIdAndMeta(String id, int meta) {
        return Optional.ofNullable(WOOD_TYPES.get(id + ":" + meta));
    }

    public static Collection<CustomWoodType> getAllTypes() {
        return WOOD_TYPES.values();
    }
    
    public static Optional<CustomWoodType> getFromState(@Nullable IBlockState state) {
        return TypeHelper.cast(state, IExtendedBlockState.class)
                .map(s -> s.getValue(AgriProperties.CUSTOM_WOOD_TYPE));
    }

}
