package com.TBK.sanguinaire.server;

import com.TBK.sanguinaire.common.registry.SGAttribute;
import com.TBK.sanguinaire.common.registry.SGEntityType;
import com.TBK.sanguinaire.common.registry.SGItemProperties;
import com.TBK.sanguinaire.server.entity.vampire.VampillerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class AttributeEvent {
    @SubscribeEvent
    public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, SGAttribute.BLOOD);
    }
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(SGEntityType.VAMPILLER.get(), VampillerEntity.setAttributes());
    }
    @SubscribeEvent
    public static void registerSpawn(SpawnPlacementRegisterEvent event) {
        event.register(SGEntityType.VAMPILLER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new SpawnPlacements.SpawnPredicate<VampillerEntity>() {
                    @Override
                    public boolean test(EntityType<VampillerEntity> p_217081_, ServerLevelAccessor p_217082_, MobSpawnType p_217083_, BlockPos p_217084_, RandomSource p_217085_) {
                        return VampillerEntity.checkMonsterSpawnRules(p_217081_,p_217082_,p_217083_,p_217084_,p_217085_);
                    }
                }, SpawnPlacementRegisterEvent.Operation.AND);

    }
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(SGItemProperties::register);
    }
}