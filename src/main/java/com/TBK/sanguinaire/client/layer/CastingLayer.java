package com.TBK.sanguinaire.client.layer;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;


@OnlyIn(Dist.CLIENT)
public class CastingLayer<T extends Player,M extends EntityModel<T>> extends RenderLayer<T,M>{
    private static final ResourceLocation BLANK_LOCATION = new ResourceLocation(Sanguinaire.MODID,"textures/entity/blood_rune_blank.png");

    public CastingLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);

    }

    @Override
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        SkillPlayerCapability cap = SkillPlayerCapability.get(pEntity);
        if(cap!=null){
            if(cap.getCastingClientTimer()>0){
                int i=cap.getMaxCastingClientTimer();
                int rest= (int) (i*0.65F);
                int k= (int) (i*0.35F);
                float timer= (1.0F- Mth.clamp(((float) cap.getCastingClientTimer()-rest) / k,0.0F,1.0F));
                pMatrixStack.pushPose();
                pMatrixStack.mulPose(Axis.YP.rotationDegrees(((float)pEntity.tickCount+pPartialTicks)*10.0F));

                float width=6*timer;
                pMatrixStack.translate(0.0F,1.5F,0.0F);
                PoseStack.Pose posestack$pose = pMatrixStack.last();
                drawSlash(posestack$pose,pEntity,pBuffer,pPackedLight,width,4);
                pMatrixStack.popPose();

            }
        }
    }

    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        return BLANK_LOCATION;
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