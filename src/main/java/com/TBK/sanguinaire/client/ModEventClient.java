package com.TBK.sanguinaire.client;

import com.TBK.sanguinaire.client.renderer.BatFormRenderer;
import com.TBK.sanguinaire.server.capability.SGCapability;
import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ModEventClient {
    @SubscribeEvent
    public static void renderEvent(RenderLivingEvent.Post<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event){
        if(event.getEntity() instanceof Player player){
            RenderUtil.render(event.getPoseStack(),event.getMultiBufferSource(),player,event.getPartialTick());
        }
    }

    @SubscribeEvent
    public static void renderPreEvent(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event){
        if(event.getEntity() instanceof Player player && SkillPlayerCapability.get(player).isTransform){
            Minecraft mc = Minecraft.getInstance();
            Entity entity = event.getEntity();
            EntityRendererProvider.Context context = new EntityRendererProvider.Context(mc.getEntityRenderDispatcher(),
                    mc.getItemRenderer(),mc.getBlockRenderer(),mc.gameRenderer.itemInHandRenderer,
                    mc.getResourceManager(),mc.getEntityModels(),mc.font);
            SkillPlayerCapability animatable = SGCapability.getEntityCap(event.getEntity(), SkillPlayerCapability.class);
            EntityRenderer renderer= new BatFormRenderer(context,animatable);
            if(renderer instanceof BatFormRenderer geoRenderer  && animatable!=null){
                //geoRenderer.setCurrentEntity(event.getEntity());
                event.setCanceled(true);
                geoRenderer.renderTransform(entity,animatable,0.0F,event.getPartialTick(),event.getPoseStack(),event.getMultiBufferSource(),event.getPackedLight());
            }
        }

    }

}
