package com.TBK.sanguinaire.client;

import com.TBK.sanguinaire.client.renderer.BatFormRenderer;
import com.TBK.sanguinaire.client.renderer.SlashBloodRenderer;
import com.TBK.sanguinaire.common.registry.SGEntityType;
import com.TBK.sanguinaire.server.capability.SGCapability;
import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ModEventClient {
    @SubscribeEvent
    public static void renderPreEvent(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event){
        if(event.getEntity() instanceof Player player){
            RenderUtil.render(event.getPoseStack(),event.getMultiBufferSource(),player,event.getPartialTick());
        }
    }
}
