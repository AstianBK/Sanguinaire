package com.TBK.sanguinaire.client.model;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.server.entity.summon.BloodSpikesEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BloodSpikesModel<T extends BloodSpikesEntity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Sanguinaire.MODID, "blood_spikes"), "main");
	private final ModelPart truemain;
	private final ModelPart main;
	private final ModelPart spike1;
	private final ModelPart spike2;
	private final ModelPart spike3;

	public BloodSpikesModel(ModelPart root) {
		this.truemain = root.getChild("truemain");
		this.main = this.truemain.getChild("main");
		this.spike1 = this.main.getChild("spike1");
		this.spike2 = this.main.getChild("spike2");
		this.spike3 = this.main.getChild("spike3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition truemain = partdefinition.addOrReplaceChild("truemain", CubeListBuilder.create(), PartPose.offset(-5.0F, 12.0F, -5.0F));

		PartDefinition main = truemain.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(5.0F, 4.2667F, 4.6667F));

		PartDefinition spike1 = main.addOrReplaceChild("spike1", CubeListBuilder.create(), PartPose.offset(0.0F, -6.7667F, 0.3333F));

		PartDefinition spike1_r1 = spike1.addOrReplaceChild("spike1_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -15.0F, 0.0F, 12.0F, 30.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7849F, 1.5091F, 0.7859F));

		PartDefinition spike2 = main.addOrReplaceChild("spike2", CubeListBuilder.create(), PartPose.offset(3.0F, 3.3833F, -0.1667F));

		PartDefinition spike2_r1 = spike2.addOrReplaceChild("spike2_r1", CubeListBuilder.create().texOffs(24, -5).addBox(0.0F, -4.5F, -2.5F, 0.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0097F, 0.218F, 0.0447F));

		PartDefinition spike3 = main.addOrReplaceChild("spike3", CubeListBuilder.create(), PartPose.offset(-3.0F, 3.3833F, -0.1667F));

		PartDefinition spike3_r1 = spike3.addOrReplaceChild("spike3_r1", CubeListBuilder.create().texOffs(24, -5).mirror().addBox(0.0F, -4.5F, -2.5F, 0.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0097F, -0.218F, -0.0447F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float pLimbSwing, float pLimbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = pLimbSwing * 2.0F;
		if (f > 1.0F) {
			f = 1.0F;
		}

		f = 1.0F - f * f * f;
		float f1 = (pLimbSwing + Mth.sin(pLimbSwing * 2.7F)) * 0.6F * 12.0F;
		this.truemain.y = 24.0F - f1;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		truemain.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}