package com.TBK.sanguinaire.server.skill.drakul;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.entity.projetile.LeveableProjectile;
import com.TBK.sanguinaire.server.entity.projetile.SlashBloodProjetile;
import com.TBK.sanguinaire.server.skill.ChargedSkill;
import net.minecraft.world.entity.Entity;



public class BloodSlash extends ChargedSkill {
    public BloodSlash() {
        super("blood_slash",80, 60,2);
    }

    @Override
    public void startSkillAbstract(SkillPlayerCapability skill) {
        super.startSkillAbstract(skill);
    }

    @Override
    public void summon(SkillPlayerCapability skill) {
        SlashBloodProjetile slashBlood =new SlashBloodProjetile(skill.getPlayer().level(),skill.getPlayer(),this.level);
        slashBlood.setPos(this.getPos(skill.getPlayer().getEyePosition(),skill.getPlayer()));
        reRot(slashBlood,skill.getPlayer().getXRot(),skill.getPlayer().getYRot(),0.0F,1.0F,1.0F);
        slashBlood.setIsCharging(true);
        skill.getPlayer().level().addFreshEntity(slashBlood);
        this.castingProjectileId=slashBlood.getId();
    }

    @Override
    public void tick(SkillPlayerCapability skill) {
        super.tick(skill);
        if(!skill.getPlayer().level().isClientSide){
            Entity entity=skill.getPlayer().level().getEntity(this.castingProjectileId);
            if(entity instanceof  LeveableProjectile projectile){
                projectile.setPos(this.getPos(skill.getPlayer().getEyePosition(),skill.getPlayer()));
                reRot(projectile,skill.getPlayer().getXRot(),skill.getPlayer().getYRot(),1.0F,1.0F);
            }
        }
    }

    @Override
    public void stopSkillAbstract(SkillPlayerCapability skill) {
        super.stopSkillAbstract(skill);
    }
}
