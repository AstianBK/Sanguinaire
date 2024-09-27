package com.TBK.sanguinaire.server.entity.projetile;

import com.TBK.sanguinaire.common.registry.SGEffect;
import com.TBK.sanguinaire.common.registry.SGEntityType;
import com.TBK.sanguinaire.common.registry.SGParticles;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;


public class SlashBloodProjetile extends LeveableProjectile {
    public SlashBloodProjetile(EntityType<? extends ThrowableProjectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
        this.setNoGravity(true);
    }

    public SlashBloodProjetile(Level p_37249_,LivingEntity owner,int level) {
        super(SGEntityType.BLOOD_SLASH.get(), p_37249_);
        this.setNoGravity(true);
        this.setPowerLevel(level);
        this.setOwner(owner);
    }

    @Override
    protected float getGravity() {
        return 0.0F;
    }

    @Override
    protected void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);
        if(p_37259_.getEntity() instanceof LivingEntity living){
            if (living.hurt(damageSources().generic(), 3)){
                living.addEffect(new MobEffectInstance(SGEffect.INFINITY_BLEEDING.get(),100,this.getPowerLevel()));
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(this.animTick++>3){
            if(this.frame++>3){
                this.frame=0;
            }
            this.animTick=0;
        }
        this.setYRot(this.getYRot());
        this.setXRot(this.getXRot());
        if(this.level().isClientSide){
            Vec3 delta=this.getDeltaMovement();
            this.level().addParticle(SGParticles.BLOOD_TRAIL_PARTICLES.get(), this.getX()-delta.x, this.getY()-delta.y, this.getZ()-delta.z, 0.0F, 0.0F, 0.0F);
            if(this.tickCount%20==0){
                this.spawnParticles();
            }
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
