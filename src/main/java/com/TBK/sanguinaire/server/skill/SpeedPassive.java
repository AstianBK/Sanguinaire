package com.TBK.sanguinaire.server.skill;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SpeedPassive extends PassiveAbstract{
    public SpeedPassive() {
        super("speed_passive", 0);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,"91AEAA56-376B-4498-935B-2F7F68070635", (double)0.08F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE,"91AEAA56-376B-4498-935B-2F7F68070635", (double)0.05F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

}
