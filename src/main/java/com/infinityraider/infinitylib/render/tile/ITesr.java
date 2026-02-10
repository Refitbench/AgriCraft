/*
 */
package com.infinityraider.infinitylib.render.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Interface allowing TESRs to be used by classes that already have a superclass.
 */
@SideOnly(Side.CLIENT)
@FunctionalInterface
public interface ITesr<T extends TileEntity> {
	
	void renderTileEntityAt(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha);
	
}