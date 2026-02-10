/*
 */
package com.infinityraider.infinitylib.render.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * TESR wrapper class to allow for use of the ITesr interface.
 * This is a way to simulate multiple inheritance of renderers.
 */
@SideOnly(Side.CLIENT)
public class TesrWrapper<T extends TileEntity> extends TileEntitySpecialRenderer<T> {
	
	private final ITesr<T> tesr;

	public TesrWrapper(ITesr<T> tesr) {
		this.tesr = tesr;
	}

	@Override
	public void render(T tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		this.tesr.renderTileEntityAt(tile, x, y, z, partialTicks, destroyStage, alpha);
	}
	
}