package com.infinityraider.agricraft.blocks.irrigation;

import com.infinityraider.agricraft.crafting.CustomWoodShapedRecipe;
import com.infinityraider.agricraft.init.AgriBlocks;
import com.infinityraider.agricraft.items.blocks.ItemBlockCustomWood;
import com.infinityraider.agricraft.renderers.blocks.RenderChannelFull;
import com.infinityraider.agricraft.tiles.irrigation.TileEntityChannelFull;
import java.util.Optional;

import com.infinityraider.agricraft.utility.CustomWoodType;
import com.infinityraider.agricraft.utility.CustomWoodTypeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class BlockWaterChannelFull extends AbstractBlockWaterChannel<TileEntityChannelFull> {

    private final ItemBlockCustomWood itemBlock;

    public BlockWaterChannelFull() {
        super("full");
        this.itemBlock = new ItemBlockCustomWood(this);
    }

    @Override
    public Optional<ItemBlockCustomWood> getItemBlock() {
        return Optional.of(this.itemBlock);
    }
    
    @Override
    public TileEntityChannelFull createNewTileEntity(World world, int meta) {
        return new TileEntityChannelFull();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RenderChannelFull getRenderer() {
        return new RenderChannelFull(this);
    }

    @Override
    public void registerRecipes(IForgeRegistry<IRecipe> registry) {
        for (CustomWoodType type : CustomWoodTypeRegistry.getAllTypes()) {
            NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.EMPTY);

            for (int i = 0; i <= 8; i++) {
                if (i % 2 != 0) {
                    ItemStack stack = new ItemStack(this, 1, 0);
                    stack.setTagCompound(type.writeToNBT(new NBTTagCompound()));
                    ingredients.set(i, Ingredient.fromStacks(stack));
                }
            }

            ItemStack result = new ItemStack(AgriBlocks.getInstance().CHANNEL, 4, 0);
            result.setTagCompound(type.writeToNBT(new NBTTagCompound()));

            registry.register(new CustomWoodShapedRecipe("full_to_normal_" + type.getMeta(), ingredients, result));
        }
    }

}
