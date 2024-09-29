package com.TBK.sanguinaire.server.capability;

import com.TBK.sanguinaire.common.api.IBiterEntity;
import com.TBK.sanguinaire.server.entity.vampire.VampillerEntity;
import com.TBK.sanguinaire.server.network.HandlerParticles;
import com.TBK.sanguinaire.server.network.PacketHandler;
import com.TBK.sanguinaire.server.network.messager.PacketSyncBloodEntity;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Slime;
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
    public int regBlood=0;
    @Override
    public boolean canBiter() {
        return this.currentEntity!=null && this.currentEntity.getMobType()!= MobType.UNDEAD && !(this.currentEntity instanceof VampillerEntity) &&
                !(this.currentEntity instanceof AbstractGolem) && !(this.currentEntity instanceof Slime) && !this.unBlooded();
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
            this.regBlood=0;
            if(target.level().isClientSide){
                HandlerParticles.spawnBlood((LivingEntity) target);
            }
        }
    }
    public void regBlood(int blood){
        int lastBlood=Math.min(this.getBlood()+blood,this.getMaxBlood());
        this.setBlood(lastBlood);
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
            if(!this.unBlooded()){
                if(this.getBlood()<this.getMaxBlood()){
                    if(this.regBlood++>6000){
                        this.regBlood(2);
                        this.regBlood=0;
                    }
                }
            }
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
        tag.putInt("regBlood",this.regBlood);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.setBlood(nbt.getInt("blood"));
        this.regBlood=nbt.getInt("regBlood");
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
