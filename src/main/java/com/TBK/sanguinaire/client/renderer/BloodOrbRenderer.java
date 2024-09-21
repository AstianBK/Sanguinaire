package com.TBK.sanguinaire.client.renderer;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.server.entity.projetile.BloodOrbProjetile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BloodOrbRenderer<T extends BloodOrbProjetile> extends ThrownItemRenderer<T> {

    public BloodOrbRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.scale(2.0F,2.0F,2.0F);
        super.render(pEntity, pEntityYaw, pPackedLight, pMatrixStack, pBuffer, pPackedLight);
    }
}
