package com.TBK.sanguinaire.client.layer;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.server.Util;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class VampireLayer<T extends Player,M extends EntityModel<T>> extends EyesLayer<T, M> {
    private static final RenderType VAMPIRE_EYES = RenderType.eyes(new ResourceLocation(Sanguinaire.MODID,"textures/eyes/vamp_eyes_1.png"));

    public VampireLayer(RenderLayerParent<T, M> p_116981_) {
        super(p_116981_);
    }

    @Override
    public void render(PoseStack p_116983_, MultiBufferSource p_116984_, int p_116985_, T p_116986_, float p_116987_, float p_116988_, float p_116989_, float p_116990_, float p_116991_, float p_116992_) {
        if(Util.isVampire(p_116986_)){
            super.render(p_116983_, p_116984_, p_116985_, p_116986_, p_116987_, p_116988_, p_116989_, p_116990_, p_116991_, p_116992_);
        }
    }

    @Override
    public RenderType renderType() {
        return VAMPIRE_EYES;
    }
}
