package com.TBK.sanguinaire.common.mixin;

import com.TBK.sanguinaire.client.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class EntityRenderDispatcherMixin<T extends Entity> {

    @Shadow @Final private RenderBuffers renderBuffers;

    @Inject(method = "renderLevel",at = @At("TAIL"))
    public void renderTentacle(PoseStack p_109600_, float p_109601_, long p_109602_, boolean p_109603_, Camera p_109604_, GameRenderer p_109605_, LightTexture p_109606_, Matrix4f p_254120_, CallbackInfo ci){
        if(p_109604_.getEntity()==Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType().isFirstPerson()){
            MultiBufferSource.BufferSource multibuffersource$buffersource = this.renderBuffers.bufferSource();
            p_109600_.translate(0.0F,-0.2F,0.0F);
            RenderUtil.render(p_109600_,multibuffersource$buffersource, Minecraft.getInstance().player,p_109601_);
        }
    }
}
