package com.infinityraider.infinitylib.render.tessellation;

import com.infinityraider.infinitylib.reference.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public abstract class TessellatorAbstractBase implements ITessellator {

    /**
     * Default color (white)
     */
    public static final float STANDARD_COLOR = 1.0f;

    /**
     * Default brightness (max)
     */
    public static final int STANDARD_BRIGHTNESS = 15 << 24;

    /**
     * Default normal (up)
     */
    public static final Vector3f STANDARD_NORMAL = new Vector3f(0, 1, 0);

    /**
     * Current transformation matrix
     */
    protected final Matrix4fStack matrices;

    /**
     * Current vertex format
     */
    protected VertexFormat format;

    /**
     * Current normal
     */
    protected final Vector3f normal;

    /**
     * Current color
     */
    protected float r, g, b, a;

    /**
     * Current brightness value
     */
    protected int l;

    /**
     * Current tint index for the quad
     */
    protected int tintIndex;

    /**
     * Current diffuse lighting setting for the quad
     */
    protected boolean applyDiffuseLighting;

    protected TessellatorAbstractBase() {
        this.matrices = new Matrix4fStack(64);
        this.normal = new Vector3f(STANDARD_NORMAL);
        this.tintIndex = -1;
        this.r = STANDARD_COLOR;
        this.g = STANDARD_COLOR;
        this.b = STANDARD_COLOR;
        this.a = STANDARD_COLOR;
        this.applyDiffuseLighting = false;
    }

    @Override
    public final void startDrawingQuads(VertexFormat vertexFormat) {
        this.format = vertexFormat;
        this.onStartDrawingQuadsCall();
    }

    /**
     * Sub delegated method call of the startDrawingQuads() method to ensure
     * correct call chain
     */
    protected abstract void onStartDrawingQuadsCall();

    @Override
    public final void draw() {
        this.onDrawCall();
        this.format = null;
        this.normal.set(STANDARD_NORMAL);
        this.setColorRGBA(STANDARD_COLOR, STANDARD_COLOR, STANDARD_COLOR, STANDARD_COLOR);
        this.setBrightness(STANDARD_BRIGHTNESS);
        this.tintIndex = -1;
        this.applyDiffuseLighting = false;
        this.matrices.clear();
    }

    /**
     * Sub delegated method call of the draw() method to ensure correct call
     * chain
     */
    protected abstract void onDrawCall();

    @Override
    public final VertexFormat getVertexFormat() {
        return format;
    }

    @Override
    public void pushMatrix() {
        this.matrices.pushMatrix();
    }

    @Override
    public void popMatrix() {
        this.matrices.popMatrix();
    }

    @Override
    public void addVertexWithUV(float x, float y, float z, TextureAtlasSprite icon, float u, float v) {
        if (icon == null) {
            icon = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
        }
        this.addVertexWithUV(x, y, z, icon.getInterpolatedU(u), icon.getInterpolatedV(v));
    }

    @Override
    public void addScaledVertexWithUV(float x, float y, float z, float u, float v) {
        addVertexWithUV(x * Constants.UNIT, y * Constants.UNIT, z * Constants.UNIT, u * Constants.UNIT, v * Constants.UNIT);
    }

    @Override
    public void addScaledVertexWithUV(float x, float y, float z, TextureAtlasSprite icon, float u, float v) {
        addVertexWithUV(x * Constants.UNIT, y * Constants.UNIT, z * Constants.UNIT, icon, u, v);
    }

    @Override
    public void drawScaledFace(float minX, float minY, float maxX, float maxY, EnumFacing face, float offset) {
        float x1, x2, x3, x4;
        float y1, y2, y3, y4;
        float z1, z2, z3, z4;
        float u1, u2, u3, u4;
        float v1, v2, v3, v4;
        final int uv = 17;
        switch (face) {
            case UP: {
                x1 = x4 = maxX;
                x2 = x3 = minX;
                z1 = z2 = minY;
                z3 = z4 = maxY;
                y1 = y2 = y3 = y4 = offset;
                u2 = u3 = (minX % uv);
                u1 = u4 = (maxX % uv);
                v3 = v4 = maxY % uv;
                v1 = v2 = minY % uv;
                break;
            }
            case DOWN: {
                x1 = x2 = maxX;
                x3 = x4 = minX;
                z1 = z4 = minY;
                z2 = z3 = maxY;
                y1 = y2 = y3 = y4 = offset;
                u1 = u2 = maxX % uv;
                u3 = u4 = minX % uv;
                v1 = v4 = 16 - (minY % uv);
                v2 = v3 = 16 - (maxY % uv);
                break;
            }
            case WEST: {
                z1 = z2 = maxX;
                z3 = z4 = minX;
                y1 = y4 = minY;
                y2 = y3 = maxY;
                x1 = x2 = x3 = x4 = offset;
                u1 = u2 = maxX % uv;
                u3 = u4 = minX % uv;
                v1 = v4 = (16 - minY % uv);
                v2 = v3 = (16 - maxY % uv);
                break;
            }
            case EAST: {
                z1 = z2 = minX;
                z3 = z4 = maxX;
                y1 = y4 = minY;
                y2 = y3 = maxY;
                x1 = x2 = x3 = x4 = offset;
                u1 = u2 = (16 - minX % uv);
                u3 = u4 = (16 - maxX % uv);
                v1 = v4 = (16 - minY % uv);
                v2 = v3 = (16 - maxY % uv);
                break;
            }
            case NORTH: {
                x1 = x2 = maxX;
                x3 = x4 = minX;
                y1 = y4 = maxY;
                y2 = y3 = minY;
                z1 = z2 = z3 = z4 = offset;
                u1 = u2 = (16 - maxX % uv);
                u3 = u4 = (16 - minX % uv);
                v1 = v4 = (16 - maxY % uv);
                v2 = v3 = (16 - minY % uv);
                break;
            }
            case SOUTH: {
                x1 = x2 = maxX;
                x3 = x4 = minX;
                y1 = y4 = minY;
                y2 = y3 = maxY;
                z1 = z2 = z3 = z4 = offset;
                u1 = u2 = maxX % uv;
                u3 = u4 = minX % uv;
                v1 = v4 = (16 - minY % uv);
                v2 = v3 = (16 - maxY % uv);
                break;
            }
            default:
                return;
        }
        float rPrev = this.r;
        float gPrev = this.g;
        float bPrev = this.b;
        float aPrev = this.a;
        this.applyColorMultiplier(face);
        this.setNormal(face.getXOffset(), face.getYOffset(), face.getZOffset());
        addScaledVertexWithUV(x1, y1, z1, u1, v1);
        addScaledVertexWithUV(x2, y2, z2, u2, v2);
        addScaledVertexWithUV(x3, y3, z3, u3, v3);
        addScaledVertexWithUV(x4, y4, z4, u4, v4);
        this.setColorRGBA(rPrev, gPrev, bPrev, aPrev);
    }

    @Override
    public void drawScaledFace(float minX, float minY, float maxX, float maxY, EnumFacing face, TextureAtlasSprite icon, float offset) {
        float x1, x2, x3, x4;
        float y1, y2, y3, y4;
        float z1, z2, z3, z4;
        float u1, u2, u3, u4;
        float v1, v2, v3, v4;
        final int uv = 17;
        switch (face) {
            case UP: {
                x1 = x4 = maxX;
                x2 = x3 = minX;
                z1 = z2 = minY;
                z3 = z4 = maxY;
                y1 = y2 = y3 = y4 = offset;
                u2 = u3 = (minX % uv);
                u1 = u4 = (maxX % uv);
                v3 = v4 = maxY % uv;
                v1 = v2 = minY % uv;
                break;
            }
            case DOWN: {
                x1 = x2 = maxX;
                x3 = x4 = minX;
                z1 = z4 = minY;
                z2 = z3 = maxY;
                y1 = y2 = y3 = y4 = offset;
                u1 = u2 = maxX % uv;
                u3 = u4 = minX % uv;
                v1 = v4 = 16 - (minY % uv);
                v2 = v3 = 16 - (maxY % uv);
                break;
            }
            case WEST: {
                z1 = z2 = maxX;
                z3 = z4 = minX;
                y1 = y4 = minY;
                y2 = y3 = maxY;
                x1 = x2 = x3 = x4 = offset;
                u1 = u2 = maxX % uv;
                u3 = u4 = minX % uv;
                v1 = v4 = (16 - minY % uv);
                v2 = v3 = (16 - maxY % uv);
                break;
            }
            case EAST: {
                z1 = z2 = minX;
                z3 = z4 = maxX;
                y1 = y4 = minY;
                y2 = y3 = maxY;
                x1 = x2 = x3 = x4 = offset;
                u1 = u2 = (16 - minX % uv);
                u3 = u4 = (16 - maxX % uv);
                v1 = v4 = (16 - minY % uv);
                v2 = v3 = (16 - maxY % uv);
                break;
            }
            case NORTH: {
                x1 = x2 = maxX;
                x3 = x4 = minX;
                y1 = y4 = maxY;
                y2 = y3 = minY;
                z1 = z2 = z3 = z4 = offset;
                u1 = u2 = (16 - maxX % uv);
                u3 = u4 = (16 - minX % uv);
                v1 = v4 = (16 - maxY % uv);
                v2 = v3 = (16 - minY % uv);
                break;
            }
            case SOUTH: {
                x1 = x2 = maxX;
                x3 = x4 = minX;
                y1 = y4 = minY;
                y2 = y3 = maxY;
                z1 = z2 = z3 = z4 = offset;
                u1 = u2 = maxX % uv;
                u3 = u4 = minX % uv;
                v1 = v4 = (16 - minY % uv);
                v2 = v3 = (16 - maxY % uv);
                break;
            }
            default:
                return;
        }
        float rPrev = this.r;
        float gPrev = this.g;
        float bPrev = this.b;
        float aPrev = this.a;
        this.applyColorMultiplier(face);
        this.setNormal(face.getXOffset(), face.getYOffset(), face.getZOffset());
        addScaledVertexWithUV(x1, y1, z1, icon, u1, v1);
        addScaledVertexWithUV(x2, y2, z2, icon, u2, v2);
        addScaledVertexWithUV(x3, y3, z3, icon, u3, v3);
        addScaledVertexWithUV(x4, y4, z4, icon, u4, v4);
        this.setColorRGBA(rPrev, gPrev, bPrev, aPrev);
    }

    @Override
    public void drawScaledFaceDouble(float minX, float minY, float maxX, float maxY, EnumFacing face, float offset) {
        if(face == null) {
            return;
        }
        EnumFacing opposite = face.getOpposite();
        this.drawScaledFace(minX, minY, maxX, maxY, face, offset);
        this.drawScaledFace(minX, minY, maxX, maxY, opposite, offset);
    }

    @Override
    public void drawScaledFaceDouble(float minX, float minY, float maxX, float maxY, EnumFacing face, TextureAtlasSprite icon, float offset) {
        if(face == null) {
            return;
        }
        EnumFacing opposite = face.getOpposite();
        this.drawScaledFace(minX, minY, maxX, maxY, face, icon, offset);
        this.drawScaledFace(minX, minY, maxX, maxY, opposite, icon, offset);
    }

    @Override
    public void drawScaledPrism(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        //bottom
        drawScaledFace(minX, minZ, maxX, maxZ, EnumFacing.DOWN, minY);
        //top
        drawScaledFace(minX, minZ, maxX, maxZ, EnumFacing.UP, maxY);
        //north
        drawScaledFace(minX, minY, maxX, maxY, EnumFacing.NORTH, minZ);
        //south
        drawScaledFace(minX, minY, maxX, maxY, EnumFacing.SOUTH, maxZ);
        //west
        drawScaledFace(minZ, minY, maxZ, maxY, EnumFacing.WEST, minX);
        //east
        drawScaledFace(minZ, minY, maxZ, maxY, EnumFacing.EAST, maxX);
    }

    @Override
    public void drawScaledPrism(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, TextureAtlasSprite icon) {
        //bottom
        drawScaledFace(minX, minZ, maxX, maxZ, EnumFacing.DOWN, icon, minY);
        //top
        drawScaledFace(minX, minZ, maxX, maxZ, EnumFacing.UP, icon, maxY);
        //north
        drawScaledFace(minX, minY, maxX, maxY, EnumFacing.NORTH, icon, minZ);
        //south
        drawScaledFace(minX, minY, maxX, maxY, EnumFacing.SOUTH, icon, maxZ);
        //west
        drawScaledFace(minZ, minY, maxZ, maxY, EnumFacing.WEST, icon, minX);
        //east
        drawScaledFace(minZ, minY, maxZ, maxY, EnumFacing.EAST, icon, maxX);
    }

    @Override
    public void drawScaledCylinder(float x, float y, float z, float r, float h, float vMax,int quads) {
        this.drawScaledCylinderOutside(x, y, z, r, h, vMax, quads);
        this.drawScaledCylinderInside(x, y, z, r, h, vMax, quads);
    }

    @Override
    public void drawScaledCylinder(float x, float y, float z, float r, float h, TextureAtlasSprite texture, float vMax, int quads) {
        this.drawScaledCylinderOutside(x, y, z, r, h, texture, vMax, quads);
        this.drawScaledCylinderInside(x, y, z, r, h, texture, vMax, quads);
    }

    @Override
    public void drawScaledCylinderOutside(float x, float y, float z, float r, float h, float vMax, int quads) {
        float prevX = x + r;
        float prevZ = z;
        float prevU = 0;
        for(int i = 0; i < quads; i ++) {
            double angle = ((i+1)%quads)*2*Math.PI/quads;
            float newX = (float) (r*Math.cos(angle)) + x;
            float newZ = (float) (r*Math.sin(angle)) + z;
            float newU = Constants.WHOLE*((float) (i+1))/quads;
            this.addScaledVertexWithUV(prevX, y, prevZ, prevU, vMax);
            this.addScaledVertexWithUV(prevX, y + h, prevZ, prevU, 0);
            this.addScaledVertexWithUV(newX, y + h, newZ, newU, 0);
            this.addScaledVertexWithUV(newX, y, newZ, newU, vMax);
            prevX = newX;
            prevZ = newZ;
            prevU = newU;
        }
    }

    @Override
    public void drawScaledCylinderOutside(float x, float y, float z, float r, float h, TextureAtlasSprite texture, float vMax, int quads) {
        float prevX = x + r;
        float prevZ = z;
        float prevU = 0;
        for(int i = 0; i < quads; i ++) {
            double angle = ((i+1)%quads)*2*Math.PI/quads;
            float newX = (float) (r*Math.cos(angle)) + x;
            float newZ = (float) (r*Math.sin(angle)) + z;
            float newU = Constants.WHOLE*((float) (i+1))/quads;
            this.addScaledVertexWithUV(prevX, y, prevZ, texture, prevU, vMax);
            this.addScaledVertexWithUV(prevX, y + h, prevZ, texture, prevU, 0);
            this.addScaledVertexWithUV(newX, y + h, newZ, texture, newU, 0);
            this.addScaledVertexWithUV(newX, y, newZ, texture, newU, vMax);
            prevX = newX;
            prevZ = newZ;
            prevU = newU;
        }
    }

    @Override
    public void drawScaledCylinderInside(float x, float y, float z, float r, float h, float vMax, int quads) {
        float prevX = x + r;
        float prevZ = z;
        float prevU = 0;
        for(int i = 0; i < quads; i ++) {
            double angle = ((i+1)%quads)*2*Math.PI/quads;
            float newX = (float) (r*Math.cos(angle)) + x;
            float newZ = (float) (r*Math.sin(angle)) + z;
            float newU = Constants.WHOLE*((float) (i+1))/quads;
            this.addScaledVertexWithUV(prevX, y, prevZ, prevU, vMax);
            this.addScaledVertexWithUV(newX, y, newZ, newU, vMax);
            this.addScaledVertexWithUV(newX, y + h, newZ, newU, 0);
            this.addScaledVertexWithUV(prevX, y + h, prevZ, prevU, 0);
            prevX = newX;
            prevZ = newZ;
            prevU = newU;
        }
    }

    @Override
    public void drawScaledCylinderInside(float x, float y, float z, float r, float h, TextureAtlasSprite texture, float vMax, int quads) {
        float prevX = x + r;
        float prevZ = z;
        float prevU = 0;
        for(int i = 0; i < quads; i ++) {
            double angle = ((i+1)%quads)*2*Math.PI/quads;
            float newX = (float) (r*Math.cos(angle)) + x;
            float newZ = (float) (r*Math.sin(angle)) + z;
            float newU = Constants.WHOLE*((float) (i+1))/quads;
            this.addScaledVertexWithUV(prevX, y, prevZ, texture, prevU, vMax);
            this.addScaledVertexWithUV(newX, y, newZ, texture, newU, vMax);
            this.addScaledVertexWithUV(newX, y + h, newZ, texture, newU, 0);
            this.addScaledVertexWithUV(prevX, y + h, prevZ, texture, prevU, 0);
            prevX = newX;
            prevZ = newZ;
            prevU = newU;
        }
    }

    @Override
    public void translate(BlockPos pos) {
        this.translate(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void translate(float x, float y, float z) {
        this.matrices.translate(x, y, z);
    }

    @Override
    public void rotate(float angle, float x, float y, float z) {
        this.matrices.rotate((float)Math.toRadians(angle), x, y, z);
    }

    @Override
    public void scale(float x, float y, float z) {
        this.matrices.scale(x, y, z);
    }

    @Override
    public TextureAtlasSprite getIcon(ResourceLocation loc) {
        if (loc != null) {
            return ModelLoader.defaultTextureGetter().apply(loc);
        } else {
            return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
        }
    }

    @Override
    public void bindTexture(ResourceLocation loc) {
        Minecraft.getMinecraft().renderEngine.bindTexture(loc);
    }

    @Override
    public void setNormal(float x, float y, float z) {
        this.normal.set(x, y, z);
    }

    @Override
    public void setNormal(Vector3f vec) {
        this.normal.set(vec);
    }

    @Override
    public Vector3f getNormal() {
        return this.normal;
    }

    @Override
    public int getColor() {
        return ((int)(this.r * 255) << 16) | ((int)(this.g * 255) << 8) | ((int)(this.b * 255));
    }

    @Override
    public void setColorRGB(float red, float green, float blue) {
        this.setColorRGBA(red, green, blue, 1);
    }

    @Override
    public void setColorRGBA(float red, float green, float blue, float alpha) {
        this.r = red;
        this.g = green;
        this.b = blue;
        this.a = alpha;
    }

    @Override
    public void setAlpha(float alpha) {
        this.a = alpha;
    }

    @Override
    public float getRed() {
        return this.r;
    }

    @Override
    public float getGreen() {
        return this.g;
    }

    @Override
    public float getBlue() {
        return this.b;
    }

    @Override
    public float getAlpha() {
        return this.a;
    }

    @Override
    public void setBrightness(int value) {
        this.l = value;
    }

    /**
     * Gets the brightness of the tessellator
     *
     * @return the brightness value
     */
    @Override
    public int getBrightness() {
        return this.l;
    }

    @Override
    public void setTintIndex(int index) {
        this.tintIndex = index;
    }

    @Override
    public int getTintIndex() {
        return this.tintIndex;
    }

    @Override
    public void setApplyDiffuseLighting(boolean value) {
        this.applyDiffuseLighting = value;
    }

    @Override
    public boolean getApplyDiffuseLighting() {
        return this.applyDiffuseLighting;
    }

    protected abstract void applyColorMultiplier(EnumFacing side);

    @Override
    public final void transform(Vector4f pos) {
        this.matrices.transform(pos);
    }

    public final UnpackedBakedQuad transformQuad(BakedQuad quad) {
        // Fetch required information
        final VertexFormat format = quad.getFormat();
        final float[][][] vertexData = new float[4][format.getElementCount()][4];

        // Objects to be reused in the loop
        final Vector4f temp = new Vector4f();

        //unpack and transform vertex data
        for (int v = 0; v < 4; v++) {
            for (int e = 0; e < format.getElementCount(); e++) {
                LightUtil.unpack(quad.getVertexData(), vertexData[v][e], format, v, e);
                transformUnpackedVertexDataElement(format.getElement(e).getUsage(), vertexData[v][e], temp);
            }
        }
        //create new quad with the transformed vertex data
        return new UnpackedBakedQuad(vertexData, quad.getTintIndex(), quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), format);
    }

    public final void transformUnpackedVertexDataElement(VertexFormatElement.EnumUsage type, float[] data, Vector4f temp) {
        switch (type) {
            case POSITION:
            case NORMAL:
                this.matrices.transform(data[0], data[1], data[2], 1, temp);
                data[0] = temp.x;
                data[1] = temp.y;
                data[2] = temp.z;
                break;
            case COLOR:
                data[0] = getRed();
                data[1] = getGreen();
                data[2] = getBlue();
                data[3] = getAlpha();
                break;
        }
    }



}
