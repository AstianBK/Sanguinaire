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
        boolean flag=player.getPlayer().tickCount%5==0;
        if(!player.getPlayer().level().isClientSide){
            this.getTargets().forEach(target->{
                target.setDeltaMovement(0.0F,0.2f,0.0f);
                if(flag){
                    if(target.hurt(player.getPlayer().damageSources().magic(),3)){
                        player.getPlayer().setHealth(player.getPlayer().getHealth()+3);
                        player.getPlayerVampire().drainBlood(1);
                    }
                }
            });
        }
        if(flag){
            this.extraCooldown+=50;
        }
    }

    @Override
    public void stopSkillAbstract(SkillPlayerCapability skill) {
        this.getTargets().clear();
        skill.getCooldowns().addCooldown(this,this.cooldown+this.extraCooldown);
    }

    @Override
    public void startSkillAbstract(SkillPlayerCapability skill) {
        super.startSkillAbstract(skill);
        int level=skill.getPlayerVampire().age/10;
        skill.getPlayer().level().getEntitiesOfClass(LivingEntity.class,skill.getPlayer().getBoundingBox().inflate(10.0D),e->e!=skill.getPlayer()).forEach(e->{
            if(this.getTargets().size()<3+level){
                this.addTarget(e);
            }
        });
        this.extraCooldown=0;
    }
}
