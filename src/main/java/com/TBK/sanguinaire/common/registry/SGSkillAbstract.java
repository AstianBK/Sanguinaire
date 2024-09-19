package com.TBK.sanguinaire.common.registry;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.server.skill.BatForm;
import com.TBK.sanguinaire.server.skill.SkillAbstract;
import com.TBK.sanguinaire.server.skill.drakul.TentacleBlood;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class SGSkillAbstract {
    public static Map<ResourceLocation,SkillAbstract> POWERS= Maps.newHashMap();

    public static SkillAbstract register(ResourceLocation name, SkillAbstract power){
        return POWERS.put(name,power);
    }

    public static void init(){
        register(new ResourceLocation(Sanguinaire.MODID,"tentacle_blood"),new TentacleBlood());
        register(new ResourceLocation(Sanguinaire.MODID,"transform_bat"),new BatForm());

    }

    public static SkillAbstract getSkillAbstractForName(String name){
        ResourceLocation resourceLocation=new ResourceLocation(Sanguinaire.MODID,name);
        return POWERS.get(resourceLocation)!=null ? POWERS.get(resourceLocation) : SkillAbstract.NONE;
    }

    public static SkillAbstract getSkillAbstractForLocation(ResourceLocation resourceLocation){
        return POWERS.get(resourceLocation)!=null ? POWERS.get(resourceLocation) : SkillAbstract.NONE;
    }
}
