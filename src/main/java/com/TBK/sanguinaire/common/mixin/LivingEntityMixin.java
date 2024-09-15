package com.TBK.sanguinaire.common.mixin;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;)V",at = @At("TAIL"))
    public void onSwing(InteractionHand p_21007_, CallbackInfo ci){
        if (((Object)this) instanceof Player player) {
            SkillPlayerCapability cap=SkillPlayerCapability.get(player);
            if(cap!=null){
                cap.swingHand(player);
            }
        }
    }

}
