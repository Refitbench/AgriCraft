package com.infinityraider.infinitylib.block;

import com.infinityraider.infinitylib.item.IInfinityItem;
import com.infinityraider.infinitylib.utility.IToggleable;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;

public interface IInfinityBlock extends IToggleable {

    /**
     * The name used internally as to represent this block, not including the modid qualifier.
     *
     * @return the internal name of the block.
     */
    @Nonnull
    String getInternalName();

    /**
     * Method should be implemented by default in any class extending Block.
     *
     * @return the registry name of the block.
     */
    @Nonnull
    ResourceLocation getRegistryName();

    /**
     * Retrieves a list of all the OreDict tags that this block wishes to associate with by default.
     *
     * @return a list of OreDict tags, or an empty list.
     */
    @Nonnull
    default List<String> getOreTags() {
        return Collections.emptyList();
    }

    /**
     * Retrieves the block's item form, if the block has one.
     *
     * @param <T>
     * @return an optional containing the block's item form, or the empty optional.
     */
    @Nonnull
    default <T extends ItemBlock & IInfinityItem> Optional<T> getItemBlock() {
        return Optional.empty();
    }

}
