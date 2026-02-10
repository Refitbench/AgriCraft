package com.infinityraider.infinitylib.entity.ai.pathfinding;

import com.infinityraider.infinitylib.InfinityLib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Objects;

public class PathFindJob extends Path {
    public static final double DEFAULT_SPEED = 1.5D;

    private final EntityLiving entity;
    private final double speed;
    private final PathCalculator.ITarget target;
    private final PathCalculator.IPathOptions options;
    private final ICallback callback;

    private boolean cancelled;

    public PathFindJob(EntityLiving entity, PathCalculator.ITarget target, PathCalculator.IPathOptions options) {
        this(entity, DEFAULT_SPEED, target, options);
    }

    public PathFindJob(EntityLiving entity, double speed, PathCalculator.ITarget target, PathCalculator.IPathOptions options) {
        this(entity, speed, target, options, ICallback.none);
    }

    public PathFindJob(EntityLiving entity, double speed, PathCalculator.ITarget target, PathCalculator.IPathOptions options, ICallback callback) {
        super(new PathPoint[] {target.getTargetPoint()});
        this.entity = Objects.requireNonNull(entity);
        this.speed = speed;
        this.target = Objects.requireNonNull(target);
        this.options = options;
        this.callback = Objects.requireNonNull(callback);
        this.cancelled = false;
    }


    //Getters
    //-------

    public EntityLiving entity() {
        return this.entity;
    }

    public double speed() {
        return this.speed;
    }

    public PathCalculator.IPathOptions options() {
        return this.options;
    }

    public PathPoint getEntityPoint() {
        Vec3d entityPos = this.getEntityPosition();
        return new PathPoint(MathHelper.floor(entityPos.x), MathHelper.floor(entityPos.y), MathHelper.floor(entityPos.z));
    }

    public Vec3d getEntityPosition() {
        return this.entity().getPositionVector().add(new Vec3d(
                (double)((int)(this.entity().width + 1.0F)) * 0.5D,
                0,
                (double)((int)(this.entity().width + 1.0F)) * 0.5D
        ));
    }


    //Job methods
    //-----------

    public boolean isValid() {
        return (!this.cancelled) && this.entity().isEntityAlive() && this.target().isValid();
    }

    public PathFindJob cancel() {
        this.cancelled = true;
        this.entity().getNavigator().setPath(null, this.speed());
        InfinityLib.proxy.queueTask(this.callback::onJobCancelled);
        return this;
    }

    public PathFindJob finish(Path path) {
        this.entity().getNavigator().setPath(path, this.speed());
        InfinityLib.proxy.queueTask(() -> this.callback.onJobFinished(this.entity(), path));
        this.cancelled = true;
        return this;
    }

    public PathFindJob fail() {
        this.cancelled = true;
        this.entity().getNavigator().setPath(null, this.speed());
        InfinityLib.proxy.queueTask(this.callback::onJobFailed);
        return this;
    }

    public interface ICallback {
        void onJobCancelled();

        void onJobFinished(EntityLiving entity, Path path);

        void onJobFailed();

        ICallback none = new ICallback() {
            @Override
            public void onJobCancelled() {}

            @Override
            public void onJobFinished(EntityLiving entity, Path path) {}

            @Override
            public void onJobFailed() {}
        };
    }


    //Target properties
    //-----------------

    public PathCalculator.ITarget target() {
        return this.target;
    }

    public boolean canTargetMove() {
        return this.target.canTargetMove();
    }

    public boolean hasTargetChanged(Vec3d previous) {
        return this.target.hasTargetChanged(previous);
    }

    public Vec3d getTargetVector() {
        return this.target.getTarget();
    }


    //Path options
    //------------
    public boolean canOpenDoors() {
        return this.options().canOpenDoors();
    }

    public boolean canClimbLadders() {
        return this.options().canClimbLadders();
    }

    public boolean canClimbWalls() {
        return this.options().canClimbWalls();
    }

    public boolean canSwim() {
        return this.options().canSwim();
    }

    public boolean canFly() {
        return this.options().canFly();
    }

    public int maxFallHeight() {
        return this.options().maxFallHeight();
    }

    public int maxJumpHeight() {
        return this.options().maxJumpHeight();
    }


    //Overrides from Path
    //-------------------

    @Override
    public void incrementPathIndex() {}

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    @Nullable
    public PathPoint getFinalPathPoint() {
        return this.target.getTargetPoint();
    }

    @Override
    public PathPoint getPathPointFromIndex(int index) {
        return this.getEntityPoint();
    }

    @Override
    public void setPoint(int index, PathPoint point) {}

    @Override
    public int getCurrentPathLength() {
        return MathHelper.ceil(this.entity().getPositionVector().distanceTo(this.target.getTarget()));
    }

    @Override
    public void setCurrentPathLength(int length) {}

    @Override
    public int getCurrentPathIndex() {
        return 0;
    }

    @Override
    public void setCurrentPathIndex(int currentPathIndexIn) {}

    @Override
    public Vec3d getVectorFromIndex(Entity entity, int index) {
        return this.getEntityPosition();
    }

    @Override
    public Vec3d getPosition(Entity entity)  {
        return this.getVectorFromIndex(entity, this.getCurrentPathIndex());
    }

    @Override
    public Vec3d getCurrentPos() {
        return this.getEntityPosition();
    }

    @Override
    public boolean isSamePath(Path other) {
        return other == this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public PathPoint[] getOpenSet() {
        return new PathPoint[] {};
    }

    @Override
    @SideOnly(Side.CLIENT)
    public PathPoint[] getClosedSet() {
        return new PathPoint[] {};
    }

    @Override
    @SideOnly(Side.CLIENT)
    public PathPoint getTarget()  {
        return this.getFinalPathPoint();
    }
}