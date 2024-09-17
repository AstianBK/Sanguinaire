package com.TBK.sanguinaire.server.skill;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import net.minecraft.world.entity.LivingEntity;

public class TentacleBlood extends SkillAbstract{
    public TentacleBlood() {
        super("tentacle_blood", 30, 300, 1, true, false, false, false, false,4);
    }

    @Override
    public void tick(SkillPlayerCapability player) {
        super.tick(player);
        if(!player.getPlayer().level().isClientSide){
            this.getTargets().forEach(target->{
                target.setDeltaMovement(0.0F,0.1f,0.0f);
            });
        }
    }

    @Override
    public void startSkillAbstract(SkillPlayerCapability skill) {
        super.startSkillAbstract(skill);
        skill.getPlayer().level().getEntitiesOfClass(LivingEntity.class,skill.getPlayer().getBoundingBox().inflate(10.0D),e->e!=skill.getPlayer()).forEach(this::addTarget);
    }
}
