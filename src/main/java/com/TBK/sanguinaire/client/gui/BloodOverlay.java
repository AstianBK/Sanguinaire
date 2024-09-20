package com.TBK.sanguinaire.client.gui;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.common.registry.SGAttribute;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.jetbrains.annotations.NotNull;

import javax.management.Attribute;

public class BloodOverlay implements IGuiOverlay {
    private final ResourceLocation icons = new ResourceLocation(Sanguinaire.MODID ,"textures/gui/icons_vampire.png");
    private final Minecraft mc = Minecraft.getInstance();

    @Override
    public void render(ForgeGui gui, @NotNull GuiGraphics graphics, float partialTicks, int width, int height) {
        if (this.mc.player != null) {
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 0.01);
            VampirePlayerCapability cap=VampirePlayerCapability.get(this.mc.player);
            assert cap!=null;
            if (this.mc.gameMode.hasExperience() && this.mc.player.isAlive() && cap.isVampire()) {
                int blood = (int) this.mc.player.getAttribute(SGAttribute.BLOOD_VALUE.get()).getValue();
                int left = this.mc.getWindow().getGuiScaledWidth() / 2 + 91;
                int top = this.mc.getWindow().getGuiScaledHeight() - ((ForgeGui) this.mc.gui).rightHeight+10;
                ((ForgeGui) this.mc.gui).rightHeight += 10;
                for (int i = 0; i < 10; ++i) {
                    int idx = i * 2 + 1;
                    int x = left - i * 8 - 9;
                    graphics.blit(icons, x, top, 0,  27, 9, 9);

                    if (idx < blood) {
                        graphics.blit(icons, x, top, 18, 27, 9, 9);
                    } else if (idx == blood) {
                        graphics.blit(icons, x, top, 9, 27, 9, 9);
                    }
                }
            }
            graphics.pose().popPose();
        }
    }
}
