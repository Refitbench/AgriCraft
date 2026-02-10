package com.infinityraider.infinitylib.entity.ai.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

/**
 * Class to calculate paths for entities on a separate thread,
 * Different options and callbacks are available
 *
 * When prompting the calculator to calculate a path for an entity, the entity will be given a temporary path
 * which will keep the entity standby until the actual path has been determined
 *
 * Changing the path manually of the entity while a path is being calculated might result in weird entity behaviour,
 * cancel the job first before setting a new path
 */
public class PathCalculator implements Runnable {
    /**
     * Singleton instance, will be used to calculate paths, will by default be set to this class,
     * can manually be changed by calling setInstance().
     */
    private static PathCalculator instance;

    /**
     * Sets the PathCalculator instance to a different instance, all current jobs will be cancelled
     * @param calculator
     */
    public static void setInstance(PathCalculator calculator) {
        if(instance != null) {
            instance.cancel();
        }
        instance = calculator;
    }

    /**
     * @return the current instance
     */
    public static  PathCalculator getInstance() {
        return instance;
    }

    /** All queued jobs */
    private final Deque<PathFindJob> jobs;

    /** Flag: is true if the calculator should stop calculating paths */
    private boolean stop;
    /** Flag: is true if the calculator is not running */
    private boolean stopped;
    /** Flag: is true if all current jobs should be cancelled */
    private boolean cancelJobs;

    /** Constructor */
    protected PathCalculator() {
        this.jobs = new ArrayDeque<>();
        this.stop = false;
        this.stopped = true;
        this.cancelJobs = false;
    }

    //Path calculation request methods
    //--------------------------------

    /**
     * Calculates a path for an entity to the specified coordinates,
     * Will use all default path finding options (see PathOptionsConfigurator)
     *
     * @param entity the entity
     * @param x the target x-coordinate
     * @param y the target y-coordinate
     * @param z the target z-coordinate
     */
    public void calculatePath(EntityLiving entity, double x, double y, double z) {
        this.calculatePath(entity, new Vec3d(x, y, z), defaultOptions());
    }

    /**
     * Calculates a path for an entity to the specified coordinates, using the specified options
     *
     * @param entity the entity
     * @param x the target x-coordinate
     * @param y the target y-coordinate
     * @param z the target z-coordinate
     * @param options the options to use to determine the path
     */
    public void calculatePath(EntityLiving entity, double x, double y, double z, IPathOptions options) {
        this.calculatePath(entity, new Vec3d(x, y, z), options);
    }

    /**
     * Calculates a path for an entity to the specified coordinates,
     * Will use all default path finding options (see PathOptionsConfigurator)
     *
     * @param entity the entity
     * @param target the target position
     */
    public void calculatePath(EntityLiving entity, BlockPos target) {
        this.calculatePath(entity, new Vec3d(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D), defaultOptions());
    }

    /**
     * Calculates a path for an entity to the specified coordinates, using the specified options
     *
     * @param entity the entity
     * @param target the target position
     * @param options the options to use to determine the path
     */
    public void calculatePath(EntityLiving entity, Vec3i target, IPathOptions options) {
        this.calculatePath(entity, new Vec3d(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D), options);
    }

    /**
     * Calculates a path for an entity to the specified coordinates,
     * Will use all default path finding options (see PathOptionsConfigurator)
     *
     * @param entity the entity
     * @param target the target position
     */
    public void calculatePath(EntityLiving entity, Vec3d target) {
        this.calculatePath(entity, target, defaultOptions());
    }

    /**
     * Calculates a path for an entity to the specified coordinates, using the specified options
     *
     * @param entity the entity
     * @param target the target position
     * @param options the options to use to determine the path
     */
    public void calculatePath(EntityLiving entity, Vec3d target, IPathOptions options) {
        ITarget iTarget =  new ITarget() {
            @Override
            public Vec3d getTarget() {
                return target;
            }

            @Override
            public boolean canTargetMove() {
                return false;
            }

            @Override
            public boolean hasTargetChanged(Vec3d previous) {
                return false;
            }

            @Override
            public boolean isValid() {
                return true;
            }
        };
        this.calculatePath(entity, iTarget, options);
    }

    /**
     * Calculates a path for an entity to a specified entity,
     * Will use all default path finding options (see PathOptionsConfigurator)
     *
     * @param entity the entity
     * @param target the target entity
     */
    public void calculatePath(EntityLiving entity, Entity target) {
        this.calculatePath(entity, target, defaultOptions());
    }

    /**
     * Calculates a path for an entity to a specified entity, using the specified options
     *
     * @param entity the entity
     * @param target the target entity
     * @param options the options to use to determine the path
     */
    public void calculatePath(EntityLiving entity, Entity target, IPathOptions options) {
        ITarget iTarget = new ITarget() {
            @Override
            public Vec3d getTarget() {
                return target.getPositionVector();
            }

            @Override
            public boolean canTargetMove() {
                return true;
            }

            @Override
            public boolean hasTargetChanged(Vec3d previous) {
                Vec3d current = getTarget();
                double dX = current.x - previous.x;
                double dY = current.y - previous.y;
                double dZ = current.z - previous.z;
                return (dX * dX + dY * dY + dZ * dZ) >= 1.0D;
            }

            @Override
            public boolean isValid() {
                return target.isEntityAlive();
            }
        };
        this.calculatePath(entity, iTarget, options);
    }

    /**
     * Calculates a path for an entity to a custom target,
     * Will use all default path finding options (see PathOptionsConfigurator)
     *
     * @param entity the entity
     * @param target the target
     */
    public void calculatePath(EntityLiving entity, ITarget target) {
        calculatePath(entity, target, defaultOptions());
    }

    /**
     * Calculates a path for an entity to a custom target, using the specified options
     *
     * @param entity the entity
     * @param target the target
     * @param options the options to use to determine the path
     */
    public void calculatePath(EntityLiving entity, ITarget target, IPathOptions options) {
        this.calculatePath(new PathFindJob(entity, target, options));
    }

    /**
     * Calculates a path for an entity by specifying a custom job
     *
     * @param job the path finding job
     */
    public void calculatePath(PathFindJob job) {
        this.addJob(job);
    }


    //Calculator execution command methods
    //------------------------------------

    /** Starts the calculation thread */
    public void start() {
        if(this.stopped) {
            this.stop = false;
            new Thread(this).start();
        }
    }

    /** Stops the calculation thread without cancelling any queued jobs */
    public void stop() {
        this.stop = true;
    }

    /** Cancels the calculation thread and cancels all queued jobs */
    public void cancel() {
        this.cancelJobs = true;
        this.stop();
    }

    @Override
    public final void run() {
        this.stopped = false;
        while(!stop) {
            processJob(jobs.pollFirst());
            if(jobs.size() <= 0) {
                this.stop();
            }
        }
        if(this.cancelJobs) {
            jobs.forEach(PathFindJob::cancel);
            jobs.clear();
            this.cancelJobs = false;
        }
        this.stopped = true;
    }


    //Internal execution logic methods
    //--------------------------------

    /**
     * Adds a job to be processed by the calculation thread,
     * Will fail if the thread has been cancelled
     * Starts the thread if the thread was stopped
     *
     * @param job the job to add
     * @return true if the job was successfully queued, false otherwise
     */
    protected boolean addJob(PathFindJob job) {
        if(this.cancelJobs) {
            return false;
        }
        this.jobs.add(job);
        if(this.stopped) {
            this.start();
        }
        return true;
    }

    /**
     * Processes a job, meaning a Path is calculated, and the job callback methods are correctly notified afterwards
     * @param job job to be processed
     */
    protected void processJob(PathFindJob job) {
        if(job != null && job.isValid()) {
            this.determinePath(job).map(path -> {
                job.finish(path);
                return job;
            }).orElseGet(() -> {
                job.fail();
                return job;
            });
        }
    }

    /**
     * Actually calculates a path for a job
     * @param job the job defining the path to be calculated
     * @return an optional holding the resulting path, or empty if no path is found
     */
    protected Optional<Path> determinePath(PathFindJob job) {
        //TODO
        return Optional.empty();
    }


    //Utility methods and classes
    //---------------------------

    /**
     * @return a new configurator to easily configure path finding options
     */
    public static PathOptionsConfigurator configurator() {
        return new PathOptionsConfigurator();
    }

    /**
     * @return the default path finding options
     */
    public static IPathOptions defaultOptions() {
        return configurator().configure();
    }

    /**
     * Interface defining the target for path finding
     */
    public interface ITarget {
        /**
         * @return the target coordinates
         */
        Vec3d getTarget();

        /**
         * @return the target coordinates converted to a PathPoint object
         */
        default PathPoint getTargetPoint() {
            Vec3d target = this.getTarget();
            return new PathPoint(MathHelper.floor(target.x), MathHelper.floor(target.y), MathHelper.floor(target.z));
        }

        /**
         * @return true if the target is not fixed, influences path finding logic
         */
        boolean canTargetMove();

        /**
         * Checks if the target has (significantly) moved since the previous getTarget() call
         * @param previous the previous target
         * @return true if the target has changed
         */
        boolean hasTargetChanged(Vec3d previous);

        /**
         * Checks if the target is still valid, a job with a non-valid target will be cancelled
         * For example, a path finding job to an entity, which has died in the meantime, is no longer valid
         *
         * @return if the job is still valid
         */
        boolean isValid();
    }

    /**
     * Interface defining the options to use for path finding
     */
    public interface IPathOptions {
        /**
         * @return true if the entity can open doors
         */
        boolean canOpenDoors();

        /**
         * @return true if the entity can climb ladders
         */
        boolean canClimbLadders();

        /**
         * @return true if the entity can climb walls (like spiders)
         */
        boolean canClimbWalls();

        /**
         * @return true if the entity can swim
         */
        boolean canSwim();

        /**
         * @return true if the entity can fly
         */
        boolean canFly();

        /**
         * @return the maximum height the entity is allowed to fall
         */
        int maxFallHeight();

        /**
         * @return the maximum height the entity can jump
         */
        int maxJumpHeight();
    }

    /**
     * Utility class to easily configure path finding options by using chain calls
     */
    public static class PathOptionsConfigurator {
        private boolean canOpenDoors;
        private boolean canClimbLadders;
        private boolean canClimbWalls;
        private boolean canSwim;
        private boolean canFly;
        private int maxFallHeight;
        private int maxJumpHeight;

        private PathOptionsConfigurator() {
            this.canOpenDoors = false;
            this.canClimbLadders = false;
            this.canClimbWalls = false;
            this.canSwim = false;
            this.canFly = false;
            this.maxFallHeight = 1;
            this.maxJumpHeight = 1;
        }

        public PathOptionsConfigurator setCanOpenDoors(boolean value) {
            this.canOpenDoors = value;
            return this;
        }

        public PathOptionsConfigurator setCanClimbLadders(boolean value) {
            this.canClimbLadders = value;
            return this;
        }

        public PathOptionsConfigurator setCanClimbWalls(boolean value) {
            this.canClimbWalls = value;
            return this;
        }

        public PathOptionsConfigurator setCanSwim(boolean value) {
            this.canSwim = value;
            return this;
        }

        public PathOptionsConfigurator setCanFly(boolean value) {
            this.canFly = value;
            return this;
        }

        public PathOptionsConfigurator setMaxFallHeight(int value) {
            this.maxFallHeight = value;
            return this;
        }

        public PathOptionsConfigurator setMaxJumpHeight(int value) {
            this.maxJumpHeight = value;
            return this;
        }

        public IPathOptions configure() {
            return new IPathOptions() {
                @Override
                public boolean canOpenDoors() {
                    return canOpenDoors;
                }

                @Override
                public boolean canClimbLadders() {
                    return canClimbLadders;
                }

                @Override
                public boolean canClimbWalls() {
                    return canClimbWalls;
                }

                @Override
                public boolean canSwim() {
                    return canSwim;
                }

                @Override
                public boolean canFly() {
                    return canFly;
                }

                @Override
                public int maxFallHeight() {
                    return maxFallHeight;
                }

                @Override
                public int maxJumpHeight() {
                    return maxJumpHeight;
                }
            };
        }
    }

    static {
        setInstance(new PathCalculator());
    }
}