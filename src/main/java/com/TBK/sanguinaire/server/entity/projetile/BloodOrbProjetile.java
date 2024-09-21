package com.TBK.sanguinaire.server.entity.projetile;

import com.TBK.sanguinaire.common.registry.SGEffect;
import com.TBK.sanguinaire.common.registry.SGEntityType;
import com.TBK.sanguinaire.common.registry.SGItems;
import com.TBK.sanguinaire.common.registry.SGParticles;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;


public class BloodOrbProjetile extends LeveableProjectile implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(BloodOrbProjetile.class, EntityDataSerializers.ITEM_STACK);
    private int discardTimer=0;
    public BloodOrbProjetile(EntityType<? extends ThrowableProjectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
        this.setNoGravity(true);
        this.discardTimer=200;
    }

    public BloodOrbProjetile(Level p_37249_, LivingEntity owner, int level) {
        super(SGEntityType.BLOOD_ORB.get(), p_37249_);
        this.setNoGravity(true);
        this.setPowerLevel(level);
        this.setOwner(owner);
        this.discardTimer=200;
    }
    public void setItem(ItemStack p_37447_) {
        if (!p_37447_.is(this.getDefaultItem()) || p_37447_.hasTag()) {
            this.getEntityData().set(DATA_ITEM_STACK, p_37447_.copyWithCount(1));
        }

    }

    protected Item getDefaultItem(){
        return SGItems.ORB.get();
    }

    protected ItemStack getItemRaw() {
        return new ItemStack(SGItems.ORB.get());
    }
    @Override
    public ItemStack getItem() {
        ItemStack itemstack = this.getItemRaw();
        return itemstack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemstack;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    public void addAdditionalSaveData(CompoundTag p_37449_) {
        super.addAdditionalSaveData(p_37449_);
        ItemStack itemstack = this.getItemRaw();
        if (!itemstack.isEmpty()) {
            p_37449_.put("Item", itemstack.save(new CompoundTag()));
        }

    }

    public void readAdditionalSaveData(CompoundTag p_37445_) {
        super.readAdditionalSaveData(p_37445_);
        ItemStack itemstack = ItemStack.of(p_37445_.getCompound("Item"));
        this.setItem(itemstack);
    }

    @Override
    protected float getGravity() {
        return 0.0F;
    }

    @Override
    protected void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
        if(!this.level().isClientSide){
            this.level().broadcastEntityEvent(this,(byte) 2);
        }
        this.discard();
    }

    @Override
    public void handleEntityEvent(byte p_19882_) {
        if(p_19882_==2){
            this.level().addParticle(SGParticles.BLOOD_EXPLOSION_PARTICLES.get(), this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F, 0.0F);
        }
        super.handleEntityEvent(p_19882_);
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);
        if(p_37259_.getEntity() instanceof LivingEntity living){
            living.hurt(damageSources().indirectMagic(this,this.getOwner()), 5+1.5F*this.getPowerLevel());
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(this.discardTimer--<=0){
            this.discard();
        }
        if(this.level().isClientSide){
            Vec3 delta=this.getDeltaMovement();
            this.level().addParticle(SGParticles.BLOOD_TRAIL_PARTICLES.get(), this.getX()-delta.x, this.getY()-delta.y, this.getZ()-delta.z, 0.0F, 0.0F, 0.0F);
        }
    }
    public void spawnParticles(){
        float width = (float) getBoundingBox().getXsize();
        float step = 0.25f;
        float radians = Mth.DEG_TO_RAD * getYRot();
        float speed = 0.1f;
        for (int i = 0; i < width / step; i++) {
            double x = getX();
            double y = getY();
            double z = getZ();
            double offset = step * (i - width / step / 2);
            double rotX = offset * Math.cos(radians);
            double rotZ = -offset * Math.sin(radians);

            double dx = Math.random() * speed * 2 - speed;
            double dy = Math.random() * speed * 2 - speed;
            double dz = Math.random() * speed * 2 - speed;
            this.level().addParticle(SGParticles.BLOOD_TRAIL_PARTICLES.get(), x + rotX + dx, y + dy, z + rotZ + dz, dx, dy, dz);
        }
    }
}
