package com.TBK.sanguinaire.server.network;

import com.TBK.sanguinaire.common.registry.SGParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class HandlerParticles {
    public static void spawnCuts(LivingEntity living){
        Random random = new Random();
        for (int i = 0 ; i<2 ; i++){
            double box = living.getBbWidth();
            double xp = living.getX() + random.nextDouble(-box, box);
            double yp = living.getY() + random.nextDouble(0.0d, living.getBbHeight());
            double zp = living.getZ() + random.nextDouble(-box, box);
            living.level().addParticle(SGParticles.SLASH_PARTICLES.get(),xp,yp,zp,0.0F,0.0F,0.0F);
        }
        for (int i = 0 ; i<2 ; i++){
            double box = living.getBbWidth();
            double xp = living.getX() + random.nextDouble(-box, box);
            double yp = living.getY() + random.nextDouble(0.0d, living.getBbHeight());
            double zp = living.getZ() + random.nextDouble(-box, box);
            living.level().addParticle(SGParticles.SLICE_PARTICLES.get(),xp,yp,zp,0.0F,0.0F,0.0F);
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
    public static void spawnChargedBlood(Entity entity){
        Random random = new Random();
        Minecraft mc=Minecraft.getInstance();
        if(mc.level!=null){
            for (int i = 0 ; i<5 ; i++){
                double box = entity.getBbWidth()+0.75F;
                double d0=random.nextDouble(-box, box);
                double d1=random.nextDouble(0.0d, entity.getBbHeight());
                double d2=random.nextDouble(-box, box);
                double xp = entity.getX() + d0;
                double yp = entity.getY() + d1;
                double zp = entity.getZ() + d2;
                Particle blood= mc.particleEngine.createParticle(ParticleTypes.SMOKE,xp,yp,zp,-d0*0.1F,-d1*0.1F,-d2*0.1F);
                if(blood!=null){
                    blood.setColor(1.0F,0.0F,0.0F);
                }
            }
        }

    }
}
