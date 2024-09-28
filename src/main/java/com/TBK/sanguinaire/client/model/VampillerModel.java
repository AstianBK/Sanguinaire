package com.TBK.sanguinaire.client.model;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.server.entity.vampire.VampillerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class VampillerModel<T extends VampillerEntity> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return new ResourceLocation(Sanguinaire.MODID,"geo/vampiller.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return new ResourceLocation(Sanguinaire.MODID,"textures/entity/vampiller/vampiller_drakul.png");
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return new ResourceLocation(Sanguinaire.MODID,"animations/vampiller_drakul.animation.json");
    }
}
