package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//Yes, really
@SideOnly(Side.CLIENT)
public class GrabProtectedRenderDataHack {
    public static boolean getRenderOutlines(Render render) {
        return render.renderOutlines;
    }

    public static boolean getRenderMarker(RenderLivingBase render) {
        return render.renderMarker;
    }

    public static <E extends EntityLivingBase> boolean setScoreTeamColor(RenderLivingBase<E> renderer, E entity) {
        return renderer.setScoreTeamColor(entity);
    }

    public static <E extends EntityLivingBase> void unsetScoreTeamColor(RenderLivingBase<E> renderer) {
        renderer.unsetScoreTeamColor();
    }

    public static <E extends EntityLivingBase> int getTeamColor(RenderLivingBase<E> renderer, E entity) {
        return renderer.getTeamColor(entity);
    }

    public static <E extends EntityLivingBase> void renderName(RenderLivingBase<E> renderer, E entity, double x, double y, double z) {
        renderer.renderName(entity, x, y, z);
    }

    public static <E extends EntityLivingBase> boolean setDoRenderBrightness(RenderLivingBase<E> renderer, E entity, float partialTicks) {
        return renderer.setDoRenderBrightness(entity, partialTicks);
    }

    public static <E extends EntityLivingBase> void unsetBrightness(RenderLivingBase<E> renderer) {
        renderer.unsetBrightness();
    }
}
