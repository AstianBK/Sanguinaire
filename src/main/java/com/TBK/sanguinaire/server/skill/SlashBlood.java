package com.TBK.sanguinaire.server.skill;

import com.TBK.sanguinaire.common.registry.SGEntityType;
import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.entity.projetile.SlashBloodProjetile;

public class SlashBlood extends SkillAbstract{
    public SlashBlood() {
        super("slash_blood", 5, 20, 1, true, false, false, false, false,2);
    }

    @Override
    public void startSkillAbstract(SkillPlayerCapability skill) {
        super.startSkillAbstract(skill);
        if(!skill.getPlayer().level().isClientSide){
            SlashBloodProjetile slashBlood =new SlashBloodProjetile(SGEntityType.BLOOD_SLASH.get(),skill.getPlayer().level());
            slashBlood.setPos(skill.getPlayer().getEyePosition());
            slashBlood.shootFromRotation(skill.getPlayer(),skill.getPlayer().getXRot(),skill.getPlayer().getYRot(), 0.0F, 0.75F, 1.0F);
            skill.getPlayer().level().addFreshEntity(slashBlood);
        }
    }
}
