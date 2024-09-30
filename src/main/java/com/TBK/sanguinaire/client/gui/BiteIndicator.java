package com.TBK.sanguinaire.client.gui;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.common.registry.SGEffect;
import com.TBK.sanguinaire.server.Util;
import com.TBK.sanguinaire.server.capability.BiterEntityCap;
import com.TBK.sanguinaire.server.capability.SGCapability;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
@OnlyIn(Dist.CLIENT)
public class BiteIndicator implements IGuiOverlay {
    private final ResourceLocation ICONS = new ResourceLocation(Sanguinaire.MODID ,"textures/gui/icons_vampire.png");

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        assert player != null;
        if(Util.isVampire(player) && mc.crosshairPickEntity!=null){
            if(mc.options.getCameraType().isFirstPerson()){
                BiterEntityCap cap= SGCapability.getEntityEntity(mc.crosshairPickEntity, BiterEntityCap.class);
                if(cap!=null && cap.canBiter()){
                    float blood= (float) cap.getBlood() /cap.getMaxBlood();
                    int j = screenHeight / 2 + 4 ;
                    int k = screenWidth / 2 + 3;
                    RenderSystem.enableBlend();
                    RenderSystem.setShaderColor(1.0F,0,0,0.8F);
                    guiGraphics.blit(ICONS, k, j, 0, 18, 18, 9);
                    guiGraphics.setColor(1.0F,1.0F,1.0F,1.0F);
                    guiGraphics.blit(ICONS, k, j, 0, 18, 18,9-(int) (Math.ceil(9.0F*blood)));
                    RenderSystem.disableBlend();
                }
            }
        }
    }

}
