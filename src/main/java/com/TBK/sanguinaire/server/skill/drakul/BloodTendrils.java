package com.TBK.sanguinaire.server.skill.drakul;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.skill.SkillAbstract;
import net.minecraft.world.entity.LivingEntity;

public class BloodTendrils extends SkillAbstract {
    private int extraCooldown=0;
    public BloodTendrils() {
        super("blood_tendrils", 30,60, 100, 1, true, false, false, true, false,4);
    }

    @Override
    public void tick(SkillPlayerCapability player) {
        super.tick(player);
        if(!player.getPlayer().level().isClientSide){
            this.getTargets().forEach(target->{
                target.setDeltaMovement(0.0F,0.2f,0.0f);
                if(player.getPlayer().tickCount%5==0){
                    if(target.hurt(player.getPlayer().damageSources().magic(),3)){
                        player.getPlayer().heal(3);
                        player.getPlayerVampire().drainBlood(1);
                        this.extraCooldown+=20;
                    }
                }
            });
        }
    }

    @Override
    public void stopSkillAbstract(SkillPlayerCapability skill) {
        this.getTargets().clear();
        skill.getCooldowns().addCooldown(this,this.cooldown+this.extraCooldown);
        this.extraCooldown=0;
    }

    @Override
    public void startSkillAbstract(SkillPlayerCapability skill) {
        super.startSkillAbstract(skill);
        skill.getPlayer().level().getEntitiesOfClass(LivingEntity.class,skill.getPlayer().getBoundingBox().inflate(10.0D),e->e!=skill.getPlayer()).forEach(this::addTarget);
    }
}
