package com.TBK.sanguinaire.server.skill.drakul;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.entity.projetile.BloodOrbProjetile;
import com.TBK.sanguinaire.server.entity.projetile.LeveableProjectile;
import com.TBK.sanguinaire.server.entity.projetile.SlashBloodProjetile;
import com.TBK.sanguinaire.server.skill.ChargedSkill;
import com.TBK.sanguinaire.server.skill.SkillAbstract;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class BloodOrb extends ChargedSkill {
    public BloodOrb() {
        super("blood_orb",20, 20, 1);
    }


    @Override
    public void summon(SkillPlayerCapability skill) {

    }
    @Override
    public void startSkillAbstract(SkillPlayerCapability skill) {
        super.startSkillAbstract(skill);
        if(!skill.getPlayer().level().isClientSide){
            for(int i = -1 ; i<1 ;i++ ){
                BloodOrbProjetile orb = new BloodOrbProjetile(skill.getPlayer().level(),skill.getPlayer(),this.level);
                orb.setPos(this.getPos(skill.getPlayer().getEyePosition(),skill.getPlayer()));
                reRot(orb,0.0F,skill.getPlayer().getYRot(),0.0F,1.0F,1.0F);
                orb.setIsCharging(true);
                skill.getPlayer().level().addFreshEntity(orb);
                orb.setPos(this.getPos(skill.getPlayer().getEyePosition(),skill.getPlayer()));
                reRot(orb,skill.getPlayer().getXRot(),skill.getPlayer().getYRot()+30.0F*i,1.0F,1.0F);
                orb.setIsCharging(false);
                orb.setChargedLevel(6);
                orb.setPowerLevel(level);
                orb.refreshDimensions();
                orb.shootFromRotation(skill.getPlayer(),skill.getPlayer().getXRot(),skill.getPlayer().getYRot(), 0.0F, 1F, 1.0F);
            }
        }
    }
    @Override
    public List<String> getSequence() {
        List<String> sequenceRequest = new ArrayList<>();
        sequenceRequest.add("LEFT");
        sequenceRequest.add("UP");
        sequenceRequest.add("LEFT");
        return sequenceRequest;
    }
}
