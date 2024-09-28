package com.TBK.sanguinaire.common.registry;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.server.entity.projetile.BloodOrbProjetile;
import com.TBK.sanguinaire.server.entity.projetile.SlashBloodProjetile;
import com.TBK.sanguinaire.server.entity.vampire.VampillerEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SGEntityType {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Sanguinaire.MODID);

        public static final RegistryObject<EntityType<VampillerEntity>> VAMPILLER =
            ENTITY_TYPES.register("vampiller",
                    () -> EntityType.Builder.of(VampillerEntity::new, MobCategory.MONSTER)
                            .sized(0.60f, 1.0f)
                            .build(new ResourceLocation(Sanguinaire.MODID, "vampiller").toString()));

    public static final RegistryObject<EntityType<SlashBloodProjetile>> BLOOD_SLASH = ENTITY_TYPES
            .register("blood_slash", () -> EntityType.Builder.<SlashBloodProjetile>of(SlashBloodProjetile::new, MobCategory.MISC)
                    .fireImmune().sized(3.0F, 0.2F).build(Sanguinaire.MODID+ "blood_slash"));

    public static final RegistryObject<EntityType<BloodOrbProjetile>> BLOOD_ORB = ENTITY_TYPES
            .register("blood_orb", () -> EntityType.Builder.<BloodOrbProjetile>of(BloodOrbProjetile::new, MobCategory.MISC)
                    .fireImmune().sized(0.2F, 0.2F).build(Sanguinaire.MODID+ "blood_orb"));

}
