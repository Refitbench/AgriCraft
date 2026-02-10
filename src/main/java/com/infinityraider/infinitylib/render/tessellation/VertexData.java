package com.infinityraider.infinitylib.render.tessellation;

import java.util.Objects;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class VertexData {

    private final VertexFormat format;
    private float x, y, z;
    private float u, v;
    private float r, g, b, a;
    private float nX, nY, nZ;

    public VertexData(VertexFormat format) {
        this.format = format;
    }

    public void setXYZ(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setUV(float u, float v) {
        this.u = u;
        this.v = v;
    }

    public void setRGB(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void setRGBA(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void setNormal(float x, float y, float z) {
        this.nX = x;
        this.nY = y;
        this.nZ = z;
    }

    public void applyVertexData(UnpackedBakedQuad.Builder builder) {
        for (int index = 0; index < format.getElementCount(); index++) {
            applyVertexDataForType(index, format.getElement(index).getUsage(), builder);
        }
    }

    private void applyVertexDataForType(int index, VertexFormatElement.EnumUsage type, UnpackedBakedQuad.Builder builder) {
        switch (type) {
            case POSITION:
                builder.put(index, x, y, z, 1);
                break;
            case UV:
                builder.put(index, u, v, 0, 1);
                break;
            case COLOR:
                builder.put(index, r, g, b, a);
                break;
            case NORMAL:
                builder.put(index, nX, nY, nZ, 0);
                break;
            case PADDING:
                //TODO: figure this one out
                builder.put(index);
                break;
            case GENERIC:
                //TODO: figure this one out
                builder.put(index);

        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VertexData) {
            final VertexData other = (VertexData) obj;
            return Objects.equals(this.format, other.format)
                    && (this.x == other.x)
                    && (this.y == other.y)
                    && (this.z == other.z)
                    && (this.u == other.u)
                    && (this.v == other.v)
                    && (this.r == other.r)
                    && (this.g == other.g)
                    && (this.b == other.b)
                    && (this.a == other.a)
                    && (this.nX == other.nX)
                    && (this.nY == other.nY)
                    && (this.nZ == other.nZ);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.format);
        hash = 89 * hash + Float.floatToIntBits(this.x);
        hash = 89 * hash + Float.floatToIntBits(this.y);
        hash = 89 * hash + Float.floatToIntBits(this.z);
        hash = 89 * hash + Float.floatToIntBits(this.u);
        hash = 89 * hash + Float.floatToIntBits(this.v);
        hash = 89 * hash + Float.floatToIntBits(this.r);
        hash = 89 * hash + Float.floatToIntBits(this.g);
        hash = 89 * hash + Float.floatToIntBits(this.b);
        hash = 89 * hash + Float.floatToIntBits(this.a);
        hash = 89 * hash + Float.floatToIntBits(this.nX);
        hash = 89 * hash + Float.floatToIntBits(this.nY);
        hash = 89 * hash + Float.floatToIntBits(this.nZ);
        return hash;
    }

}
