package com.infinityraider.infinitylib.utility;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class RayTraceHelper {
    @Nullable
    public static RayTraceResult getTargetBlock(Entity entity, double distance) {
        Optional<Tuple<Vec3d, Vec3d>> eyesAndTrace = getEyesAndTraceVectors(entity, distance);
        if(!eyesAndTrace.isPresent()) {
            return null;
        }
        return entity.getEntityWorld().rayTraceBlocks(eyesAndTrace.get().getFirst(), eyesAndTrace.get().getSecond(), false, false, true);
    }

    @Nullable
    public static RayTraceResult getTargetEntityOrBlock(Entity entity, double distance) {
        Optional<Tuple<Vec3d, Vec3d>> eyesAndTrace = getEyesAndTraceVectors(entity, distance);
        if(!eyesAndTrace.isPresent()) {
            return null;
        }
        return rayTraceBlocksForEntity(entity, entity.getEntityWorld(), eyesAndTrace.get().getFirst(), eyesAndTrace.get().getSecond(), false, false, true);
    }

    @Nullable
    public static RayTraceResult getTargetEntityOrBlock(Entity entity, double distance, Class<? extends Entity> entityClass) {
        Optional<Tuple<Vec3d, Vec3d>> eyesAndTrace = getEyesAndTraceVectors(entity, distance);
        if(!eyesAndTrace.isPresent()) {
            return null;
        }
        return rayTraceBlocksForEntity(entity, entity.getEntityWorld(), eyesAndTrace.get().getFirst(), eyesAndTrace.get().getSecond(), false, false, true, entityClass);
    }

    @Nullable
    public static RayTraceResult getTargetEntityOrBlock(Entity entity, double distance, Predicate<? super Entity> filter) {
        Optional<Tuple<Vec3d, Vec3d>> eyesAndTrace = getEyesAndTraceVectors(entity, distance);
        if(!eyesAndTrace.isPresent()) {
            return null;
        }
        return rayTraceBlocksForEntity(entity, entity.getEntityWorld(), eyesAndTrace.get().getFirst(), eyesAndTrace.get().getSecond(), false, false, true, filter);
    }

    private static Optional<Tuple<Vec3d, Vec3d>> getEyesAndTraceVectors(Entity entity, double distance) {
        Vec3d eyes = new Vec3d(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ);
        Vec3d look = entity.getLookVec();
        if(look == null) {
            return Optional.empty();
        }
        Vec3d trace = eyes.add(look.x * distance, look.y * distance, look.z * distance);
        return Optional.of(new Tuple<>(eyes, trace));
    }

    @Nullable
    public static RayTraceResult rayTraceBlocksForEntity(
            Entity entity, World world, Vec3d start, Vec3d ray, boolean stopOnLiquid,
            boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {

        return rayTraceBlocksForEntity(entity, world, start, ray, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock, Entity.class);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static RayTraceResult rayTraceBlocksForEntity(
            Entity entity, World world, Vec3d start, Vec3d ray, boolean stopOnLiquid,
            boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock, Class<? extends Entity> entityClass) {

        return rayTraceBlocksForEntity(entity, world, start, ray, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock, new PredicateInstanceOf(entityClass));
    }

    @Nullable
    public static RayTraceResult rayTraceBlocksForEntity(
            Entity entity, World world, Vec3d start, Vec3d ray, boolean stopOnLiquid,
            boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock, Predicate<? super Entity> filter) {

        if (!Double.isNaN(start.x) && !Double.isNaN(start.y) && !Double.isNaN(start.z)) {
            if (!Double.isNaN(ray.x) && !Double.isNaN(ray.y) && !Double.isNaN(ray.z)) {
                int x2 = MathHelper.floor(ray.x);
                int y2 = MathHelper.floor(ray.y);
                int z2 = MathHelper.floor(ray.z);
                int x1 = MathHelper.floor(start.x);
                int y1 = MathHelper.floor(start.y);
                int z1 = MathHelper.floor(start.z);
                BlockPos pos = new BlockPos(x1, y1, z1);
                IBlockState state = world.getBlockState(pos);
                Block blockStart = state.getBlock();

                if ((!ignoreBlockWithoutBoundingBox || state.getCollisionBoundingBox(world, pos) != Block.NULL_AABB) && blockStart.canCollideCheck(state, stopOnLiquid)) {
                    RayTraceResult raytraceresult = state.collisionRayTrace(world, pos, start, ray);
                    if (raytraceresult != null) {
                        return raytraceresult;
                    }
                }

                RayTraceResult result = null;
                int k1 = 200;
                while (k1-- >= 0) {
                    if (Double.isNaN(start.x) || Double.isNaN(start.y) || Double.isNaN(start.z)) {
                        return null;
                    }
                    if (x1 == x2 && y1 == y2 && z1 == z2) {
                        return returnLastUncollidableBlock ? result : null;
                    }

                    Tuple<Double, Boolean> xStep = step(x1, x2);
                    Tuple<Double, Boolean> yStep = step(y1, y2);
                    Tuple<Double, Boolean> zStep = step(z1, z2);
                    boolean flagX = xStep.getSecond();
                    boolean flagY = yStep.getSecond();
                    boolean flagZ = zStep.getSecond();
                    double dX = xStep.getFirst();
                    double dY = yStep.getFirst();
                    double dZ = zStep.getFirst();

                    double xNew = 999.0D;
                    double yNew = 999.0D;
                    double zNew = 999.0D;
                    double deltaX = ray.x - start.x;
                    double deltaY = ray.y - start.y;
                    double deltaZ = ray.z - start.z;

                    if (flagX) {
                        xNew = (dX - start.x) / deltaX;
                    }

                    if (flagY) {
                        yNew = (dY - start.y) / deltaY;
                    }

                    if (flagZ) {
                        zNew = (dZ - start.z) / deltaZ;
                    }

                    if (xNew == -0.0D) {
                        xNew = -1.0E-4D;
                    }

                    if (yNew == -0.0D) {
                        yNew = -1.0E-4D;
                    }

                    if (zNew == -0.0D) {
                        zNew = -1.0E-4D;
                    }

                    EnumFacing enumfacing;

                    if (xNew < yNew && xNew < zNew) {
                        enumfacing = x2 > x1 ? EnumFacing.WEST : EnumFacing.EAST;
                        start = new Vec3d(dX, start.y + deltaY * xNew, start.z + deltaZ * xNew);
                    } else if (yNew < zNew) {
                        enumfacing = y2 > y1 ? EnumFacing.DOWN : EnumFacing.UP;
                        start = new Vec3d(start.x + deltaX * yNew, dY, start.z + deltaZ * yNew);
                    } else {
                        enumfacing = z2 > z1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        start = new Vec3d(start.x + deltaX * zNew, start.y + deltaY * zNew, dZ);
                    }

                    x1 = MathHelper.floor(start.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    y1 = MathHelper.floor(start.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    z1 = MathHelper.floor(start.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    pos = new BlockPos(x1, y1, z1);

                    List<Entity> entities = world.getEntitiesInAABBexcluding(entity, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), filter);
                    Entity closest = null;
                    double dist = 999.0D * 999.0D;
                    for(Entity collided : entities) {
                        double distTo = (collided.posX - entity.posX)*(collided.posX - entity.posX) + (collided.posY - entity.posY)*(collided.posY - entity.posY) + (collided.posZ- entity.posZ)*(collided.posZ - entity.posZ);
                        if(distTo < dist) {
                            dist = distTo;
                            closest = collided;
                        }
                    }

                    if(closest != null) {
                        return new RayTraceResult(closest);
                    }

                    IBlockState stateAt = world.getBlockState(pos);
                    Block block = stateAt.getBlock();

                    if (!ignoreBlockWithoutBoundingBox || stateAt.getMaterial() == Material.PORTAL || stateAt.getCollisionBoundingBox(world, pos) != Block.NULL_AABB) {
                        if (block.canCollideCheck(stateAt, stopOnLiquid)) {
                            RayTraceResult newResult = stateAt.collisionRayTrace(world, pos, start, ray);
                            if (newResult != null) {
                                return newResult;
                            }
                        } else {
                            result = new RayTraceResult(RayTraceResult.Type.MISS, start, enumfacing, pos);
                        }
                    }
                }

                return returnLastUncollidableBlock ? result : null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static Tuple<Double, Boolean> step(int v1, int v2) {
        boolean flag = true;
        double d = 999.0;

        if (v2 > v1) {
            d = (double)v1 + 1.0D;
        }
        else if (v2 < v1) {
            d = (double)v1 + 0.0D;
        }
        else {
            flag = false;
        }

        return new Tuple<>(d, flag);
    }

    public static class PredicateInstanceOf<E extends Entity> implements Predicate<E> {
        private final Class<E> entityClass;

        public PredicateInstanceOf(Class<E> entityClass) {
            this.entityClass = entityClass;
        }

        @Override
        public boolean apply(@Nullable Entity e) {
            return e != null && entityClass.isAssignableFrom(e.getClass());
        }
    }
}
