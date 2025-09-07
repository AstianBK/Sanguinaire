package com.TBK.sanguinaire.server.skill;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public abstract class InstantSkill extends SkillAbstract{
    public InstantSkill(String name, int cooldown, int lauchTime, int costBloodBase) {
        super(name,10,1,cooldown,lauchTime,true,false,false,false,false,costBloodBase);
    }


}
