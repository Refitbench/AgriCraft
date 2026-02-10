package com.infinityraider.infinitylib.utility.text;

import com.infinityraider.infinitylib.InfinityLib;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Optional;

public class ItemStackParser {
    /**
     * parses a string into an item stack.
     * String format is domain:item_id:metadata.
     * Metadata is optional, if no metadata is specified, the ItemStack will have OreDictionary.WILDCARD_VALUE as metadata
     *
     * @param string the string
     * @return the parsed ItemStack
     */
    public static Optional<ItemStack> parseItemStack(String string) {
        String[] split = string.split(":");
        if(split.length <= 2) {
            Item item = Item.REGISTRY.getObject(new ResourceLocation(string));
            return item == null ? Optional.empty() : Optional.of(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
        } else {
            Item item = Item.REGISTRY.getObject(new ResourceLocation(split[0] + ":" + split[1]));
            if(item == null) {
                return Optional.empty();
            }
            int meta = -1;
            try {
                meta = Integer.parseInt(split[2]);
            } catch(Exception e) {
                InfinityLib.instance.getLogger().info("[ERROR] Failed parsing of item metadata for " + string);
                InfinityLib.instance.getLogger().printStackTrace(e);
            }
            return meta < 0 ? Optional.empty() : Optional.of(new ItemStack(item, 1, meta));
        }
    }
}
