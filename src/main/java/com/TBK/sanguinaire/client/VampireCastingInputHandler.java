package com.TBK.sanguinaire.client;

import com.TBK.sanguinaire.Sanguinaire;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Sanguinaire.MODID, value = Dist.CLIENT)
public class VampireCastingInputHandler {

    private static final ResourceLocation ARROW_UP = new ResourceLocation(Sanguinaire.MODID, "textures/gui/arrow_up.png");
    private static final ResourceLocation ARROW_DOWN = new ResourceLocation(Sanguinaire.MODID, "textures/gui/arrow_down.png");
    private static final ResourceLocation ARROW_LEFT = new ResourceLocation(Sanguinaire.MODID, "textures/gui/arrow_left.png");
    private static final ResourceLocation ARROW_RIGHT = new ResourceLocation(Sanguinaire.MODID, "textures/gui/arrow_right.png");

    private static final ResourceLocation OVERLAY_CASTING = new ResourceLocation(Sanguinaire.MODID, "textures/gui/overlay_casting.png");

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        if (!VampireCastingHandler.isCasting()) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        Minecraft mc = Minecraft.getInstance();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        // Draw overlay texture
        RenderSystem.setShaderTexture(0, OVERLAY_CASTING);
        guiGraphics.blit(OVERLAY_CASTING, 0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);

        // Draw arrow inputs in the center of the screen
        int startX = screenWidth / 2 - 40;
        int startY = screenHeight / 2 - 40;
        int offset = 20;

        for (int i = 0; i < VampireCastingHandler.getInputSequence().size(); i++) {
            String input = VampireCastingHandler.getInputSequence().get(i);
            ResourceLocation texture = switch (input) {
                case "UP" -> ARROW_UP;
                case "DOWN" -> ARROW_DOWN;
                case "LEFT" -> ARROW_LEFT;
                case "RIGHT" -> ARROW_RIGHT;
                default -> null;
            };

            if (texture != null) {
                RenderSystem.setShaderTexture(0, texture);
                guiGraphics.blit(texture, startX + i * offset, startY, 0, 0, 16, 16, 16, 16);
            }
        }
    }
}
