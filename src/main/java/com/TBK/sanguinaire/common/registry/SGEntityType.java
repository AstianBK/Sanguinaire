package com.TBK.sanguinaire.common.registry;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.server.entity.projetile.SlashBloodProjetile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SGEntityType {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Sanguinaire.MODID);

    public static final RegistryObject<EntityType<SlashBloodProjetile>> BLOOD_SLASH = ENTITY_TYPES
            .register("soul_slash", () -> EntityType.Builder.<SlashBloodProjetile>of(SlashBloodProjetile::new, MobCategory.MISC)
                    .fireImmune().sized(3.0F, 0.2F).build(Sanguinaire.MODID+ "soul_slash"));

}
