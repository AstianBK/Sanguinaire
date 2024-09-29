package com.TBK.sanguinaire.server.skill;

import com.TBK.sanguinaire.common.registry.SGSounds;
import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.entity.projetile.LeveableProjectile;
import com.TBK.sanguinaire.server.entity.projetile.SlashBloodProjetile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;


public abstract class ChargedSkill extends SkillAbstract {
    public int castingProjectileId=-1;
    public ChargedSkill(String name,int castingDuration,int cooldown,int costBloodBase) {
        super(name, 0 ,castingDuration, cooldown, 1, false, false, false, true, false,costBloodBase);
    }

    @Override
    public void startSkillAbstract(SkillPlayerCapability skill) {
        super.startSkillAbstract(skill);
        if(!skill.getPlayer().level().isClientSide){
            this.summon(skill);
        }
    }

    public abstract void summon(SkillPlayerCapability skill);

    @Override
    public void tick(SkillPlayerCapability skill) {
        super.tick(skill);
        Entity entity=skill.getPlayer().level().getEntity(this.castingProjectileId);
        if(skill.getCastingTimer()%10==0){
            if(entity instanceof LeveableProjectile projectile){
                if(skill.getPlayerVampire().loseBlood(1)){
                    skill.getPlayer().level().playSound(null,skill.getPlayer(), SoundEvents.CHICKEN_DEATH, SoundSource.PLAYERS,1.0F,1.0F);
                    int level=skill.getPlayerVampire().age/5;
                    projectile.upgrade(level);
                    if(!skill.getPlayer().level().isClientSide){
                        projectile.level().broadcastEntityEvent(projectile,(byte) 4);
                    }
                }else {
                    skill.stopCasting(skill.getPlayer());
                }

            }
        }
        if(!skill.getPlayer().level().isClientSide){
            if(entity instanceof  LeveableProjectile projectile){
                projectile.setPos(this.getPos(skill.getPlayer().getEyePosition(),skill.getPlayer()));
                reRot(projectile,skill.getPlayer().getXRot(),skill.getPlayer().getYRot(),1.0F,1.0F);
            }
        }
    }


    public Vec3 getPos(Vec3 initialVec, Player player){
        float f1 = player.getYHeadRot() * Mth.DEG_TO_RAD;
        float f2 = Mth.sin(f1);
        float f3 = Mth.cos(f1);
        float f5 = player.getViewXRot(1.0F);
        float f4 = (float) ((f5+90.0F) * (double) Mth.DEG_TO_RAD);
        if(f5<0.0F){
            f5 =-f5;
        }
        float f6 =Mth.lerp(f5/90.0F,1.5F,0.0F);
        f4 = Mth.cos(f4);
        f2 = f2*f6;
        f3 = f3*f6;
        return initialVec.add(-f2,f4-0.35F,f3);
    }

    @Override
    public void stopSkillAbstract(SkillPlayerCapability skill) {
        super.stopSkillAbstract(skill);
        if(!skill.getPlayer().level().isClientSide){
            Entity entity=skill.getPlayer().level().getEntity(this.castingProjectileId);
            if(entity instanceof LeveableProjectile projectile){
                projectile.setIsCharging(false);
                projectile.shootFromRotation(skill.getPlayer(),skill.getPlayer().getXRot(),skill.getPlayer().getYRot(), 0.0F, 1.0F, 1.0F);
                this.castingProjectileId=-1;
            }
        }
    }
    public void reRot(LeveableProjectile projectile,float x, float y, float vel, float miss){
        float f = -Mth.sin(y * ((float)Math.PI / 180F)) * Mth.cos(x * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((x) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(y * ((float)Math.PI / 180F)) * Mth.cos(x * ((float)Math.PI / 180F));
        reRot(projectile,f,f1,f2,miss,vel);
    }
    public void reRot(LeveableProjectile projectile,double x, double y, double z, float vel, float miss) {
        Vec3 vec3 = (new Vec3(x, y, z)).normalize().add(projectile.level().random.triangle(0.0D, 0.0172275D * (double) miss), projectile.level().random.triangle(0.0D, 0.0172275D * (double) miss), projectile.level().random.triangle(0.0D, 0.0172275D * (double) miss)).scale((double) vel);
        double d0 = vec3.horizontalDistance();
        projectile.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
        projectile.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
        projectile.yRotO = projectile.getYRot();
        projectile.xRotO = projectile.getXRot();
    }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        this.castingProjectileId=tag.getInt("idSummon");
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("idSummon",this.castingProjectileId);
        return super.save(tag);
    }
}
