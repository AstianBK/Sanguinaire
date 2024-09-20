package com.TBK.sanguinaire.server.skill.drakul;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.entity.projetile.SlashBloodProjetile;
import com.TBK.sanguinaire.server.skill.SkillAbstract;

public class BloodSlash extends SkillAbstract {
    public BloodSlash() {
        super("blood_slash", 0 ,20, 20, 1, true, false, false, true, false,2);
    }

    @Override
    public void startSkillAbstract(SkillPlayerCapability skill) {
        super.startSkillAbstract(skill);
    }

    @Override
    public void stopSkillAbstract(SkillPlayerCapability skill) {
        super.stopSkillAbstract(skill);
        if(!skill.getPlayer().level().isClientSide){
            SlashBloodProjetile slashBlood =new SlashBloodProjetile(skill.getPlayer().level(),skill.getPlayer(),this.level);
            slashBlood.setPos(skill.getPlayer().getEyePosition());
            slashBlood.shootFromRotation(skill.getPlayer(),skill.getPlayer().getXRot(),skill.getPlayer().getYRot(), 0.0F, 1.0F, 1.0F);
            skill.getPlayer().level().addFreshEntity(slashBlood);
        }
    }
}
