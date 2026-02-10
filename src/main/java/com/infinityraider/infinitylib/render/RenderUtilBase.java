package com.infinityraider.infinitylib.render;

import com.infinityraider.infinitylib.config.ModConfiguration;
import com.infinityraider.infinitylib.render.tessellation.ITessellator;
import com.infinityraider.infinitylib.render.tessellation.TessellatorVertexBuffer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Utility base class for rendering event handlers
 */
@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public abstract class RenderUtilBase {

    protected RenderUtilBase() {
    }

    public static void drawBlockModel(ITessellator tessellator, IBlockState state) {
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        for (EnumFacing facing : EnumFacing.values()) {
            drawQuads(TessellatorVertexBuffer.getInstance(), model.getQuads(state, facing, 0));
        }
        drawQuads(TessellatorVertexBuffer.getInstance(), model.getQuads(state, null, 0));
    }

    public static void drawQuads(ITessellator tessellator, List<BakedQuad> quads) {
        if (quads.size() > 0) {
            tessellator.startDrawingQuads(quads.get(0).getFormat());
            tessellator.addQuads(quads);
            tessellator.draw();
        }
    }

    public static int getMixedBrightness(World world, BlockPos pos, Block block) {
        return getMixedBrightness(world, pos, world.getBlockState(pos), block);
    }

    public static int getMixedBrightness(World world, BlockPos pos, IBlockState state) {
        return getMixedBrightness(world, pos, state, state.getBlock());
    }

    public static int getMixedBrightness(World world, BlockPos pos, IBlockState state, Block block) {
        return world.getCombinedLight(pos, world.getLightFor(EnumSkyBlock.BLOCK, pos));
    }

    public static void rotateBlock(ITessellator tess, EnumFacing dir) {
        switch (dir) {
            case WEST:
                tess.translate(0.5f, 0, 0.5f);
                tess.rotate(270, 0, 1, 0);
                tess.translate(-0.5f, 0, -0.5f);
                break;
            case NORTH:
                tess.translate(0.5f, 0, 0.5f);
                tess.rotate(180, 0, 1, 0);
                tess.translate(-0.5f, 0, -0.5f);
                break;
            case EAST:
                tess.translate(0.5f, 0, 0.5f);
                tess.rotate(90, 0, 1, 0);
                tess.translate(-0.5f, 0, -0.5f);
                break;
            case DOWN:
                tess.translate(0, 0.5f, 0.5f);
                tess.rotate(90, 1, 0, 0);
                tess.translate(0, -0.5f, -0.5f);
                break;
            case UP:
                tess.translate(0, 0.5f, 0.5f);
                tess.rotate(270, 1, 0, 0);
                tess.translate(0, -0.5f, -0.5f);
                break;
        }
    }

    public static void renderItemStack(ItemStack stack, double x, double y, double z, double scale, boolean rotate) {
        // Save Settings
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Fix Lighting
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableLighting();

        // Translate to correct spot
        GlStateManager.translate(x, y, z);

        // Scale to correct Size
        GlStateManager.scale(scale, scale, scale);

        // Rotate Item as function of system time.
        if (rotate) {
            double angle = (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL); //credits to Pahimar
            GlStateManager.rotate((float) angle, 0, 1, 0);
        }

        // Draw the item.
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);

        // Restore Settings.
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    /**
     * Method to cancel out view bobbing when rendering from RenderHandEvent
     *
     * @param player player
     * @param partialTicks partial tick
     * @param inverse inverse or not
     */
    public static final void correctViewBobbing(EntityPlayer player, float partialTicks, boolean inverse) {
        if (!Minecraft.getMinecraft().gameSettings.viewBobbing) {
            return;
        }
        float f = player.distanceWalkedModified - player.prevDistanceWalkedModified;
        float f1 = -(player.distanceWalkedModified + f * partialTicks);
        float f2 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
        float f3 = player.prevCameraPitch + (player.cameraPitch - player.prevCameraPitch) * partialTicks;
        if (inverse) {
            GlStateManager.translate(MathHelper.sin(f1 * (float) Math.PI) * f2 * 0.5F, -Math.abs(MathHelper.cos(f1 * (float) Math.PI) * f2), 0.0F);
            GlStateManager.rotate(MathHelper.sin(f1 * (float) Math.PI) * f2 * 3.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(Math.abs(MathHelper.cos(f1 * (float) Math.PI - 0.2F) * f2) * 5.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(f3, 1.0F, 0.0F, 0.0F);
        } else {
            GlStateManager.rotate(-f3, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-Math.abs(MathHelper.cos(f1 * (float) Math.PI - 0.2F) * f2) * 5.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-MathHelper.sin(f1 * (float) Math.PI) * f2 * 3.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(-MathHelper.sin(f1 * (float) Math.PI) * f2 * 0.5F, Math.abs(MathHelper.cos(f1 * (float) Math.PI) * f2), 0.0F);
        }
    }

    /**
     * Method to render the coordinate system for the current matrix. Renders three lines with
     * length 1 starting from (0, 0, 0): red line along x axis, green line along y axis and blue
     * line along z axis.
     */
    public static final void renderCoordinateSystemDebug() {
        if(ModConfiguration.getInstance().debug()) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();

            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            for (int i = 0; i <= 16; i++) {
                buffer.pos(((float) i) / 16.0F, 0, 0).color(255, 0, 0, 255).endVertex();
            }
            tessellator.draw();

            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            for (int i = 0; i <= 16; i++) {
                buffer.pos(0, ((float) i) / 16.0F, 0).color(0, 255, 0, 255).endVertex();
            }
            tessellator.draw();

            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            for (int i = 0; i <= 16; i++) {
                buffer.pos(0, 0, ((float) i) / 16.0F).color(0, 0, 255, 255).endVertex();
            }
            tessellator.draw();

            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
        }
    }

    /**
     * Method to fetch a TextureAtlasSprite icon from a Resource Location
     *
     * @param loc ResourceLocation to grab icon from
     * @return the icon
     */
    public static final TextureAtlasSprite getIcon(ResourceLocation loc) {
        if (loc == null) {
            return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
        }
        return ModelLoader.defaultTextureGetter().apply(loc);
    }
}
