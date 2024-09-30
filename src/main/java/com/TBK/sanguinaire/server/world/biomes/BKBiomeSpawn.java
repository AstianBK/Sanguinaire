package com.TBK.sanguinaire.server.world.biomes;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.common.registry.SGEntityType;
import com.TBK.sanguinaire.server.world.SpawnBiomeData;
import org.apache.commons.lang3.tuple.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BKBiomeSpawn implements BiomeModifier {
    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(new ResourceLocation(Sanguinaire.MODID, "sanguinaire_spawns"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS,Sanguinaire.MODID);

    public BKBiomeSpawn(){

    }
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if(phase==Phase.ADD){
            addBiomeSpawns(biome,builder);
        }
    }
    public static void addBiomeSpawns(Holder<Biome> biome, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (testBiome(BKBiomeConfig.vampillers, biome)) {
            builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(SGEntityType.VAMPILLER.get(), 10, 2, 10));
        }
    }

    public static Codec<BKBiomeSpawn> makeCodec() {
        return Codec.unit(BKBiomeSpawn::new);
    }
    private static ResourceLocation getBiomeName(Holder<Biome> biome) {
        return biome.unwrap().map(ResourceKey::location, (noKey) -> null);
    }

    public static boolean testBiome(Pair<String, SpawnBiomeData> entry, Holder<Biome> biome) {
        boolean result = false;
        try {
            result = BKBiomeConfig.test(entry, biome, getBiomeName(biome));
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return (Codec)SERIALIZER.get();
    }
}
