package com.TBK.sanguinaire.client;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.client.gui.*;
import com.TBK.sanguinaire.client.layer.CastingLayer;
import com.TBK.sanguinaire.client.layer.RegenerationLayer;
import com.TBK.sanguinaire.client.particle.custom.*;
import com.TBK.sanguinaire.common.registry.SGParticles;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Sanguinaire.MODID, bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class EventClient {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerArmorRenderers(EntityRenderersEvent.AddLayers event){
        event.getSkins().forEach(s -> {
            event.getSkin(s).addLayer(new RegenerationLayer(event.getSkin(s)));
            event.getSkin(s).addLayer(new CastingLayer(event.getSkin(s)));
        });
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerGui(RegisterGuiOverlaysEvent event){
        event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "bite",new BiteIndicator());
        event.registerAbove(VanillaGuiOverlay.PLAYER_HEALTH.id(), "heart_vampire",new HeartsEffect());
        event.registerAbove(VanillaGuiOverlay.HELMET.id(), "bleeding",new BleedingOverlay());
        event.registerAbove(VanillaGuiOverlay.FOOD_LEVEL.id(), "blood",new BloodOverlay());
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "skill_hotbar",new SkillOverlay());
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        if(SGParticles.BLOOD_PARTICLES.isPresent()){
            event.registerSpriteSet(SGParticles.BLOOD_PARTICLES.get(), BloodBKParticles.Factory::new);
        }
        if(SGParticles.SLASH_PARTICLES.isPresent()){
            event.registerSpriteSet(SGParticles.SLASH_PARTICLES.get(), SlashParticles.Factory::new);
        }
        if(SGParticles.SLICE_PARTICLES.isPresent()){
            event.registerSpriteSet(SGParticles.SLICE_PARTICLES.get(), SliceParticles.Factory::new);
        }
        if(SGParticles.BLOOD_TRAIL_PARTICLES.isPresent()){
            event.registerSpriteSet(SGParticles.BLOOD_TRAIL_PARTICLES.get(), BloodTrailParticles.Factory::new);
        }
        if(SGParticles.BLOOD_EXPLOSION_PARTICLES.isPresent()){
            event.registerSpriteSet(SGParticles.BLOOD_EXPLOSION_PARTICLES.get(), BloodExplosionParticles.Factory::new);
        }
    }

}
