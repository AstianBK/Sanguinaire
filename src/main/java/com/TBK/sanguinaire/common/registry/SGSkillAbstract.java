package com.TBK.sanguinaire.common.registry;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.server.skill.BatForm;
import com.TBK.sanguinaire.server.skill.SkillAbstract;
import com.TBK.sanguinaire.server.skill.drakul.*;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.Supplier;

public class SGSkillAbstract {
    public static Map<ResourceLocation, Supplier<SkillAbstract>> POWERS= Maps.newHashMap();

    public static Supplier<SkillAbstract> register(ResourceLocation name, Supplier<SkillAbstract> power){
        return POWERS.put(name,power);
    }

    public static Supplier<BloodTendrils> BLOOD_TENDRILS;
    public static Supplier<SkillAbstract> BLOOD_SPIKES;
    public static Supplier<SkillAbstract> BLOOD_SLASH;
    public static Supplier<SkillAbstract> BLOOD_ORB;
    public static Supplier<SkillAbstract> BLOOD_POOL;
    public static Supplier<SkillAbstract> TRANSFORM_BAT;
    public static void init(){
        register(new ResourceLocation(Sanguinaire.MODID,"blood_tendrils"),BloodTendrils::new);
        register(new ResourceLocation(Sanguinaire.MODID,"blood_slash"),BloodSlash::new);
        register(new ResourceLocation(Sanguinaire.MODID,"blood_orb"),BloodOrb::new);
        //register(new ResourceLocation(Sanguinaire.MODID,"transform_bat"),TRANSFORM_BAT);
        register(new ResourceLocation(Sanguinaire.MODID,"blood_spikes"),BloodSpikes::new);
        register(new ResourceLocation(Sanguinaire.MODID,"blood_pool"),BloodPool::new);
    }

    public static SkillAbstract getSkillAbstractForName(String name){
        ResourceLocation resourceLocation=new ResourceLocation(Sanguinaire.MODID,name);
        return POWERS.get(resourceLocation)!=null ? POWERS.get(resourceLocation).get() : SkillAbstract.NONE;
    }

    public static SkillAbstract getSkillAbstractForLocation(ResourceLocation resourceLocation){
        return POWERS.get(resourceLocation)!=null ? POWERS.get(resourceLocation).get() : SkillAbstract.NONE;
    }
}
