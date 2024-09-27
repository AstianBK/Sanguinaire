package com.TBK.sanguinaire.server.skill;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

public class BatForm extends TransformSkill{
    public BatForm() {
        super("transform_bat", 50, 1, true, 0);
    }

    @Override
    public Forms getForm() {
        return Forms.BAT;
    }

    @Override
    public void tick(SkillPlayerCapability skill) {
        super.tick(skill);
        skill.getPlayer().getAbilities().flying=true;
        skill.getPlayer().onUpdateAbilities();
    }

    @Override
    public void startSkillAbstract(SkillPlayerCapability skill) {
        super.startSkillAbstract(skill);
        skill.getPlayer().getAbilities().flying=true;
        skill.getPlayer().onUpdateAbilities();
        if(skill.getPlayer().level().isClientSide){
            this.spawnSmoke(skill.getPlayer());
        }
    }
    public void spawnSmoke(Player player){
        Random random=new Random();
        player.playSound(SoundEvents.FIRE_EXTINGUISH,1.0f,-1.0f/(random.nextFloat() * 0.4F + 0.8F));
        for (int i = 0; i < 24; i++) {
            double x1 = player.getX();
            double x2 = player.getY();
            double x3 = player.getZ();
            player.level().addParticle(ParticleTypes.LARGE_SMOKE, x1, x2, x3,
                    random.nextFloat(-0.1f, 0.1f),
                    0.1f,
                    random.nextFloat(-0.1f, 0.1f));
        }
    }

    @Override
    public void stopSkillAbstract(SkillPlayerCapability skill) {
        super.stopSkillAbstract(skill);
        skill.getPlayer().getAbilities().flying=false;
        skill.getPlayer().onUpdateAbilities();
        if(skill.getPlayer().level().isClientSide){
            this.spawnSmoke(skill.getPlayer());
        }
    }
}
