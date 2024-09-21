package com.TBK.sanguinaire.common.effects;

import com.TBK.sanguinaire.server.network.PacketHandler;
import com.TBK.sanguinaire.server.network.messager.PacketHandlerParticles;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BleedingEffects extends MobEffect {
    public BleedingEffects() {
        super(MobEffectCategory.HARMFUL, 4523433);
    }

}
