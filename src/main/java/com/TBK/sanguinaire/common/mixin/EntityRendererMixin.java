package com.TBK.sanguinaire.common.mixin;

import com.TBK.sanguinaire.common.api.Limbs;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import com.TBK.sanguinaire.server.manager.RegenerationInstance;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {

    @Inject(method = "shouldRender",at = @At("HEAD"), cancellable = true)
    public void should$Render(T p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_, CallbackInfoReturnable<Boolean> cir){
        cir.cancel();
        cir.setReturnValue(true);
    }
    @Inject(method = "getRenderOffset",at = @At("HEAD"), cancellable = true)
    public void posRender(T p_114483_, float p_114484_, CallbackInfoReturnable<Vec3> cir){
        if(p_114483_ instanceof Player player){
            VampirePlayerCapability cap =VampirePlayerCapability.get(player);
            if(cap !=null && cap.getLimbsPartRegeneration()!=null){
                double d0=0.0F;
                if(cap.legsLess()){
                    RegenerationInstance instanceLegR=cap.getLimbsPartRegeneration().loseLimbs.get(Limbs.RIGHT_LEG.name().toLowerCase());
                    RegenerationInstance instanceLegL=cap.getLimbsPartRegeneration().loseLimbs.get(Limbs.LEFT_LEG.name().toLowerCase());
                    RegenerationInstance instance=instanceLegR.getRegerationTimerRemaining()<instanceLegL.getRegerationTimerRemaining() ? instanceLegL : instanceLegR ;
                    int res= (int) this.calculeRegeneration(instance.getRegerationTimer());
                    float porcentReg= Math.max(((float) instance.getRegerationTimerRemaining()-res)/ res,0.0F);
                    d0+=(0.8F*porcentReg);
                }
                if(cap.bodyLess()){
                    RegenerationInstance instanceBody=cap.getLimbsPartRegeneration().loseLimbs.get(Limbs.BODY.name().toLowerCase());
                    int res= this.calculeRegeneration(instanceBody.getRegerationTimer());
                    float porcentReg= Math.max(((float) instanceBody.getRegerationTimerRemaining()-res)/ res,0.0F);
                    d0+=(0.5F*porcentReg);
                }
                cir.setReturnValue(new Vec3(0.0F,-d0,0.0F));
            }
        }
    }
    public int calculeRegeneration(int timer){
        return (int) (timer*0.5F);
    }
}
