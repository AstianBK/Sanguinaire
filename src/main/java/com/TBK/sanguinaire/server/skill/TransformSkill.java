package com.TBK.sanguinaire.server.skill;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public abstract class TransformSkill extends SkillAbstract{
    public TransformSkill(String name,int cooldown,int lauchTime,boolean canReActive,int costBloodBase) {
        super(name,999999,1,cooldown,lauchTime,true,true,canReActive,false,false,costBloodBase);
    }

    @Override
    public void startSkillAbstract(SkillPlayerCapability skill) {
        super.startSkillAbstract(skill);
        skill.setIsTransform(true);
        skill.setForm(this.getForm());
    }

    @Override
    public void stopSkillAbstract(SkillPlayerCapability skill) {
        super.stopSkillAbstract(skill);
        this.removeAttributeModifiers(skill.getPlayer(),skill.getPlayer().getAttributes(),4);
        skill.setIsTransform(false);
        skill.setForm(Forms.NONE);
    }

    public double getAttributeModifierValue(int p_19457_, AttributeModifier p_19458_) {
        return p_19458_.getAmount() * (double)(p_19457_ + 1);
    }

    @Override
    public void updateAttributes(SkillPlayerCapability player) {
        this.addAttributeModifiers(player.getPlayer(),player.getPlayer().getAttributes(),4);
    }

    public abstract Forms getForm();
}
