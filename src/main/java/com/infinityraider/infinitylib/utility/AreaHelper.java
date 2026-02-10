package com.infinityraider.infinitylib.utility;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class AreaHelper {
	
	public static AxisAlignedBB getArea(Vec3d center, double radius) {
		return getArea(center, radius, radius, radius);
	}
	
	public static AxisAlignedBB getArea(Vec3d center, double dx, double dy, double dz) {
		return new AxisAlignedBB(
				center.x - dx,
				center.y - dy,
				center.z - dz,
				center.x + dx,
				center.y + dy,
				center.z + dz
		);
	}

	public static AxisAlignedBB getArea(Entity center, double radius) {
		return getArea(center, radius, radius, radius);
	}

	public static AxisAlignedBB getArea(Entity center, double dx, double dy, double dz) {
		return getArea(center.getPositionVector(), dx, dy, dz);
	}
	
}
