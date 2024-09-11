package com.TBK.sanguinaire.client.layer;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.client.RenderUtil;
import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import com.TBK.sanguinaire.server.manager.RegenerationInstance;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class RegenerationLayer<T extends Player,M extends EntityModel<T>> extends RenderLayer<T,M>{
    private static final ResourceLocation SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/skeleton.png");
    private static final ResourceLocation MUSCLE_LOCATION = new ResourceLocation(Sanguinaire.MODID,"textures/entity/muscles.png");
    private static final ResourceLocation TRANS_LOCATION = new ResourceLocation(Sanguinaire.MODID,"textures/entity/trans.png");
    private static final RenderType DECAL_RENDER=RenderType.entityDecal(MUSCLE_LOCATION);
    private final HumanoidModel<T> model;
    private final HumanoidModel<T> modelHuman;
    private final HumanoidModel<T> modelHumanReg;
    public RegenerationLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
        this.model= new HumanoidModel<T>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.SKELETON));
        this.modelHuman= new HumanoidModel<T>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER));
        this.modelHumanReg= new HumanoidModel<T>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER));

    }
    @Override
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T player, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        VampirePlayerCapability cap=VampirePlayerCapability.get(player);
        if(cap!=null && player.isAlive()){
            if(cap.isVampire()){
                pMatrixStack.pushPose();
                float health=player.getHealth();
                float maxHealth=player.getMaxHealth();
                float f0=maxHealth*0.5F;
                float f1=maxHealth*0.25F;
                float f2=maxHealth*0.75F;
                float porcentReg=health/maxHealth;
                this.initModel(this.model,player,pLimbSwing,pLimbSwingAmount,pAgeInTicks,pNetHeadYaw,pHeadPitch);
                this.initModel(this.modelHuman,player,pLimbSwing,pLimbSwingAmount,pAgeInTicks,pNetHeadYaw,pHeadPitch);
                this.initModel(this.modelHumanReg,player,pLimbSwing,pLimbSwingAmount,pAgeInTicks,pNetHeadYaw,pHeadPitch);
                if(porcentReg<1.0F){
                    float porcentRegSkin=1.0F-(health-f2)/f1;
                    if(porcentRegSkin<=1.0F){
                        pMatrixStack.pushPose();
                        pMatrixStack.scale(0.9F,0.9F,0.9F);
                        VertexConsumer vertexConsumer4=pBuffer.getBuffer(RenderType.entityTranslucent(MUSCLE_LOCATION));
                        this.modelHuman.renderToBuffer(pMatrixStack,vertexConsumer4,pPackedLight,OverlayTexture.NO_OVERLAY,1.0F,1.0F,1.0F, 1F);
                        pMatrixStack.popPose();
                        VertexConsumer vertexConsumer=pBuffer.getBuffer(RenderType.dragonExplosionAlpha(TRANS_LOCATION));
                        this.modelHuman.renderToBuffer(pMatrixStack,vertexConsumer,pPackedLight,OverlayTexture.NO_OVERLAY,1.0F,1.0F,1.0F,porcentRegSkin);
                        VertexConsumer vertexConsumer2=pBuffer.getBuffer(RenderType.entityDecal(this.getTextureLocation(player)));
                        this.modelHuman.renderToBuffer(pMatrixStack,vertexConsumer2,pPackedLight,OverlayTexture.pack(0.0F, false),1.0F,1.0F,1.0F, 1F);
                    }
                    if(porcentReg<0.75){
                        float porcentRegMuscles=1.0F-(health-f1)/f2;
                        if(porcentRegMuscles<=1.0F){
                            pMatrixStack.pushPose();
                            VertexConsumer vertexConsumer1=pBuffer.getBuffer(RenderType.dragonExplosionAlpha(TRANS_LOCATION));
                            pMatrixStack.scale(0.9F,0.9F,0.9F);
                            this.modelHuman.renderToBuffer(pMatrixStack,vertexConsumer1,pPackedLight,OverlayTexture.NO_OVERLAY,1.0F,1.0F,1.0F,porcentRegMuscles);
                            VertexConsumer vertexConsumer3=pBuffer.getBuffer(DECAL_RENDER);
                            this.modelHuman.renderToBuffer(pMatrixStack,vertexConsumer3,pPackedLight,OverlayTexture.pack(0.0F, false),1.0F,1.0F,1.0F, 1F);
                            pMatrixStack.popPose();
                        }
                        pMatrixStack.pushPose();
                        pMatrixStack.scale(0.9F,0.9F,0.9F);
                        VertexConsumer vertexConsumer4=pBuffer.getBuffer(RenderType.entityTranslucent(SKELETON_LOCATION));
                        this.model.renderToBuffer(pMatrixStack,vertexConsumer4,pPackedLight,OverlayTexture.NO_OVERLAY,1.0F,1.0F,1.0F, 1F);
                        pMatrixStack.popPose();
                    }
                }
                pMatrixStack.popPose();
            }
        }
    }
    public void initModel(HumanoidModel<T> model, T player, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch){
        this.getParentModel().copyPropertiesTo((EntityModel<T>) model);
        model.prepareMobModel(player,pLimbSwing, 0.0F, 0.0F);
        model.setupAnim(player,pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);

    }
}