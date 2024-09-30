package com.TBK.sanguinaire;

import com.TBK.sanguinaire.client.renderer.BloodOrbRenderer;
import com.TBK.sanguinaire.client.renderer.SlashBloodRenderer;
import com.TBK.sanguinaire.client.renderer.VampillerRenderer;
import com.TBK.sanguinaire.common.registry.*;
import com.TBK.sanguinaire.server.capability.SGCapability;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import com.TBK.sanguinaire.server.network.PacketHandler;
import com.TBK.sanguinaire.server.world.biomes.BKBiomeSpawn;
import com.TBK.sanguinaire.server.world.loot.LootModifiers;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

@Mod(Sanguinaire.MODID)
public class Sanguinaire
{
    public static final String MODID = "sanguinaire";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY = ResourceKey.create(
            Registries.PROCESSOR_LIST, new ResourceLocation("minecraft", "empty"));



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
        LootModifiers.register(modEventBus);
        SGSkillAbstract.init();
        SGSounds.register(modEventBus);
        PacketHandler.registerMessages();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        final DeferredRegister<Codec<? extends BiomeModifier>> biomeModifiers = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Sanguinaire.MODID);
        biomeModifiers.register(modEventBus);
        biomeModifiers.register("sanguinaire_spawns", BKBiomeSpawn::makeCodec);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->{
            MinecraftForge.EVENT_BUS.addListener(this::onRenderFoodBar);
            modEventBus.addListener(this::registerRenderers);
        });
    }

    @OnlyIn(Dist.CLIENT)
    public void onRenderFoodBar(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc=Minecraft.getInstance();
        VampirePlayerCapability cap=VampirePlayerCapability.get(mc.player);
        if (mc.player == null || !mc.player.isAlive() || cap==null || !cap.isVampire()) return;
        if (event.getOverlay().id() == VanillaGuiOverlay.FOOD_LEVEL.id()  && mc.gameMode.hasExperience()) {
            event.setCanceled(true);
        }
        if (event.getOverlay().equals(VanillaGuiOverlay.AIR_LEVEL.type())) {
            event.setCanceled(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void registerRenderers(FMLCommonSetupEvent event){
        EntityRenderers.register(SGEntityType.BLOOD_SLASH.get(), SlashBloodRenderer::new);
        EntityRenderers.register(SGEntityType.BLOOD_ORB.get(), BloodOrbRenderer::new);
        EntityRenderers.register(SGEntityType.VAMPILLER.get(), VampillerRenderer::new);

    }


}
