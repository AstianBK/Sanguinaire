package com.TBK.sanguinaire;

import com.TBK.sanguinaire.client.renderer.BloodOrbRenderer;
import com.TBK.sanguinaire.client.renderer.SlashBloodRenderer;
import com.TBK.sanguinaire.common.registry.*;
import com.TBK.sanguinaire.server.capability.SGCapability;
import com.TBK.sanguinaire.server.network.PacketHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Sanguinaire.MODID)
public class Sanguinaire
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "sanguinaire";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();


    public Sanguinaire()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(SGCapability::registerCapabilities);
        SGParticles.register(modEventBus);
        SGAttribute.ATTRIBUTES.register(modEventBus);
        SGItems.ITEMS.register(modEventBus);
        SGCreativeModeTab.TABS.register(modEventBus);
        SGEntityType.ENTITY_TYPES.register(modEventBus);
        SGEffect.MOB_EFFECT.register(modEventBus);
        SGSkillAbstract.init();
        SGSounds.register(modEventBus);
        PacketHandler.registerMessages();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->{
            modEventBus.addListener(this::registerRenderers);
        });
    }

    @OnlyIn(Dist.CLIENT)
    private void registerRenderers(FMLCommonSetupEvent event){
        EntityRenderers.register(SGEntityType.BLOOD_SLASH.get(), SlashBloodRenderer::new);
        EntityRenderers.register(SGEntityType.BLOOD_ORB.get(), BloodOrbRenderer::new);
    }


}
