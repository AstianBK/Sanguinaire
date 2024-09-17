package com.TBK.sanguinaire.common.effects;

import com.TBK.sanguinaire.common.registry.SGParticles;
import com.TBK.sanguinaire.server.network.PacketHandler;
import com.TBK.sanguinaire.server.network.messager.PacketHandlerParticles;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

public class InfinityBleedingEffects extends MobEffect {
    public InfinityBleedingEffects() {
        super(MobEffectCategory.HARMFUL, 12444);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        int j = 25 >> 2+p_19456_;
        if (j > 0) {
            return p_19455_ % j == 0;
        } else {
            return true;
        }
    }

    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        super.applyEffectTick(p_19467_, p_19468_);
        if(!p_19467_.level().isClientSide){
            p_19467_.hurt(p_19467_.damageSources().generic(),5);
            PacketHandler.sendToAllTracking(new PacketHandlerParticles(0,p_19467_),p_19467_);
        }
    }
}
