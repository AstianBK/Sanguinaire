package com.TBK.sanguinaire.client.gui;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.common.registry.SGAttribute;
import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import com.TBK.sanguinaire.server.skill.SkillAbstract;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.jetbrains.annotations.NotNull;

public class SkillOverlay implements IGuiOverlay {
    protected static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");
    private final Minecraft mc = Minecraft.getInstance();

    @Override
    public void render(ForgeGui gui, @NotNull GuiGraphics graphics, float partialTicks, int width, int height) {
        if (this.mc.player != null) {
            Player player = this.mc.player;
            SkillPlayerCapability cap=SkillPlayerCapability.get(player);
            if(cap!=null && cap.getPlayerVampire().isVampire()){
                int i = width / 2 -140;

                graphics.pose().pushPose();
                graphics.blit(WIDGETS_LOCATION, i - 91, height - 22, 0, 0, 81, 22);
                graphics.blit(WIDGETS_LOCATION, i - 91+ cap.getPosSelectSkillAbstract() *20, height - 22 , 0, 22, 24, 22);
                graphics.pose().popPose();

                for(int i1 = 0; i1 < 4; ++i1) {
                    SkillAbstract skillAbstract=cap.getHotBarSkill().get(i1);
                    int j1 = i - 90 + i1 * 20 + 2;
                    int k1 = height - 16 - 3;
                    if(!skillAbstract.equals(SkillAbstract.NONE)){
                        graphics.pose().pushPose();
                        graphics.blit(getIconsForSkill(skillAbstract), j1, k1 , 0,0,16, 16, 16, 16);
                        graphics.pose().popPose();

                        graphics.pose().pushPose();
                        float f = cap.getCooldowns().getCooldownPercent(skillAbstract);
                        if (f > 0.0F) {
                            int i2 = k1 + Mth.floor(16.0F * (1.0F-f));
                            int j2= i2 + Mth.floor(16.0F * f);
                            graphics.fill(RenderType.guiOverlay(), j1, i2, j1 + 16,  j2+5, Integer.MAX_VALUE);
                        }
                        graphics.pose().popPose();

                    }
                }

            }
        }
    }

    private ResourceLocation getIconsForSkill(SkillAbstract skillAbstract) {
        return new ResourceLocation(Sanguinaire.MODID,"textures/gui/skill/"+skillAbstract.name+"_icon.png");
    }
}
