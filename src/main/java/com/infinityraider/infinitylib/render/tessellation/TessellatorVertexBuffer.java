package com.infinityraider.infinitylib.render.tessellation;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joml.Vector4f;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class TessellatorVertexBuffer extends TessellatorAbstractBase {

    private static final Map<BufferBuilder, ThreadLocal<TessellatorVertexBuffer>> instances = new HashMap<>();

    private final Tessellator tessellator;
    private final BufferBuilder buffer;

    private TessellatorVertexBuffer(BufferBuilder buffer, Tessellator tessellator) {
        this.buffer = buffer;
        this.tessellator = tessellator;
    }

    public static TessellatorVertexBuffer getInstance() {
        return getInstance(Tessellator.getInstance());
    }

    public static TessellatorVertexBuffer getInstance(Tessellator tessellator) {
        return getInstance(tessellator.getBuffer(), tessellator);
    }

    public static TessellatorVertexBuffer getInstance(BufferBuilder buffer) {
        return getInstance(buffer, null);
    }

    private static TessellatorVertexBuffer getInstance(BufferBuilder buffer, Tessellator tessellator) {
        if (!instances.containsKey(buffer)) {
            instances.put(buffer, new ThreadLocal<>());
        }
        ThreadLocal<TessellatorVertexBuffer> threadLocal = instances.get(buffer);
        TessellatorVertexBuffer tess = threadLocal.get();
        if (tess == null) {
            tess = new TessellatorVertexBuffer(buffer, tessellator);
            threadLocal.set(tess);
        }
        return tess;
    }

    /**
     * @return VertexBuffer object which this is currently tessellating vertices
     * for
     */
    public BufferBuilder getVertexBuffer() {
        return buffer;
    }

    /**
     * Sub delegated method call of the startDrawingQuads() method to ensure
     * correct call chain
     */
    @Override
    protected void onStartDrawingQuadsCall() {
        buffer.begin(GL11.GL_QUADS, getVertexFormat());
    }

    /**
     * Method to get all quads constructed.
     *
     * @return emtpy list, no quads are constructed here
     */
    @Override
    public ImmutableList<BakedQuad> getQuads() {
        return ImmutableList.of();
    }

    /**
     * Sub delegated method call of the draw() method to ensure correct call
     * chain.
     */
    @Override
    protected void onDrawCall() {
        if (tessellator != null) {
            tessellator.draw();
        } else {
            buffer.finishDrawing();
        }
    }

    /**
     * Adds a list of quads to be rendered
     *
     * @param quads list of quads
     */
    @Override
    public void addQuads(List<BakedQuad> quads) {
        for (BakedQuad quad : quads) {
            buffer.addVertexData(transformQuad(quad).getVertexData());
        }
    }

    /**
     * Adds a vertex
     *
     * @param x the x-coordinate for the vertex
     * @param y the y-coordinate for the vertex
     * @param z the z-coordinate for the vertex
     * @param u u value for the vertex
     * @param v v value for the vertex
     */
    @Override
    public void addVertexWithUV(float x, float y, float z, float u, float v) {
        final Vector4f pos = new Vector4f(x, y, z, 1);
        this.transform(pos);
        List<VertexFormatElement> elements = this.getVertexFormat().getElements();
        if(elements.contains(DefaultVertexFormats.POSITION_3F)) {
            buffer.pos(pos.x, pos.y, pos.z);
        }
        if(elements.contains(DefaultVertexFormats.TEX_2F)) {
            buffer.tex(u, v);
        }
        if(elements.contains(DefaultVertexFormats.COLOR_4UB)) {
            buffer.color((int) (this.r * 255), (int) (this.g * 255), (int) (this.b * 255), (int) (this.a * 255));
        }
        if(elements.contains(DefaultVertexFormats.NORMAL_3B)) {
            buffer.normal(normal.x, normal.y, normal.z);
        }
        buffer.endVertex();
    }

    @Override
    protected void applyColorMultiplier(EnumFacing side) {
        // I gave up on transforming the side. This probably is faster anyway...
        final float preMultiplier = getMultiplier(side);
        this.r *= preMultiplier;
        this.g *= preMultiplier;
        this.b *= preMultiplier;
    }

    private float getMultiplier(EnumFacing side) {
        switch (side) {
            case DOWN:
                return 0.5F;
            case NORTH:
            case SOUTH:
                return 0.8F;
            case EAST:
            case WEST:
                return 0.6F;
            default:
                return 1;
        }
    }
}
