package com.TBK.sanguinaire.client.layer;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.client.RenderUtil;
import com.TBK.sanguinaire.common.mixin.PlayerModelAccessor;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import com.TBK.sanguinaire.server.manager.RegenerationInstance;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
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
    private static final ResourceLocation MUSCLE_SLIM_LOCATION = new ResourceLocation(Sanguinaire.MODID,"textures/entity/muscles_slim.png");
    private static final ResourceLocation TRANS_LOCATION = new ResourceLocation(Sanguinaire.MODID,"textures/entity/trans.png");
    private static final ResourceLocation TRANS_SLIM_LOCATION = new ResourceLocation(Sanguinaire.MODID,"textures/entity/trans_slim.png");

    private final HumanoidModel<T> model;
    private final HumanoidModel<T> modelHuman;
    private final HumanoidModel<T> modelHumanReg;
    private boolean isSlim=false;
    public RegenerationLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
        if(pRenderer.getModel() instanceof PlayerModelAccessor<?> model){
            this.isSlim=model.isSlimModel();
        }
        this.model= new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.SKELETON));
        this.modelHuman= new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER));
        this.modelHumanReg= new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER));

    }
    @Override
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T player, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        VampirePlayerCapability cap=VampirePlayerCapability.get(player);
        if(cap!=null && player.isAlive()){
            if(cap.isVampire()){
                if(cap.getLimbsPartRegeneration().hasRegenerationLimbs()){
                    pMatrixStack.pushPose();
                    this.initModel(this.model,player,pLimbSwing,pLimbSwingAmount,pAgeInTicks,pNetHeadYaw,pHeadPitch);
                    this.initModel(this.modelHuman,player,pLimbSwing,pLimbSwingAmount,pAgeInTicks,pNetHeadYaw,pHeadPitch);
                    this.initModel(this.modelHumanReg,player,pLimbSwing,pLimbSwingAmount,pAgeInTicks,pNetHeadYaw,pHeadPitch);
                    cap.getLimbsPartRegeneration().getLimbs().forEach(limb->{
                        RegenerationInstance instance=cap.getLimbsPartRegeneration().loseLimbs.get(limb.name().toLowerCase());
                        int res= (int) (instance.getRegerationTimer()*0.75F);
                        int res1= (int) ((int) instance.getRegerationTimerRemaining()*0.25F);
                        float porcent=((float) instance.getRegerationTimerRemaining()-res)/ res1;
                        float porcentReg= 1.0F-porcent;
                        ModelPart part= RenderUtil.getModelPartForLimbs(limb, this.model);
                        if(porcentReg>0.0F){
                            part.xScale=0.9F;
                            part.yScale=0.9F;
                            part.zScale=0.9F;
                            VertexConsumer vertexConsumer2=pBuffer.getBuffer(RenderType.entityTranslucent(SKELETON_LOCATION));
                            part.render(pMatrixStack,vertexConsumer2,pPackedLight,OverlayTexture.NO_OVERLAY,1.0F,1.0F,1.0F, 1F);
                        }
                    });
                    if(!cap.getLimbsPartRegeneration().getLimbsMuscle().isEmpty()){
                        cap.getLimbsPartRegeneration().getLimbsMuscle().forEach(limb->{
                            RegenerationInstance instance=cap.getLimbsPartRegeneration().loseLimbs.get(limb.name().toLowerCase());
                            int res= (int) (instance.getRegerationTimer()*0.25F);
                            int res1= (int) ((int) instance.getRegerationTimerRemaining()*0.5F);
                            float porcent=((float) instance.getRegerationTimerRemaining()-res)/ res1;
                            ModelPart part= RenderUtil.getModelPartForLimbs(limb, this.modelHuman);
                            if(porcent>0.0F){
                                VertexConsumer vertexConsumer1=pBuffer.getBuffer(RenderType.dragonExplosionAlpha(this.getTransLocation()));
                                part.render(pMatrixStack,vertexConsumer1,pPackedLight,OverlayTexture.NO_OVERLAY,1.0F,1.0F,1.0F,porcent);
                                VertexConsumer vertexConsumer2=pBuffer.getBuffer(RenderType.entityDecal(this.getMuscleLocation()));
                                part.render(pMatrixStack,vertexConsumer2,pPackedLight,OverlayTexture.pack(0.0F, false),1.0F,1.0F,1.0F, 1F);
                            }else {
                                ModelPart part1=RenderUtil.getModelPartForLimbs(limb,this.modelHumanReg);
                                int res2= (int) (instance.getRegerationTimer()*0.25F);
                                float porcent1=((float) instance.getRegerationTimerRemaining())/ res2;
                                part1.xScale=0.9F;
                                part1.yScale=0.9F;
                                part1.zScale=0.9F;
                                VertexConsumer vertexConsumer1=pBuffer.getBuffer(RenderType.entityTranslucent(this.getMuscleLocation()));
                                part1.render(pMatrixStack,vertexConsumer1,pPackedLight,OverlayTexture.NO_OVERLAY,1.0F,1.0F,1.0F,1.0F);
                                VertexConsumer vertexConsumer=pBuffer.getBuffer(RenderType.dragonExplosionAlpha(this.getTransLocation()));
                                part.render(pMatrixStack,vertexConsumer,pPackedLight,OverlayTexture.NO_OVERLAY,1.0F,1.0F,1.0F,porcent1);
                                VertexConsumer vertexConsumer2=pBuffer.getBuffer(RenderType.entityDecal(this.getTextureLocation(player)));
                                part.render(pMatrixStack,vertexConsumer2,pPackedLight,OverlayTexture.pack(0.0F, false),1.0F,1.0F,1.0F, 1F);
                            }
                        });
                    }

                    pMatrixStack.popPose();
                }
            }
        }
    }
    public ResourceLocation getMuscleLocation(){
        return this.isSlim ? MUSCLE_SLIM_LOCATION : MUSCLE_LOCATION;
    }
    public ResourceLocation getTransLocation(){
        return this.isSlim ? TRANS_SLIM_LOCATION : TRANS_LOCATION;
    }
    public void initModel(HumanoidModel<T> model, T player, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch){
        this.getParentModel().copyPropertiesTo((EntityModel<T>) model);
        model.prepareMobModel(player,pLimbSwing, 0.0F, 0.0F);
        model.setupAnim(player,pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);

    }
}