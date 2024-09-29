package com.TBK.sanguinaire.server.entity.projetile;

import com.TBK.sanguinaire.common.registry.SGEffect;
import com.TBK.sanguinaire.common.registry.SGEntityType;
import com.TBK.sanguinaire.common.registry.SGParticles;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;


public class SlashBloodProjetile extends LeveableProjectile {
    @Nullable
    private IntOpenHashSet piercingIgnoreEntityIds;
    @Nullable
    private List<Entity> piercedAndKilledEntities;
    private int life=0;

    public SlashBloodProjetile(EntityType<? extends ThrowableProjectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
        this.setNoGravity(true);
        this.life=300;
    }

    public SlashBloodProjetile(Level p_37249_,LivingEntity owner,int level) {
        super(SGEntityType.BLOOD_SLASH.get(), p_37249_);
        this.setNoGravity(true);
        this.setPowerLevel(level);
        this.setOwner(owner);
        this.life=300;
    }

    @Override
    protected float getGravity() {
        return 0.0F;
    }

    @Override
    protected void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
        if(this.getChargedLevel()<5){
            this.discard();
        }
    }

    @Override
    protected boolean canHitEntity(Entity p_37250_) {
        return super.canHitEntity(p_37250_) && (this.piercingIgnoreEntityIds == null || !this.piercingIgnoreEntityIds.contains(p_37250_.getId()));
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);
        if(p_37259_.getEntity() instanceof LivingEntity living){
            if (this.piercingIgnoreEntityIds == null) {
                this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
            }

            if (this.piercedAndKilledEntities == null) {
                this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
            }

            if (this.piercingIgnoreEntityIds.size() >= 7) {
                this.discard();
                return;
            }

            this.piercingIgnoreEntityIds.add(p_37259_.getEntity().getId());

            if (living.hurt(damageSources().indirectMagic(this,this.getOwner()), 1+1.0F*this.getChargedLevel()+0.5F*this.getPowerLevel())){
                if (!living.isAlive() && this.piercedAndKilledEntities != null) {
                    this.piercedAndKilledEntities.add(living);
                }
                living.addEffect(new MobEffectInstance(SGEffect.INFINITY_BLEEDING.get(),100,this.getPowerLevel()));
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(this.animTick++>2){
            if(this.frame++>3){
                this.frame=0;
            }
            this.animTick=0;
        }
        this.setYRot(this.getYRot());
        this.setXRot(this.getXRot());
        if(this.level().isClientSide && !this.isCharging()){
            Vec3 delta=this.getDeltaMovement();
            this.level().addParticle(SGParticles.BLOOD_TRAIL_PARTICLES.get(), this.getX()-delta.x, this.getY()-delta.y, this.getZ()-delta.z, 0.0F, 0.0F, 0.0F);
            if(this.tickCount%20==0){
                this.spawnParticles();
            }
        }
        if(this.life--<=0){
            this.discard();
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
