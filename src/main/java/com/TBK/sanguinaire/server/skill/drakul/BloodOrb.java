package com.TBK.sanguinaire.server.skill.drakul;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.entity.projetile.BloodOrbProjetile;
import com.TBK.sanguinaire.server.entity.projetile.SlashBloodProjetile;
import com.TBK.sanguinaire.server.skill.SkillAbstract;

public class BloodOrb extends SkillAbstract {
    public BloodOrb() {
        super("blood_orb", 0 ,20, 20, 1, true, false, false, true, false,1);
    }

    @Override
    public void startSkillAbstract(SkillPlayerCapability skill) {
        super.startSkillAbstract(skill);
    }

    @Override
    public void stopSkillAbstract(SkillPlayerCapability skill) {
        super.stopSkillAbstract(skill);
        if(!skill.getPlayer().level().isClientSide){
            BloodOrbProjetile orb =new BloodOrbProjetile(skill.getPlayer().level(),skill.getPlayer(),this.level);
            orb.setPos(skill.getPlayer().getEyePosition());
            orb.shootFromRotation(skill.getPlayer(),skill.getPlayer().getXRot(),skill.getPlayer().getYRot(), 0.0F, 1.0F, 1.0F);
            skill.getPlayer().level().addFreshEntity(orb);
        }
    }
}
