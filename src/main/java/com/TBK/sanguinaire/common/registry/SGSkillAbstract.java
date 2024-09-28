package com.TBK.sanguinaire.common.registry;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.server.skill.BatForm;
import com.TBK.sanguinaire.server.skill.SkillAbstract;
import com.TBK.sanguinaire.server.skill.drakul.BloodOrb;
import com.TBK.sanguinaire.server.skill.drakul.BloodSlash;
import com.TBK.sanguinaire.server.skill.drakul.BloodTendrils;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class SGSkillAbstract {
    public static Map<ResourceLocation,SkillAbstract> POWERS= Maps.newHashMap();

    public static SkillAbstract register(ResourceLocation name, SkillAbstract power){
        return POWERS.put(name,power);
    }

    public static BloodTendrils BLOOD_TENDRILS=new BloodTendrils();
    public static BloodSlash BLOOD_SLASH=new BloodSlash();
    public static BloodOrb BLOOD_ORB=new BloodOrb();
    public static BatForm TRANSFORM_BAT=new BatForm();
    public static void init(){
        register(new ResourceLocation(Sanguinaire.MODID,"blood_tendrils"),BLOOD_TENDRILS);
        register(new ResourceLocation(Sanguinaire.MODID,"blood_slash"),BLOOD_SLASH);
        register(new ResourceLocation(Sanguinaire.MODID,"blood_orb"),BLOOD_ORB);
        register(new ResourceLocation(Sanguinaire.MODID,"transform_bat"),TRANSFORM_BAT);
    }

    public static SkillAbstract getSkillAbstractForName(String name){
        ResourceLocation resourceLocation=new ResourceLocation(Sanguinaire.MODID,name);
        return POWERS.get(resourceLocation)!=null ? POWERS.get(resourceLocation) : SkillAbstract.NONE;
    }

    public static SkillAbstract getSkillAbstractForLocation(ResourceLocation resourceLocation){
        return POWERS.get(resourceLocation)!=null ? POWERS.get(resourceLocation) : SkillAbstract.NONE;
    }
}
