package com.TBK.sanguinaire.common.registry;

import com.TBK.sanguinaire.Sanguinaire;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SGAttribute {
    public static final Attribute BLOOD = new RangedAttribute("blood",0.0d,-Double.MAX_VALUE, Double.MAX_VALUE).setSyncable(true);
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Sanguinaire.MODID);
    public static RegistryObject<Attribute> BLOOD_VALUE= ATTRIBUTES.register("blood",()-> BLOOD);

}
