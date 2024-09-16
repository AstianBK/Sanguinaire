package com.TBK.sanguinaire.client.gui;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.Random;


@OnlyIn(Dist.CLIENT)
public class HeartsEffect implements IGuiOverlay {
    long healthBlinkTime = 0;
    long lastHealthTime = 0;
    long lastHealth=0;
    int displayHealth;

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (!gui.shouldDrawSurvivalElements()) return;
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        assert player != null;
        VampirePlayerCapability cap=VampirePlayerCapability.get(player);
        assert cap!=null;
        if(cap.isVampire()){
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 0.01);

            int health = Mth.ceil(player.getHealth());
            float absorb = Mth.ceil(player.getAbsorptionAmount());
            AttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
            float healthMax = (float)attrMaxHealth.getValue();


            int ticks = gui.getGuiTicks();
            boolean highlight = this.healthBlinkTime > (long)ticks && (this.healthBlinkTime - (long)ticks) / 3L % 2L == 1L;

            if (health < this.lastHealth && player.invulnerableTime > 0)
            {
                this.lastHealthTime = Util.getMillis();
                this.healthBlinkTime = (long) (ticks + 20);
            }
            else if (health > this.lastHealth && player.invulnerableTime > 0)
            {
                this.lastHealthTime = Util.getMillis();
                this.healthBlinkTime = (long) (ticks + 10);
            }

            if (Util.getMillis() - this.lastHealthTime > 1000L)
            {
                this.lastHealth = health;
                this.displayHealth = health;
                this.lastHealthTime = Util.getMillis();
            }

            this.lastHealth = health;
            int healthLast = this.displayHealth;

            float f = Math.max((float)player.getAttributeValue(Attributes.MAX_HEALTH), (float)Math.max(health, healthLast));
            int regen = -1;
            if (player.hasEffect(MobEffects.REGENERATION)){
                regen = ticks % Mth.ceil(f + 5.0F);
            }

            Random rand = new Random();
            rand.setSeed((long) (ticks * 312871));

            int absorptionHearts = Mth.ceil(absorb / 2.0f) - 1;
            int hearts = Mth.ceil(healthMax / 2.0f) - 1;
            int healthRows = Mth.ceil((healthMax + absorb) / 2.0F / 10.0F);
            int totalHealthRows = Mth.ceil((healthMax + absorb) / 2.0F / 10.0F);
            int rowHeight = Math.max(10 - (healthRows - 2), 3);
            int extraHealthRows = totalHealthRows - healthRows;
            int extraRowHeight = Mth.clamp(10 - (healthRows - 2), 3, 10);

            int left = screenWidth / 2 - 91;
            int top = screenHeight - ((ForgeGui)Minecraft.getInstance().gui).leftHeight + healthRows * rowHeight;
            if (rowHeight != 10){
                top += 10 - rowHeight;
            }

            gui.leftHeight += extraHealthRows * extraRowHeight;

            ResourceLocation texture=new ResourceLocation(Sanguinaire.MODID, "textures/gui/icons_vampire.png");
            for (int i = absorptionHearts + hearts; i > absorptionHearts + hearts; -- i) {
                int row = (i + 1) / 10;
                int heart = (i + 1) % 10;
                int x = left + heart * 8;
                int y = top - extraRowHeight * Math.max(0, row - healthRows + 1) - rowHeight * Math.min(row, healthRows - 1);
                guiGraphics.blit(texture, x, y,  highlight ? 0 : 9, 0, 9, 9);
            }
            for (int i = Mth.ceil((healthMax + absorb) / 2.0F) - 1; i >= 0; -- i) {
                int row = i / 10;
                int heart = i % 10;
                int x = left + heart * 8;
                int y = top - row * rowHeight;
                if (health <= 4) y += rand.nextInt(2);
                if (i == regen) y -= 2;
                guiGraphics.blit(texture, x, y, highlight ? 9 : 0, 0, 9, 9);
                if (i * 2 + 1 < healthLast && highlight){
                    guiGraphics.blit(texture, x, y,54 , 0, 9, 9);
                }
                if (i * 2 + 1 < health){
                    guiGraphics.blit(texture, x, y, 36, 0, 9, 9);
                } else if (i * 2 + 1 == health){
                    guiGraphics.blit(texture, x, y, 45, 0, 9, 9);
                }
            }
            guiGraphics.pose().popPose();
        }
    }
}