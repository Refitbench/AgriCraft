package com.infinityraider.infinitylib.render.model;

import com.infinityraider.infinitylib.reference.Constants;
import com.infinityraider.infinitylib.render.tessellation.VertexData;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.joml.Matrix4f;
import org.joml.Vector4f;

/**
 * Class to render Techne models in 1.8+'s rendering system. Create one object
 * of this class for every Techne model you wish to render, Call getBakedQuads
 * with the desired vertex format to get a list of baked quads, it is advised to
 * cache this.
 *
 * @param <M> the generic type of your Techne model
 */
@SideOnly(Side.CLIENT)
public class ModelTechne<M extends ModelBase> {

    private final M model;
    private final List<ModelRenderer> modelRenderers;
    private final List<Tuple<ModelRenderer, List<TexturedQuad>>> texturedQuads;

    private boolean diffuseLighting;

    public ModelTechne(M techneModel) {
        this.model = techneModel;
        this.modelRenderers = compileModelRendererList(getModel());
        this.texturedQuads = compileTexturedQuadList(getModelRenderers());
    }

    public ModelTechne<M> setDiffuseLighting(boolean status) {
        this.diffuseLighting = status;
        return this;
    }

    public boolean getDiffuseLighting() {
        return this.diffuseLighting;
    }

    public M getModel() {
        return model;
    }

    public List<ModelRenderer> getModelRenderers() {
        return modelRenderers;
    }

    public List<Tuple<ModelRenderer, List<TexturedQuad>>> getTexturedQuads() {
        return texturedQuads;
    }

    /**
     * Returns a list of baked quads to render this Techne model, use this
     * method to render from a separate texture. The texture has to be bound
     * first using Minecraft.getMinecraft().renderEngine.bindTexture() The
     * returned list should be cached.
     *
     * @param format vertex format to create baked quads with
     * @return an immutable list of baked quads for this model
     */
    public List<BakedQuad> getBakedQuads(VertexFormat format) {
        return this.getBakedQuads(format, null);
    }

    /**
     * Returns a list of baked quads to render this Techne model, use this
     * method when the texture of the model is stitched to the texturemap The
     * returned list should be cached.
     *
     * @param format vertex format to create baked quads with
     * @param icon an icon stitched to the texture map used to render this model
     * @return an immutable list of baked quads for this model
     */
    public List<BakedQuad> getBakedQuads(VertexFormat format, TextureAtlasSprite icon) {
        return this.getBakedQuads(format, icon, 1);
    }

    /**
     * Returns a list of baked quads to render this Techne model, use this
     * method when the texture of the model is stitched to the texturemap The
     * returned list should be cached.
     *
     * @param format vertex format to create baked quads with
     * @param icon an icon stitched to the texture map used to render this model
     * @param scale the scaling to apply to the model
     * @return an immutable list of baked quads for this model
     */
    public List<BakedQuad> getBakedQuads(VertexFormat format, TextureAtlasSprite icon, float scale) {
        Matrix4f matrix = new Matrix4f().identity();
        return getTexturedQuads().stream().flatMap(t
                -> t.getSecond().stream().map(quad
                        -> createBakedQuad(format, t.getFirst(), quad, matrix, scale * Constants.UNIT, icon)
                )
        ).collect(Collectors.toList());
    }

    private BakedQuad createBakedQuad(VertexFormat format, ModelRenderer renderer, TexturedQuad quad, Matrix4f matrix, float scale, TextureAtlasSprite icon) {
        //Transformation
        loadRenderMatrix(renderer, matrix, scale);

        //normal for the quad
        Vec3d vec3d = quad.vertexPositions[1].vector3D.subtractReverse(quad.vertexPositions[0].vector3D);
        Vec3d vec3d1 = quad.vertexPositions[1].vector3D.subtractReverse(quad.vertexPositions[2].vector3D);
        Vec3d vec3d2 = vec3d1.crossProduct(vec3d).normalize();
        final Vector4f normal = new Vector4f((float) vec3d2.x, (float) vec3d2.y, (float) vec3d2.z, 1);
        matrix.transform(normal);
        final Vector4f pos = new Vector4f();

        //define vertex data for the quad
        VertexData[] vertexData = new VertexData[quad.vertexPositions.length];
        for (int i = 0; i < vertexData.length; i++) {
            PositionTextureVertex vertex = quad.vertexPositions[i];
            vertexData[i] = new VertexData(format);
            pos.set((float) vertex.vector3D.x * scale, (float) vertex.vector3D.y * scale, (float) vertex.vector3D.z * scale, 1);
            matrix.transform(pos);
            vertexData[i].setXYZ(pos.x, pos.y, pos.z);
            vertexData[i].setRGBA(1, 1, 1, 1);
            vertexData[i].setNormal(normal.x, normal.y, normal.z);
            if (icon != null) {
                vertexData[i].setUV(icon.getInterpolatedU(vertex.texturePositionX * 16), icon.getInterpolatedV(vertex.texturePositionY * 16));
            } else {
                vertexData[i].setUV(vertex.texturePositionX, vertex.texturePositionY);
            }
        }

        //build and return the quad
        UnpackedBakedQuad.Builder quadBuilder = new UnpackedBakedQuad.Builder(format);
        quadBuilder.setTexture(icon);
        quadBuilder.setApplyDiffuseLighting(this.getDiffuseLighting());
        for (VertexData data : vertexData) {
            data.applyVertexData(quadBuilder);
        }
        return quadBuilder.build();
    }

    private static List<ModelRenderer> compileModelRendererList(ModelBase model) {
        List<ModelRenderer> list = new ArrayList<>();
        for (Field field : model.getClass().getDeclaredFields()) {
            if (field.getType().isAssignableFrom(ModelRenderer.class)) {
                field.setAccessible(true);
                try {
                    list.add((ModelRenderer) field.get(model));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return ImmutableList.copyOf(list);
    }

    private static List<Tuple<ModelRenderer, List<TexturedQuad>>> compileTexturedQuadList(List<ModelRenderer> modelRenderers) {
        List<Tuple<ModelRenderer, List<TexturedQuad>>> list = new ArrayList<>();
        for (ModelRenderer model : modelRenderers) {
            List<TexturedQuad> quadList = new ArrayList<>();
            for (ModelBox box : model.cubeList) {
                Field fieldQuads = null;
                for (Field field : box.getClass().getDeclaredFields()) {
                    if (field.getType().isAssignableFrom(TexturedQuad[].class)) {
                        fieldQuads = field;
                        break;
                    }
                }
                if (fieldQuads != null) {
                    fieldQuads.setAccessible(true);
                    try {
                        Collections.addAll(quadList, (TexturedQuad[]) fieldQuads.get(box));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            list.add(new Tuple<>(model, ImmutableList.copyOf(quadList)));
        }
        return ImmutableList.copyOf(list);
    }

    private static void loadRenderMatrix(ModelRenderer renderer, Matrix4f matrix, float scale) {
        //by default, the model renders upside down and offset
        matrix.identity()
                .rotate((float) Math.toRadians(180), 1, 0, 0 )
                .translate(8 * scale, - 24 * scale, -  8 * scale)
                .translate(
                        renderer.offsetX + renderer.rotationPointX * scale,
                        renderer.offsetY + renderer.rotationPointY * scale,
                        renderer.offsetZ + renderer.rotationPointZ * scale);

        //apparently in the GL call list, the rotation point is used as the offset, I'm not sure why, but it is the case
        if (renderer.rotateAngleZ != 0) {
            matrix.rotate(renderer.rotateAngleZ, 0, 0, 1);
        }
        if (renderer.rotateAngleY != 0) {
            matrix.rotate(renderer.rotateAngleY, 0, 1, 0);
        }
        if (renderer.rotateAngleX != 0) {
            matrix.rotate(renderer.rotateAngleX, 1, 0, 0);
        }
    }
}
