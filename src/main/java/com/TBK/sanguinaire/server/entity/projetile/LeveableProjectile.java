package com.TBK.sanguinaire.server.entity.projetile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;

public abstract class LeveableProjectile extends ThrowableProjectile {
    private static final EntityDataAccessor<Integer> POWER_LEVEL =
            SynchedEntityData.defineId(LeveableProjectile.class, EntityDataSerializers.INT);

    protected LeveableProjectile(EntityType<? extends ThrowableProjectile> p_37466_, Level p_37467_) {
        super(p_37466_, p_37467_);
    }

    public int getPowerLevel() {
        return this.entityData.get(POWER_LEVEL);
    }

    public void setPowerLevel(int pPower){
        this.entityData.set(POWER_LEVEL,pPower);
    }
    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("powerLevel",this.getPowerLevel());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setPowerLevel(pCompound.getInt("powerLevel"));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(POWER_LEVEL,0);
    }
}
