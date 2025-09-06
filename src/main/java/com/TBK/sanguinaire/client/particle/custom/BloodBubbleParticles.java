package com.TBK.sanguinaire.client.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;



public class BloodBubbleParticles extends TextureSheetParticle {
    private final SpriteSet sprites;

    BloodBubbleParticles(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSprite) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.friction = 0.96F;
        this.sprites = pSprite;
        this.scale(1.0F);
        this.hasPhysics = false;
        this.setSpriteFromAge(pSprite);
    }

    public int getLightColor(float pPartialTick) {
        return 240;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BloodBubbleParticles bk_particles=new BloodBubbleParticles(world,x,y,z,xSpeed,ySpeed,zSpeed,this.spriteSet);
            bk_particles.pickSprite(this.spriteSet);
            return bk_particles;
        }
    }
}