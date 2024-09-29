package com.TBK.sanguinaire.server.skill;

import com.TBK.sanguinaire.server.manager.SkillAbstractInstance;
import com.TBK.sanguinaire.server.network.PacketHandler;
import com.TBK.sanguinaire.server.network.messager.PacketHandlerPowers;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.Map;

public class SkillAbstracts {
    public Map<Integer, SkillAbstractInstance> powers= Maps.newHashMap();

    public SkillAbstracts(Map<Integer,SkillAbstractInstance> powers){
        this.powers=powers;
    }

    public SkillAbstracts(CompoundTag tag){
        if(tag.contains("skills",9)){
            ListTag listTag = tag.getList("skills",10);
            for(int i = 0 ; i<listTag.size() ; i++){
                CompoundTag tag1=listTag.getCompound(i);
                if(tag1.contains("name")){
                    int pos=tag1.getInt("pos");
                    SkillAbstract power = new SkillAbstract(tag1);
                    this.powers.put(pos, new SkillAbstractInstance(power,0));
                }
            }
        }
    }

    public void save(CompoundTag tag){
        ListTag listtag = new ListTag();
        for (int i=1;i<this.powers.size()+1;i++){
            if(this.powers.get(i)!=null){
                SkillAbstract power=this.powers.get(i).getSkillAbstract();
                CompoundTag tag1=new CompoundTag();
                tag1.putString("name",power.name);
                tag1.putInt("pos",i);
                power.save(tag1);
                listtag.add(tag1);
            }
        }
        if(!listtag.isEmpty()){
            tag.put("skills",listtag);
        }
    }

    public SkillAbstract getForName(String name){
        SkillAbstract power =SkillAbstract.NONE;
        for (SkillAbstractInstance powerInstance:this.getSkills()){
            SkillAbstract power1=powerInstance.getSkillAbstract();
            if(power1.name.equals(name)){
                power=power1;
            }
        }
        return power;
    }

    public boolean hasSkillAbstract(String id){
        return this.getForName(id)!=null;
    }

    public void addSkillAbstracts(int pos,SkillAbstract power){
        this.powers.put(pos,new SkillAbstractInstance(power,0));
    }

    public Collection<SkillAbstractInstance> getSkills() {
        return this.powers.values();
    }

    public SkillAbstract get(int pos){
        return this.powers.get(pos).getSkillAbstract();
    }
}
