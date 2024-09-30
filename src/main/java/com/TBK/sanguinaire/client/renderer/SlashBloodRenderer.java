package com.TBK.sanguinaire.client.renderer;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.server.entity.projetile.SlashBloodProjetile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SlashBloodRenderer<T extends SlashBloodProjetile> extends EntityRenderer<T> {

    public SlashBloodRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        if(!pEntity.isCharging()){
            pMatrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot())));
            pMatrixStack.mulPose(Axis.XP.rotationDegrees(-Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot())));
            pMatrixStack.mulPose(Axis.ZP.rotationDegrees((float) Math.sin(pEntity.life * 0.15F)*10));
        }else {
            pMatrixStack.mulPose(Axis.YP.rotationDegrees(pEntity.getYRot()));
        }

        float width=0.5F+pEntity.getBbWidth()*pEntity.getChargedLevel()/10;

        pMatrixStack.translate(0.0,0.01F,0.0F);
        PoseStack.Pose posestack$pose = pMatrixStack.last();
        drawSlash(posestack$pose,pEntity,pBuffer,pPackedLight,width,4);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        return new ResourceLocation(Sanguinaire.MODID,"textures/entity/slash/crescent_slash_"+p_114482_.getFrame()+".png");
    }
    private void drawSlash(PoseStack.Pose pose, T entity, MultiBufferSource bufferSource, int light, float width, int offset) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(getTextureLocation(entity)));
        float halfWidth = width * .5f;
        consumer.vertex(poseMatrix, -halfWidth, -0.1f, -halfWidth).color(255, 255, 255, 255).uv(0f, 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, halfWidth, -0.1f, -halfWidth).color(255, 255, 255, 255).uv(1f, 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, halfWidth, -0.1f, halfWidth).color(255, 255, 255, 255).uv(1f, 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, -halfWidth, -0.1f, halfWidth).color(255, 255, 255, 255).uv(0f, 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0f, 1f, 0f).endVertex();
    }
}
