package com.TBK.sanguinaire.server.capability;

import com.TBK.sanguinaire.common.api.IBiterEntity;
import com.TBK.sanguinaire.server.network.HandlerParticles;
import com.TBK.sanguinaire.server.network.PacketHandler;
import com.TBK.sanguinaire.server.network.messager.PacketSyncBloodEntity;
import com.TBK.sanguinaire.server.network.messager.PacketSyncBloodLiving;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BiterEntityCap implements IBiterEntity {
    public LivingEntity currentEntity;
    public int blood=0;
    @Override
    public boolean canBiter() {
        return false;
    }

    @Override
    public void setCurrentEntity(LivingEntity currentEntity) {
        this.currentEntity=currentEntity;
    }

    @Override
    public LivingEntity getCurrentEntity() {
        return this.currentEntity;
    }

    @Override
    public int getMaxBlood() {
        return (int) (5+5*this.currentEntity.getBbWidth());
    }

    @Override
    public int getBlood() {
        return this.blood;
    }

    @Override
    public void setBlood(int blood) {
        this.blood=blood;
    }

    @Override
    public void onBite(VampirePlayerCapability biter, Entity target) {
        if(biter.isVampire){
            int lastBlood=Math.max(this.getBlood()-1,0);
            this.setBlood(lastBlood);
            HandlerParticles.spawnBlood((LivingEntity) target);
        }
    }


    public boolean unBlooded(){
        return this.getBlood()<=0;
    }

    @Override
    public void tick(LivingEntity living) {
        if(this.unBlooded() && living.tickCount%20==0){
            living.hurt(living.damageSources().generic(),5);
        }
        if(!living.level().isClientSide){
            PacketHandler.sendToAllTracking(new PacketSyncBloodEntity(this.getBlood(), living), (LivingEntity) living);
        }
    }

    public void init(LivingEntity entity){
        this.setCurrentEntity(entity);
        this.setBlood(this.getMaxBlood());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("blood",this.getBlood());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.setBlood(nbt.getInt("blood"));
    }

    public static class BiterEntityPlayerProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
        private final LazyOptional<IBiterEntity> instance = LazyOptional.of(BiterEntityCap::new);

        @NonNull
        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return SGCapability.ENTITY_CAPABILITY.orEmpty(cap,instance.cast());
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.orElseThrow(NullPointerException::new).serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
        }
    }
}
