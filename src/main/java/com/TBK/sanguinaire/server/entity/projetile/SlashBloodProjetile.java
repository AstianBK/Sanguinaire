package com.TBK.sanguinaire.server.entity.projetile;

import com.TBK.sanguinaire.common.registry.SGEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class SlashBloodProjetile extends ThrowableProjectile {
    public SlashBloodProjetile(EntityType<? extends ThrowableProjectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
        this.setNoGravity(true);
    }

    @Override
    protected float getGravity() {
        return 0.0F;
    }

    @Override
    protected void defineSynchedData() {

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
            if(living.hurt(damageSources().generic(),3));
            living.addEffect(new MobEffectInstance(SGEffect.INFINITY_BLEEDING.get(),100,0));
        }
    }

    @Override
    public void tick() {
        super.tick();
    }
}
