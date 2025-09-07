package com.TBK.sanguinaire.client.renderer;

import com.TBK.sanguinaire.common.block.CoffinBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class CoffinBlockEntityRenderer implements BlockEntityRenderer<CoffinBlockEntity> {
    private static final ResourceLocation MODEL_LOCATION = new ResourceLocation("sanguinaire:block/stonecoffin");

    public CoffinBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(CoffinBlockEntity blockEntity, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();


        var model = Minecraft.getInstance().getBlockRenderer()
                .getBlockModelShaper()
                .getModelManager()
                .getModel(MODEL_LOCATION);


        BlockState state = blockEntity.getBlockState();
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
                poseStack.last(),
                bufferSource.getBuffer(RenderType.cutout()),
                state,
                model,
                1.0F, 1.0F, 1.0F,
                packedLight,
                packedOverlay
        );

        poseStack.popPose();
    }

}

