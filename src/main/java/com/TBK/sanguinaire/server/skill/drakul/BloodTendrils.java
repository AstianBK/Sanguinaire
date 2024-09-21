package com.TBK.sanguinaire.server.skill.drakul;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.skill.SkillAbstract;
import net.minecraft.world.entity.LivingEntity;

public class BloodTendrils extends SkillAbstract {
    public BloodTendrils() {
        super("blood_tendrils", 30,60, 100, 1, true, false, false, true, false,4);
    }

    @Override
    public void tick(SkillPlayerCapability player) {
        super.tick(player);
        if(!player.getPlayer().level().isClientSide){
            this.getTargets().forEach(target->{
                target.setDeltaMovement(0.0F,0.2f,0.0f);
            });
        }
    }

    @Override
    public void startSkillAbstract(SkillPlayerCapability skill) {
        super.startSkillAbstract(skill);
        skill.getPlayer().level().getEntitiesOfClass(LivingEntity.class,skill.getPlayer().getBoundingBox().inflate(10.0D),e->e!=skill.getPlayer()).forEach(this::addTarget);
    }
}
