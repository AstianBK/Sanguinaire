package com.TBK.sanguinaire.server.network;

import com.TBK.sanguinaire.common.registry.SGParticles;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

public class HandlerParticles {
    public static void spawnCuts(LivingEntity living){
        Random random = new Random();
        for (int i = 0 ; i<5 ; i++){
            double box = living.getBbWidth();
            double xp = living.getX() + random.nextDouble(-box, box);
            double yp = living.getY() + random.nextDouble(0.0d, living.getBbHeight());
            double zp = living.getZ() + random.nextDouble(-box, box);
            living.level().addParticle(SGParticles.SLASH_PARTICLES.get(),xp,yp,zp,0.0F,0.0F,0.0F);
        }
        for (int i = 0 ; i<4 ; i++){
            double box = living.getBbWidth();
            double xp = living.getX() + random.nextDouble(-box, box);
            double yp = living.getY() + random.nextDouble(0.0d, living.getBbHeight());
            double zp = living.getZ() + random.nextDouble(-box, box);
            living.level().addParticle(SGParticles.SLICE_PARTICLES.get(),xp,yp,zp,1.0F,1.0F,1.0F);
        }
    }
    public static void spawnBlood(LivingEntity living){
        Random random = new Random();
        for (int i = 0 ; i<5 ; i++){
            double box = living.getBbWidth();
            double xp = living.getX() + random.nextDouble(-box, box);
            double yp = living.getY() + random.nextDouble(0.0d, living.getBbHeight());
            double zp = living.getZ() + random.nextDouble(-box, box);
            living.level().addParticle(SGParticles.BLOOD_PARTICLES.get(),xp,yp,zp,0.0F,0.0F,0.0F);
        }

    }
}
