package com.TBK.sanguinaire.common.registry;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.common.block.CoffinBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SGBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Sanguinaire.MODID);

    public static final RegistryObject<BlockEntityType<CoffinBlockEntity>> STONE_COFFIN =
            BLOCK_ENTITIES.register("stone_coffin",
                    () -> BlockEntityType.Builder.of(CoffinBlockEntity::new, SGBlocks.STONE_COFFIN.get()).build(null));
}

