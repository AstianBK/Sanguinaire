package com.TBK.sanguinaire.client.renderer;

import com.TBK.sanguinaire.client.model.VampillerModel;
import com.TBK.sanguinaire.server.entity.vampire.VampillerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VampillerRenderer<T extends VampillerEntity> extends GeoEntityRenderer<T>  {
    public VampillerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VampillerModel<>());
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float f=entity.isBat() ? 0.5f : 1.0F;
        poseStack.scale(f,f,f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
