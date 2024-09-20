package com.TBK.sanguinaire.client.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class BloodExplosionParticles extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected BloodExplosionParticles(ClientLevel p_108484_, double p_108485_, double p_108486_, double p_108487_, double xSpeed, double ySpeed, double zSpeed , SpriteSet spriteSet) {
        super(p_108484_, p_108485_, p_108486_, p_108487_);
        this.sprites=spriteSet;
        this.xd *= 0.1;
        this.yd *= 0.1;
        this.zd *= 0.1;
        this.xd += xSpeed;
        this.yd += ySpeed;
        this.zd += zSpeed;
        this.setColor(1.0f,0.0f,0.0f);
        this.scale(2.0F);
        this.lifetime=10;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ > this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
            if (!this.onGround) {
                this.yd -= (double)this.gravity;
                this.move(this.xd, this.yd, this.zd);
                this.xd *= (double)0.98F;
                this.yd *= (double)0.98F;
                this.zd *= (double)0.98F;
            }
        }
    }

    public float getQuadSize(float p_105642_) {
        return this.quadSize * Mth.clamp(((float)this.age + p_105642_) / (float)this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;

    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BloodExplosionParticles bk_particles=new BloodExplosionParticles(world,x,y,z,xSpeed,ySpeed,zSpeed,this.spriteSet);
            bk_particles.pickSprite(this.spriteSet);
            return bk_particles;
        }
    }
}
