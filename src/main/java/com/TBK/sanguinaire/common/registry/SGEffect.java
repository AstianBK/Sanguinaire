package com.TBK.sanguinaire.common.registry;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.common.effects.BleedingEffects;
import com.TBK.sanguinaire.common.effects.InfinityBleedingEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SGEffect {
    public static final DeferredRegister<MobEffect> MOB_EFFECT = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Sanguinaire.MODID);

    public static final RegistryObject<MobEffect> INFINITY_BLEEDING = MOB_EFFECT.register("infinity_bleeding", InfinityBleedingEffects::new);

    public static final RegistryObject<MobEffect> BLEEDING = MOB_EFFECT.register("bleeding", BleedingEffects::new);

}
