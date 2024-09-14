package com.TBK.sanguinaire;

import com.TBK.sanguinaire.common.registry.SGParticles;
import com.TBK.sanguinaire.common.registry.SGAttribute;
import com.TBK.sanguinaire.server.capability.SGCapability;
import com.TBK.sanguinaire.server.network.PacketHandler;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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
        PacketHandler.registerMessages();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

}
