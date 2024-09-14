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
            VampirePlayerCapability cap=VampirePlayerCapability.get(this.mc.player);
            assert cap!=null;
            if (this.mc.gameMode.hasExperience() && this.mc.player.isAlive() && cap.isVampire()) {
                int blood = (int) this.mc.player.getAttribute(SGAttribute.BLOOD_VALUE.get()).getValue();
                int left = this.mc.getWindow().getGuiScaledWidth() / 2 + 91;
                int top = this.mc.getWindow().getGuiScaledHeight() - ((ForgeGui) this.mc.gui).rightHeight;
                ((ForgeGui) this.mc.gui).rightHeight += 10;
                int maxBlood = 20;
                int blood2 = blood - 20;
                int maxBlood2 = maxBlood - 20;
                for (int i = 0; i < 10; ++i) {
                    int idx = i * 2 + 1;
                    int x = left - i * 8 - 9;

                    // Draw Background
                    graphics.blit(icons, x, top, 0, idx <= maxBlood2 ? 9 : 0, 9, 9);

                    if (idx < blood) {
                        graphics.blit(icons, x, top, 9, idx < blood2 ? 9 : 0, 9, 9);
                        if (idx == blood2) {
                            graphics.blit(icons, x, top, 18, 9, 9, 9);
                        }
                    } else if (idx == blood) {
                        graphics.blit(icons, x, top, 18, 0, 9, 9);
                    }
                }
            }
        }
    }
}
