package com.TBK.sanguinaire.client.model;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BatModel<T extends SkillPlayerCapability> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return new ResourceLocation(Sanguinaire.MODID,"geo/batform.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return new ResourceLocation(Sanguinaire.MODID,"textures/entity/batform/batform_vampire.png");
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return new ResourceLocation(Sanguinaire.MODID,"animations/batform.animation.json");
    }
}
