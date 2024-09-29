package com.TBK.sanguinaire.client.model;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.server.entity.vampire.VampillerEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class VampillerModel<T extends VampillerEntity> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        if(animatable.isBat()){
            return new ResourceLocation(Sanguinaire.MODID,"geo/vampillerbat.geo.json");
        }else {
            return new ResourceLocation(Sanguinaire.MODID,"geo/vampiller.geo.json");
        }
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        if(animatable.isBat()){
            return new ResourceLocation(Sanguinaire.MODID,"textures/entity/vampiller/vampiller_batform.png");
        }else {
            return new ResourceLocation(Sanguinaire.MODID,"textures/entity/vampiller/vampiller_drakul.png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return animatable.isBat() ? null : new ResourceLocation(Sanguinaire.MODID,"animations/vampiller_drakul.animation.json") ;
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if(animatable.isBat()){
            GeoBone head= (GeoBone) this.getAnimationProcessor().getBone("Head");
            GeoBone body= (GeoBone) this.getAnimationProcessor().getBone("body");
            GeoBone wingR= (GeoBone) this.getAnimationProcessor().getBone("rightWing");
            GeoBone wingL= (GeoBone) this.getAnimationProcessor().getBone("leftWing");
            GeoBone wingTipR= (GeoBone) this.getAnimationProcessor().getBone("rightWingTip");
            GeoBone wingTipL= (GeoBone) this.getAnimationProcessor().getBone("leftWingTip");

            float headPitch=animationState.getData(DataTickets.ENTITY_MODEL_DATA).headPitch();
            float headYaw=animationState.getData(DataTickets.ENTITY_MODEL_DATA).netHeadYaw();
            float partialTick=animationState.getPartialTick();
            float limbSwing=animationState.getLimbSwing();
            head.setRotX(headPitch * ((float)Math.PI / 180F));
            head.setRotY(headYaw * ((float)Math.PI / 180F));
            body.setRotX(((float)Math.PI / 4F) + Mth.cos( limbSwing * 0.1F) * 0.15F);
            wingR.setRotY(Mth.cos(limbSwing * 74.48451F * ((float)Math.PI / 180F)) * (float)Math.PI * 0.25F);
            wingL.setRotY(-wingR.getRotY());
            wingTipR.setRotY(wingR.getRotY() * 0.5F);
            wingTipL.setRotY(-wingR.getRotY() * 0.5F);
        }
    }
}
