package com.infinityraider.infinitylib.render.block;

import com.infinityraider.infinitylib.block.BlockBase;
import com.infinityraider.infinitylib.block.ICustomRenderedBlockWithTile;
import com.infinityraider.infinitylib.block.tile.TileEntityBase;
import com.infinityraider.infinitylib.render.tessellation.ITessellator;
import com.infinityraider.infinitylib.render.tessellation.TessellatorVertexBuffer;
import com.infinityraider.infinitylib.render.tile.ITesr;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockWithTileRenderer<B extends BlockBase & ICustomRenderedBlockWithTile<T>, T extends TileEntityBase> extends BlockRenderer<B> implements ITesr<T> {

    public BlockWithTileRenderer(ITileRenderingHandler<B, T> renderer) {
        super(renderer);
    }

    @Override
    public ITileRenderingHandler<B, T> getRenderer() {
        return (ITileRenderingHandler<B, T>) super.getRenderer();
    }

    @Override
    public void renderTileEntityAt(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        
        // Fetch the tessellator instance.
        ITessellator tessellator = TessellatorVertexBuffer.getInstance();
        
        // Fetch world information.
        World world = te.getWorld();
        BlockPos pos = te.getPos();

        // Bind the textures used for block rendering.
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        // Save the translation matrix.
        GlStateManager.pushMatrix();
        
        // Translate to the location to render at.
        GlStateManager.translate(x, y, z);
        
        // Reset the tesselator's drawing color.
        tessellator.setColorRGBA(1, 1, 1, 1);

        // Start the tesselator drawing quads.
        tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);

        // Call the block renderer to render the block dynamically.
        this.getRenderer().renderWorldBlockDynamic(tessellator, world, pos, x, y, z, this.getBlock(), te, partialTicks, destroyStage, alpha);

        // Finish tesselator drawing quads.
        tessellator.draw();

        // Restore the translation matrix.
        GlStateManager.popMatrix();

    }

}
