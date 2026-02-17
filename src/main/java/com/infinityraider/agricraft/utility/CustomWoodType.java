package com.infinityraider.agricraft.utility;

import com.agricraft.agricore.core.AgriCore;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.infinityraider.agricraft.reference.AgriNBT;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import infinityraider.infinitylib.Tags;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Class representing possible custom wood types.
 *
 * This class is candidate for a rewrite/cleaning.
 */
public class CustomWoodType {

    private final IBlockState state;
    @Nullable
    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite texture;

    protected CustomWoodType(@Nonnull Block block, int meta) {
        this.state = Preconditions.checkNotNull(block).getStateFromMeta(meta);
    }

    CustomWoodType(IBlockState state) {
        this.state = state;
    }

    public Block getBlock() {
        return state.getBlock();
    }

    public int getMeta() {
        return getBlock().getMetaFromState(state);
    }

    public IBlockState getState() {
        return state;
    }

    public ItemStack getStack() {
        return new ItemStack(getBlock(), 1, getMeta());
    }

    // TODO: Address NPE possibility.
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString(AgriNBT.MATERIAL, this.getBlock().getRegistryName().toString());
        tag.setInteger(AgriNBT.MATERIAL_META, this.getMeta());
        return tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof CustomWoodType) {
            CustomWoodType other = (CustomWoodType) obj;
            return other.getBlock() == this.getBlock() && other.getMeta() == this.getMeta();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getBlock(), this.getMeta());
    }

    @Override
    public String toString() {
        return getBlock().getRegistryName() + ":" + getMeta();
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    public TextureAtlasSprite getIcon() {
        if (texture == null) {
            try {
                texture = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
            } catch (Exception e) {
                AgriCore.getLogger(Tags.MOD_ID).debug("Unable to load texture for custom wood block {0}!", getBlock().getRegistryName() + "@" + getMeta());
                AgriCore.getLogger(Tags.MOD_ID).trace(e);
                texture = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
            }
        }
        return texture;
    }

}
