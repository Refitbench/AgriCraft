package com.infinityraider.infinitylib.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * An invulnerable, invisible, stationary entity which can be used to store data to a point in the world
 * This does not take up a Block or a TileEntity space in the world, and can be placed inside blocks
 */
@SuppressWarnings("unused")
public abstract class AbstractEntityFrozen extends Entity implements IEntityAdditionalSpawnData {
    public AbstractEntityFrozen(World world) {
        super(world);
        this.isImmuneToFire = true;
        this.firstUpdate = false;
        this.dataManager = new EntityDataManager(this);
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.prevRotationYaw = 0;
        this.prevRotationPitch = 0;
    }

    protected abstract String name();

    protected abstract void update();

    protected abstract void onEntitySpawned();

    protected abstract void readDataFromNBT(NBTTagCompound tag);

    protected abstract void writeDataToNBT(NBTTagCompound tag);

    @Override
    public final void writeSpawnData(ByteBuf data) {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeDataToNBT(tag);
        ByteBufUtils.writeTag(data, tag);
        this.onEntitySpawned();
    }

    @Override
    public final void readSpawnData(ByteBuf data) {
        NBTTagCompound tag = ByteBufUtils.readTag(data);
        this.readDataFromNBT(tag);
        this.onEntitySpawned();
    }

    @Override
    public final void onEntityUpdate() {
        this.update();
    }

    @Override
    protected final void entityInit() {}

    @Override
    protected final void setOnFireFromLava() {}

    @Override
    public final void extinguish() {}

    @Override
    protected final void outOfWorld() {}

    @Override
    public final boolean isOffsetPositionInLiquid(double x, double y, double z) {
        return false;
    }

    @Override
    public final void move(MoverType type, double x, double y, double z) {}

    @Override
    protected final void doBlockCollisions() {}

    @Override
    protected final void playStepSound(BlockPos pos, Block blockIn) {}

    @Override
    public final void playSound(SoundEvent soundIn, float volume, float pitch) {}

    @Override
    public final boolean isSilent() {
        return true;
    }

    @Override
    public final void setSilent(boolean isSilent) {}

    @Override
    protected final boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected final void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {}

    @Override
    protected final void dealFireDamage(int amount) {}

    @Override
    public final void fall(float distance, float damageMultiplier) {}

    @Override
    public final boolean isWet() {
        return false;
    }

    @Override
    public final boolean isInWater() {
        return false;
    }

    @Override
    public final boolean handleWaterMovement() {
        return false;
    }

    @Override
    protected final void doWaterSplashEffect() {}

    @Override
    public final void spawnRunningParticles() {}

    @Override
    protected final void createRunningParticles() {}

    @Override
    public final boolean isInsideOfMaterial(Material material) {
        return false;
    }

    @Override
    public final boolean isInLava() {
        return false;
    }

    @Override
    public final void moveRelative(float strafe, float up, float forward, float friction) {}

    @Override
    public final void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {}

    @Override
    public final void moveToBlockPosAndAngles(BlockPos pos, float rotationYawIn, float rotationPitchIn) {}

    @Override
    public final void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {}

    @Override
    public final void applyEntityCollision(Entity entityIn) {}

    @Override
    public final void addVelocity(double x, double y, double z) {}

    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public final boolean isEntityAlive() {
        return true;
    }

    @Override
    public final boolean isEntityInsideOpaqueBlock() {
        return false;
    }

    @Override
    public final void updateRidden() {}

    @Override
    public final void updatePassenger(Entity entity) {}

    @Override
    @SideOnly(Side.CLIENT)
    public void applyOrientationToEntity(Entity entityToUpdate) {}

    @Override
    public final float getCollisionBorderSize() {
        return 0;
    }

    @Override
    public final void setPortal(BlockPos pos) {}

    @Override
    public final int getPortalCooldown() {
        return Integer.MAX_VALUE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void setVelocity(double x, double y, double z) {}

    @Override
    public final boolean isBurning() {
        return false;
    }

    @Override
    public final boolean isRiding() {
        return false;
    }

    @Override
    public final boolean isSneaking() {
        return false;
    }

    @Override
    public final void setSneaking(boolean sneaking) {}

    @Override
    public final boolean isSprinting() {
        return false;
    }

    @Override
    public final void setSprinting(boolean sprinting) {}

    @Override
    public final boolean isInvisible() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final boolean isInvisibleToPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public final void setInvisible(boolean invisible) {}

    @Override
    protected final boolean getFlag(int flag) {
        return false;
    }

    @Override
    protected final void setFlag(int flag, boolean set) {}

    @Override
    public final int getAir() {
        return 0;
    }

    @Override
    public final void setAir(int air) {}

    @Override
    public final void onStruckByLightning(EntityLightningBolt lightningBolt) {}

    @Override
    protected final boolean pushOutOfBlocks(double x, double y, double z) {
        return false;
    }

    @Override
    public final void setInWeb() {}

    @Override
    public final String getName() {
        return this.name();
    }

    @Override
    public boolean canBeAttackedWithItem()
    {
        return false;
    }

    @Override
    public final boolean hitByEntity(Entity entityIn) {
        return true;
    }

    @Override
    public final boolean isEntityInvulnerable(DamageSource source) {
        return true;
    }

    @Override
    public final void copyLocationAndAnglesFrom(Entity entity) {}

    @Override
    public final Entity changeDimension(int dimension) {
        return this;
    }

    @Override
    public final boolean canExplosionDestroyBlock(Explosion explosion, World world, BlockPos pos, IBlockState blockState, float f) {
        return false;
    }

    @Override
    public final int getMaxFallHeight() {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final boolean canRenderOnFire() {
        return false;
    }

    @Override
    public final boolean isPushedByWater() {
        return false;
    }

    @Override
    public final void setCustomNameTag(String name) {}

    @Override
    public final String getCustomNameTag() {
        return null;
    }

    @Override
    public final boolean hasCustomName() {
        return false;
    }

    @Override
    public final void setAlwaysRenderNameTag(boolean alwaysRenderNameTag) {}

    @Override
    public final boolean getAlwaysRenderNameTag() {
        return false;
    }

    @Override
    public final void setPositionAndUpdate(double x, double y, double z) {}

    @Override
    @SideOnly(Side.CLIENT)
    public final boolean getAlwaysRenderNameTagForRender() {
        return false;
    }

    @Override
    public final void setEntityBoundingBox(AxisAlignedBB bb){}

    @Override
    public final boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    protected final void applyEnchantments(EntityLivingBase entityLivingBaseIn, Entity entityIn) {}

    @Override
    public final boolean isCreatureType(EnumCreatureType type, boolean forSpawnCount) {
        return false;
    }

    @Override
    protected final void readEntityFromNBT(NBTTagCompound tag) {
        readDataFromNBT(tag);
    }

    @Override
    protected final void writeEntityToNBT(NBTTagCompound tag) {
        writeDataToNBT(tag);
    }
}
