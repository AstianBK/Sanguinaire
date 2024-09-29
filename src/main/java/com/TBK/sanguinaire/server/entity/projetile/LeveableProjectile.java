package com.TBK.sanguinaire.server.entity.projetile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public abstract class LeveableProjectile extends ThrowableProjectile {
    private static final EntityDataAccessor<Integer> POWER_LEVEL =
            SynchedEntityData.defineId(LeveableProjectile.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> CHARGED_LEVEL =
            SynchedEntityData.defineId(LeveableProjectile.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> IS_CHARGING =
            SynchedEntityData.defineId(LeveableProjectile.class, EntityDataSerializers.BOOLEAN);
    public int animTick=0;
    public int frame=0;

    protected LeveableProjectile(EntityType<? extends ThrowableProjectile> p_37466_, Level p_37467_) {
        super(p_37466_, p_37467_);
    }
    @OnlyIn(Dist.CLIENT)
    public int getFrame(){
        return this.frame;
    }

    @Override
    protected boolean canHitEntity(Entity p_37250_) {
        return super.canHitEntity(p_37250_) && !this.isCharging();
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide()) {
            HitResult result = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (result.getType() == HitResult.Type.MISS && this.isAlive()) {
                List<Entity> intersecting = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox(), this::canHitEntity);
                if (!intersecting.isEmpty())
                    this.onHit(new EntityHitResult(intersecting.get(0)));
            }
        }
        this.refreshDimensions();
        Vec3 vec3;
        vec3 = this.getDeltaMovement();
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;
        double d7 = this.getX() + d5;
        double d2 = this.getY() + d6;
        double d3 = this.getZ() + d1;
        double d4 = vec3.horizontalDistance();

        this.setXRot((float)(Mth.atan2(d6, d4) * (double)(180F / (float)Math.PI)));
        this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
        this.setYRot(lerpRotation(this.yRotO, this.getYRot()));

        this.setDeltaMovement(vec3);

        this.setPos(d7, d2, d3);
        this.checkInsideBlocks();
    }

    @Override
    public EntityDimensions getDimensions(Pose p_19975_) {
        return EntityDimensions.scalable(0.5F+3.0F*this.getChargedLevel()/10,0.2F);
    }
    public boolean upgrade(int level){
        int oldLevel=this.getChargedLevel();
        this.setChargedLevel(oldLevel+1);
        this.setPowerLevel(level);
        return this.getChargedLevel()!=oldLevel;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> p_20059_) {
        if (p_20059_.equals(CHARGED_LEVEL)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(p_20059_);
    }

    public int getPowerLevel() {
        return this.entityData.get(POWER_LEVEL);
    }

    public void setPowerLevel(int pPower){
        this.entityData.set(POWER_LEVEL,pPower);
    }
    public int getChargedLevel() {
        return this.entityData.get(CHARGED_LEVEL);
    }

    public void setChargedLevel(int pPower){
        this.entityData.set(CHARGED_LEVEL,pPower);
    }

    public boolean isCharging(){
        return this.entityData.get(IS_CHARGING);
    }
    public void setIsCharging(boolean isCharging){
        this.entityData.set(IS_CHARGING,isCharging);
    }
    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("powerLevel",this.getPowerLevel());
        pCompound.putInt("chargedLevel",this.getChargedLevel());
        pCompound.putBoolean("isCharging",this.isCharging());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setPowerLevel(pCompound.getInt("powerLevel"));
        this.setChargedLevel(pCompound.getInt("chargedLevel"));
        this.setIsCharging(pCompound.getBoolean("isCharging"));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(POWER_LEVEL,0);
        this.entityData.define(CHARGED_LEVEL,0);
        this.entityData.define(IS_CHARGING,false);
    }
}
