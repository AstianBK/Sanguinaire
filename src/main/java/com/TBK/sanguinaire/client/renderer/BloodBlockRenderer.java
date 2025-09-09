package com.TBK.sanguinaire.client.renderer;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.common.block.BloodBlock;
import com.TBK.sanguinaire.common.block.BloodBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class BloodBlockRenderer implements BlockEntityRenderer<BloodBlockEntity> {
    private static final ModelResourceLocation MODEL_LOCATION = new ModelResourceLocation(Sanguinaire.MODID,"blood", "");

    public BloodBlockRenderer(BlockEntityRendererProvider.Context ctx) {
    }
    @Override
    public void render(BloodBlockEntity blockEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();


        if(blockEntity.getBlockState().getValue(BloodBlock.AGE_15)==0){
            var model = Minecraft.getInstance().getBlockRenderer()
                    .getBlockModelShaper()
                    .getModelManager()
                    .getModel(MODEL_LOCATION);


            BlockState state = blockEntity.getBlockState();
            poseStack.translate(0,-1,0);
            poseStack.scale(1.1F,1F,1.1F);
            poseStack.translate(-0.05,0,-0.05);
            Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
                    poseStack.last(),
                    bufferSource.getBuffer(RenderType.cutout()),
                    state,
                    model,
                    1.0F, 1.0F, 1.0F,
                    packedLight,
                    packedOverlay
            );

        }

        poseStack.popPose();
    }
}
