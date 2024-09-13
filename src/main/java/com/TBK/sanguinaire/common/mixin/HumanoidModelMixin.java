package com.TBK.sanguinaire.common.mixin;

import com.TBK.sanguinaire.client.RenderUtil;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import com.TBK.sanguinaire.server.manager.LimbsPartRegeneration;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> {
    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",at = @At("TAIL"))
    public void regeneration$Layer(T p_102866_, float p_102867_, float p_102868_, float p_102869_, float p_102870_, float p_102871_, CallbackInfo ci){
        if (((Object)this) instanceof PlayerModel<?> model){
            VampirePlayerCapability cap=VampirePlayerCapability.get((Player) p_102866_);
            if(cap!=null){
                LimbsPartRegeneration regeneration=cap.getLimbsPartRegeneration();
                if(regeneration!=null){
                    regeneration.getLimbs().forEach(e->{
                        List<ModelPart> part= RenderUtil.getModelPartForLimbs(e,model);
                        if(part!=null){
                            part.forEach(k->k.visible=false);
                        }
                    });
                }
            }
        }
    }
}
