package com.TBK.sanguinaire.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ModEventClient {
    @SubscribeEvent
    public static void renderEvent(RenderLivingEvent.Post<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event){
        boolean isGeckoModel=false;
        /*if(event.getEntity() instanceof Player player && PowerPlayerCapability.get(player).durationEffect.hasDurationForPower("fly")){
            Minecraft mc = Minecraft.getInstance();
            Entity entity = event.getEntity();

            EntityRendererProvider.Context context = new EntityRendererProvider.Context(mc.getEntityRenderDispatcher(),
                    mc.getItemRenderer(),mc.getBlockRenderer(),mc.gameRenderer.itemInHandRenderer,
                    mc.getResourceManager(),mc.getEntityModels(),mc.font);
            AnimationPlayerCapability animatable = PwCapability.getEntityPatch(event.getEntity(), AnimationPlayerCapability.class);
            EntityRenderer renderer= new GeckoPlayerRenderer(context,new GeckoPlayerModel(),((PlayerRenderer)event.getRenderer()).getTextureLocation((AbstractClientPlayer)entity),animatable);
            if(renderer instanceof GeckoPlayerRenderer geoRenderer  && animatable!=null){
                //geoRenderer.setCurrentEntity(event.getEntity());
                entity.setInvisible(true);
                isGeckoModel=true;
                geoRenderer.renderGeckoPlayer(entity,animatable,0.0F,event.getPartialTick(),event.getPoseStack(),event.getMultiBufferSource(),event.getPackedLight());
            }
        }*/
        if(event.getEntity() instanceof Player player){
            RenderUtil.render(event.getPoseStack(),event.getMultiBufferSource(),player,event.getPartialTick());
        }
    }

}
