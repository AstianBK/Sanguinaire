package com.TBK.sanguinaire.server.skill.drakul;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.entity.projetile.BloodOrbProjetile;
import com.TBK.sanguinaire.server.entity.projetile.SlashBloodProjetile;
import com.TBK.sanguinaire.server.skill.ChargedSkill;
import com.TBK.sanguinaire.server.skill.SkillAbstract;

public class BloodOrb extends ChargedSkill {
    public BloodOrb() {
        super("blood_orb",20, 20, 1);
    }

    @Override
    public void startSkillAbstract(SkillPlayerCapability skill) {
        super.startSkillAbstract(skill);
    }

    @Override
    public void stopSkillAbstract(SkillPlayerCapability skill) {
        super.stopSkillAbstract(skill);
    }


    @Override
    public void summon(SkillPlayerCapability skill) {
        BloodOrbProjetile orb =new BloodOrbProjetile(skill.getPlayer().level(),skill.getPlayer(),this.level);
        orb.setPos(this.getPos(skill.getPlayer().getEyePosition(),skill.getPlayer()));
        reRot(orb,0.0F,skill.getPlayer().getYRot(),0.0F,1.0F,1.0F);
        orb.setIsCharging(true);
        skill.getPlayer().level().addFreshEntity(orb);
        this.castingProjectileId=orb.getId();
    }
}
