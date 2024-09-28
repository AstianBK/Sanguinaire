package com.TBK.sanguinaire.server.skill;

import com.TBK.sanguinaire.common.registry.SGSkillAbstract;
import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.network.PacketHandler;
import com.TBK.sanguinaire.server.network.messager.PacketSyncBlood;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SkillAbstract {
    public static SkillAbstract NONE = new SkillAbstract("",0,0, 0, 0, false, false, false, false,false,0);
    private final String descriptionId;
    private List<UUID> uuidTargets=new ArrayList<>();
    private List<LivingEntity> targets=new ArrayList<>();

    public int cooldown;
    public int duration;
    public int cooldownTimer=0;
    public int lauchTime;
    public int castingDuration;
    public boolean instantUse;
    public boolean isTransform;
    public boolean isPassive;
    public boolean isCasting;
    public boolean canReActive;
    public int level;
    public int costBloodBase;
    public String name;
    public CompoundTag tag;
    public Map<Attribute, AttributeModifier> attributeModifierMap= Maps.newHashMap();
    public SkillAbstract(String name, int duration, int castingDuration, int cooldown, int lauchTime,
                         boolean instantUse, boolean isTransform, boolean canReActive, boolean isCasting, boolean isPassive, int costBloodBase){
        this.castingDuration=castingDuration;
        this.cooldown=cooldown;
        this.lauchTime =lauchTime;
        this.instantUse=instantUse;
        this.isTransform=isTransform;
        this.isCasting = isCasting;
        this.level=1;
        this.duration=duration;
        this.name=name;
        this.isPassive=isPassive;
        this.costBloodBase=costBloodBase;
        this.canReActive=canReActive;
        this.descriptionId= Component.translatable("skill."+name).getString();
    }

    public SkillAbstract(CompoundTag tag){
        SkillAbstract power= SGSkillAbstract.getSkillAbstractForName(tag.getString("name"));
        this.name= power.name;
        this.cooldown= power.cooldown;
        this.castingDuration= power.castingDuration;
        this.lauchTime= power.lauchTime;
        this.instantUse= power.instantUse;
        this.isTransform= power.isTransform;
        this.isCasting = power.isCasting;
        this.descriptionId=power.descriptionId;
        this.isPassive=power.isPassive;
        this.costBloodBase=power.costBloodBase;
        this.duration=power.duration;
        this.read(tag);
    }
    public SkillAbstract(FriendlyByteBuf buf){
        this.name= buf.readUtf();
        this.cooldown= buf.readInt();
        this.castingDuration= buf.readInt();
        this.lauchTime= buf.readInt();
        this.instantUse= buf.readBoolean();
        this.isTransform=buf.readBoolean();
        this.isCasting = buf.readBoolean();
        this.isPassive=buf.readBoolean();
        this.costBloodBase=buf.readInt();
        this.duration=buf.readInt();
        this.descriptionId= Component.translatable("skill."+name).getString();
    }


    public boolean isCanReActive(){
        return this.canReActive;
    }
    public void addTarget(LivingEntity target){
        this.targets.add(target);
        this.uuidTargets.add(target.getUUID());
    }
    public List<LivingEntity> getTargets(){
        return this.targets;
    }
    public void tick(SkillPlayerCapability player){
        this.effectSkillAbstractForTick(player.getPlayer());
    }

    public void effectSkillAbstractForTick(Player player) {
        this.updateAttributes(player);
    }

    public int getCostBloodBase() {
        return this.costBloodBase;
    }

    public void effectPassiveForTick(Player player) {

    }

    public void updateAttributes(Player player){

    }
    public boolean isCasting() {
        return this.isCasting;
    }

    public boolean isInstantUse() {
        return this.instantUse;
    }

    public boolean isTransform() {
        return this.isTransform;
    }

    public  int getCooldownForLevel(){
        return 0;
    }

    public void startSkillAbstract(SkillPlayerCapability skill) {
        skill.getPlayerVampire().loseBlood(this.getCostBloodBase());
        if(!skill.getPlayer().level().isClientSide){
            PacketHandler.sendToPlayer(new PacketSyncBlood(skill.getPlayerVampire().getBlood()), (ServerPlayer) skill.getPlayer());
        }
    }
    public void stopSkillAbstract(SkillPlayerCapability skill){
        this.getTargets().clear();
        skill.getCooldowns().addCooldown(this,this.cooldown);
    }

    public void setCooldownTimer(int cooldownTimer) {
        this.cooldownTimer = cooldownTimer;
    }
    public CompoundTag save(CompoundTag tag){
        if(this.tag==null){
            this.tag=tag;
        }
        tag.putInt("duration",this.duration);
        tag.putInt("cooldownTimer",this.cooldownTimer);
        return tag;
    }

    public void read(CompoundTag tag){
        if(tag==null){
            tag=new CompoundTag();
        }
        this.duration=tag.getInt("duration");
        this.setCooldownTimer(tag.getInt("cooldownTimer"));
        this.tag=tag;
    }


    public boolean useResources() {
        return false;
    }

    public Attributes getResources() {
        return null;
    }

    public void addAttributeModifiers(LivingEntity p_19478_, AttributeMap p_19479_, int p_19480_) {
        for(Map.Entry<Attribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
            AttributeInstance attributeinstance = p_19479_.getInstance(entry.getKey());
            if (attributeinstance != null) {
                AttributeModifier attributemodifier = entry.getValue();
                attributeinstance.removeModifier(attributemodifier);
                attributeinstance.addPermanentModifier(new AttributeModifier(attributemodifier.getId(), this.descriptionId + " " + p_19480_, this.getAttributeModifierValue(p_19480_, attributemodifier), attributemodifier.getOperation()));
            }
        }

    }

    public SkillAbstract addAttributeModifier(Attribute p_19473_, String p_19474_, double p_19475_, AttributeModifier.Operation p_19476_) {
        AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString(p_19474_),this.name, p_19475_, p_19476_);
        this.attributeModifierMap.put(p_19473_, attributemodifier);
        return this;
    }

    public double getAttributeModifierValue(int p_19457_, AttributeModifier p_19458_) {
        return p_19458_.getAmount() * (double)(p_19457_ + 1);
    }

    public Map<Attribute, AttributeModifier> getAttributeModifiers() {
        return this.attributeModifierMap;
    }

    public void removeAttributeModifiers(LivingEntity p_19469_, AttributeMap p_19470_, int p_19471_) {
        for(Map.Entry<Attribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
            AttributeInstance attributeinstance = p_19470_.getInstance(entry.getKey());
            if (attributeinstance != null) {
                attributeinstance.removeModifier(entry.getValue());
            }
        }

    }

    @Override
    public boolean equals(Object obj) {
        return ((SkillAbstract) obj).name.equals(this.name);
    }
}
