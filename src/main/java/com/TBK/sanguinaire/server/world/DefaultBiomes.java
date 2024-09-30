package com.TBK.sanguinaire.server.world;

public class DefaultBiomes {
    public static final SpawnBiomeData EMPTY = new SpawnBiomeData();

    public static final SpawnBiomeData VAMPILLER = new SpawnBiomeData()
            .addBiomeEntry(SpawnBiomeData.BiomeEntryType.REGISTRY_NAME, false, "minecraft:savanna", 0)
            .addBiomeEntry(SpawnBiomeData.BiomeEntryType.REGISTRY_NAME, false, "minecraft:snowy_taiga", 1)
            .addBiomeEntry(SpawnBiomeData.BiomeEntryType.REGISTRY_NAME, false, "minecraft:taiga", 2)
            .addBiomeEntry(SpawnBiomeData.BiomeEntryType.REGISTRY_NAME, false, "minecraft:plains", 3);


    public static final SpawnBiomeData UNSEEN_GRASP = new SpawnBiomeData()
            .addBiomeEntry(SpawnBiomeData.BiomeEntryType.REGISTRY_NAME, false, "minecraft:dark_forest", 0);


}
