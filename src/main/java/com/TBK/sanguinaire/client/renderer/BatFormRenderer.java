package com.TBK.sanguinaire.client.renderer;

import com.TBK.sanguinaire.client.model.BatModel;
import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

public class BatFormRenderer extends GeoReplacedEntityRenderer<Player, SkillPlayerCapability> implements GeoRenderer<SkillPlayerCapability> {
    public BatFormRenderer(EntityRendererProvider.Context renderManager, SkillPlayerCapability animatable) {
        super(renderManager, new BatModel<>(), animatable);
    }

    public void renderTransform(Entity entity, GeoEntity animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight){
        super.render((Player) entity,entityYaw,partialTick,poseStack,bufferSource,packedLight);
    }

}
