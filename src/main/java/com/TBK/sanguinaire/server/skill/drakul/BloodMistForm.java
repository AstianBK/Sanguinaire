package com.TBK.sanguinaire.server.skill.drakul;

import com.TBK.sanguinaire.common.registry.SGParticles;
import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.skill.SkillAbstract;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3f;

public class BloodMistForm extends SkillAbstract {

    private static final String TAG_TIMER = "SG_BloodMistTimer";
    private static final int DURATION_TICKS = 20 * 30;

    public BloodMistForm() {
        super(
                "blood_mist_form",
                DURATION_TICKS, 0, 0, 0, false, true, false, false, false, 4
        );
    }


    @Override
    public void startSkillAbstract(SkillPlayerCapability skill) {
        super.startSkillAbstract(skill);

        Player player = skill.getPlayer();
        player.getPersistentData().putInt(TAG_TIMER, DURATION_TICKS);

        if (!player.level().isClientSide) {
            player.level().playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.ILLUSIONER_CAST_SPELL,
                    SoundSource.PLAYERS,
                    1.0F,
                    0.8F
            );
        }
    }

    @Override
    public void effectSkillAbstractForTick(SkillPlayerCapability skill) {
        Player player = skill.getPlayer();
        int timeLeft = player.getPersistentData().getInt(TAG_TIMER);

        if (timeLeft <= 0) {
            player.removeEffect(MobEffects.INVISIBILITY);
            return;
        }

        player.getPersistentData().putInt(TAG_TIMER, timeLeft - 1);

        if (!player.level().isClientSide) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.INVISIBILITY, 40, 0, false, false, false
            ));

            player.setInvulnerable(true);
            player.setDeltaMovement(
                    player.getDeltaMovement().multiply(1.15D, 1.0D, 1.15D)
            );
        }

        if (player.level().isClientSide) {
            float radius = 1.1F;
            int count = Mth.ceil((float) Math.PI * radius * radius * 2.5F);

            for (int k = 0; k < 2; k++) {
                for (int i = 0; i < count; i++) {
                    float angle = player.getRandom().nextFloat() * ((float) Math.PI * 2F);
                    float dist = Mth.sqrt(player.getRandom().nextFloat()) * radius;

                    double x = player.getX() + Mth.cos(angle) * dist;
                    double y = player.getY() + 0.5D + player.getRandom().nextDouble() * 0.5D;
                    double z = player.getZ() + Mth.sin(angle) * dist;

                    player.level().addParticle(
                            SGParticles.BLOOD_DOT_PARTICLES.get(),
                            x, y, z,
                            0.0D, 0.02D, 0.0D
                    );

                    player.level().addParticle(
                            new DustParticleOptions(
                                    new org.joml.Vector3f(0.6F, 0.0F, 0.0F),
                                    1.4F
                            ),
                            x, y, z,
                            0.0D, 0.01D, 0.0D
                    );
                }
            }
        }
    }

        @Override
    public void stopSkillAbstract(SkillPlayerCapability skill) {
        Player player = skill.getPlayer();

        player.removeEffect(MobEffects.INVISIBILITY);
        player.setInvulnerable(false);
        player.getPersistentData().remove(TAG_TIMER);

        super.stopSkillAbstract(skill);
    }

    @Override
    public java.util.List<String> getSequence() {
        return java.util.List.of("UP", "LEFT", "DOWN");
    }
}
