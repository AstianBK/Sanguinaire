package com.TBK.sanguinaire.server.manager;

import com.TBK.sanguinaire.common.api.Limbs;
import com.TBK.sanguinaire.server.manager.RegenerationInstance;
import com.TBK.sanguinaire.server.network.PacketHandler;
import com.TBK.sanguinaire.server.network.messager.PacketRemoveActiveEffect;
import com.TBK.sanguinaire.server.network.messager.PacketSyncLimbRegeneration;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LimbsPartRegeneration {
    public ServerPlayer serverPlayer;
    public Map<String, RegenerationInstance> loseLimbs;
    public LimbsPartRegeneration(ServerPlayer player){
        this.loseLimbs= Maps.newHashMap();
        this.serverPlayer=player;
    }
    @OnlyIn(Dist.CLIENT)
    public LimbsPartRegeneration(Map<String,RegenerationInstance> loseLimbs){
        this.loseLimbs= loseLimbs;
        this.serverPlayer=null;
    }

    public boolean loseLimb(String id){
        return this.loseLimbs.containsKey(id) && this.loseLimbs.get(id).getCooldownPercent()>0.5F;
    }

    public boolean loseLimb(Limbs limbs){
        return this.loseLimbs.containsKey(limbs.name());
    }
    public void addLoseLimbDefault(String id){
        this.loseLimbs.put(id,new RegenerationInstance(300));
    }
    @OnlyIn(Dist.CLIENT)
    public void addLoseLimb(String id,int timer,int remaining){
        this.loseLimbs.put(id,new RegenerationInstance(timer,remaining));
    }
    @OnlyIn(Dist.CLIENT)
    public void addLoseLimb(String id,RegenerationInstance instance){
        this.loseLimbs.put(id,instance);
    }
    public void regenerateLimb(String id){
        this.loseLimbs.remove(id);
        if(this.serverPlayer!=null){
            PacketHandler.sendToPlayer(new PacketRemoveActiveEffect(id,1),this.serverPlayer);
        }
    }
    @OnlyIn(Dist.CLIENT)
    public void regenerateLimbClient(String id){
        this.loseLimbs.remove(id);
    }
    public void clearLimbs(){
        this.loseLimbs.clear();
    }
    public void syncPlayer(){
        PacketHandler.sendToPlayer(new PacketSyncLimbRegeneration(this.loseLimbs),this.serverPlayer);
    }
    public List<Limbs> getLimbs(){
        List<Limbs> limbs=new ArrayList<>();
        this.loseLimbs.forEach((s, regenerationInstance) -> {
            if(regenerationInstance.getRegerationTimerRemaining()>0){
                Limbs limbs1=Limbs.valueOf(s.toUpperCase());
                limbs.add(limbs1);
            }
        });
        return limbs;
    }
    public List<Limbs> getLimbsMuscle(){
        List<Limbs> limbs=new ArrayList<>();
        this.loseLimbs.forEach((s, regenerationInstance) -> {
            if(regenerationInstance.getCooldownPercent()<0.5F){
                Limbs limbs1=Limbs.valueOf(s.toUpperCase());
                limbs.add(limbs1);
            }
        });
        return limbs;
    }
    public boolean decrementCooldown(RegenerationInstance c, int amount,String id,List<Limbs> list) {
        if(canRegenerateLimbs(id,list)){
            c.decrementBy(amount);
            return c.getRegerationTimerRemaining() <= 0;
        }else {
            c.resetTimer();
        }
        return false;
    }
    public boolean canRegenerateLimbs(String id,List<Limbs> limbs){
        Limbs limbs1=Limbs.valueOf(id.toUpperCase());
        if(limbs1==Limbs.HEAD){
            return true;
        }else if(limbs1==Limbs.BODY){
            return !limbs.contains(Limbs.HEAD);
        }else {
            return !limbs.contains(Limbs.BODY);
        }
    }
    public void tick(){
        var powers = loseLimbs.entrySet().stream().filter(x -> decrementCooldown(x.getValue(), 1,x.getKey(),this.getLimbs())).toList();
        powers.forEach(stringRegenerationInstanceEntry -> regenerateLimb(stringRegenerationInstanceEntry.getKey()));

    }
    public boolean hasRegenerationLimbs(){
        return !this.loseLimbs.isEmpty();
    }
    public ListTag saveNBTData() {
        var listTag = new ListTag();
        loseLimbs.forEach((powerId, cooldown) -> {
            if (cooldown.getRegerationTimerRemaining() > 0) {
                CompoundTag ct = new CompoundTag();
                ct.putString("name", powerId);
                ct.putInt("timer", cooldown.getRegerationTimer());
                ct.putInt("remaining", cooldown.getRegerationTimerRemaining());
                listTag.add(ct);
            }
        });
        return listTag;
    }

    public void loadNBTData(ListTag listTag) {
        if (listTag != null) {
            listTag.forEach(tag -> {
                CompoundTag t = (CompoundTag) tag;
                String powerId = t.getString("name");
                int powerCooldown = t.getInt("timer");
                int cooldownRemaining = t.getInt("remaining");
                loseLimbs.put(powerId, new RegenerationInstance(powerCooldown, cooldownRemaining));
            });
        }
    }
}
