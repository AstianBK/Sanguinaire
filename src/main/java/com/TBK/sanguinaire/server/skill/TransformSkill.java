package com.TBK.sanguinaire.server.skill;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public abstract class TransformSkill extends SkillAbstract{
    public TransformSkill(String name,int cooldown,int lauchTime,boolean canReActive,int costBloodBase) {
        super(name,9999999,cooldown,lauchTime,true,true,canReActive,true,false,costBloodBase);
    }

    @Override
    public void startSkillAbstract(SkillPlayerCapability skill) {
        super.startSkillAbstract(skill);
        System.out.print("\n"+(skill.getPlayer().level().isClientSide ? "Client" : "Server")+"\n");
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
    public void updateAttributes(Player player) {
        this.addAttributeModifiers(player,player.getAttributes(),4);
    }

    public abstract Forms getForm();
}
