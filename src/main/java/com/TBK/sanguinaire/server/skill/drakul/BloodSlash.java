package com.TBK.sanguinaire.server.skill.drakul;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.entity.projetile.LeveableProjectile;
import com.TBK.sanguinaire.server.entity.projetile.SlashBloodProjetile;
import com.TBK.sanguinaire.server.skill.ChargedSkill;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;


public class BloodSlash extends ChargedSkill {
    public BloodSlash() {
        super("blood_slash",80, 60,2);
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
    public List<String> getSequence() {
        List<String> sequenceRequest = new ArrayList<>();
        sequenceRequest.add("RIGHT");
        sequenceRequest.add("DOWN");
        sequenceRequest.add("LEFT");
        return sequenceRequest;
    }
}
