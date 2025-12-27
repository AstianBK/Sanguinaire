package com.TBK.sanguinaire.server.capability;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.common.api.ISkillPlayer;
import com.TBK.sanguinaire.server.Util;
import com.TBK.sanguinaire.server.manager.*;
import com.TBK.sanguinaire.server.network.PacketHandler;
import com.TBK.sanguinaire.server.network.messager.PacketHandlerPowers;
import com.TBK.sanguinaire.server.network.messager.PacketSyncPosHotBar;
import com.TBK.sanguinaire.server.skill.*;
import com.TBK.sanguinaire.server.skill.drakul.*;
import com.google.common.collect.Maps;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.HashMap;
import java.util.Map;

public class SkillPlayerCapability implements ISkillPlayer, GeoEntity {
    public final AnimatableInstanceCache cache= GeckoLibUtil.createInstanceCache(this);
    public SkillAbstract lastUsingSkillAbstract=SkillAbstract.NONE;
    Player player;
    Level level;
    public SkillAbstracts skills =new SkillAbstracts(Maps.newHashMap());
    public SkillAbstracts passives=new SkillAbstracts(Maps.newHashMap());
    int posSelectSkillAbstract=0;
    int castingTimer=0;
    int castingClientTimer=0;
    int maxCastingClientTimer=0;
    public PlayerCooldowns cooldowns=new PlayerCooldowns();
    public ActiveEffectDuration durationEffect=new ActiveEffectDuration();
    //public boolean isTransform=false;
    public Forms form=Forms.NONE;
    public boolean hotbarActive = false;
    public int cooldownReUse = 0;


    public static SkillPlayerCapability get(Player player){
        return SGCapability.getEntityCap(player,SkillPlayerCapability.class);
    }

    public VampirePlayerCapability getPlayerVampire(){
        return VampirePlayerCapability.get(this.player);
    }

    public void setPosSelectSkillAbstract(int pos){
        this.posSelectSkillAbstract=pos;
    }

    public int getPosSelectSkillAbstract(){
        return this.posSelectSkillAbstract;
    }
    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void setPlayer(Player player) {
        this.player=player;
    }

    /*public void setIsTransform(boolean isTransform){
        this.isTransform=isTransform;
    }*/

    public boolean isVampire(){
        return this.getPlayerVampire().isVampire();
    }
    public void setForm(Forms form){
        this.form=form;
    }

    public Forms getForm(){
        return this.form;
    }

    @Override
    public SkillAbstract getSelectSkill() {
        return this.getHotBarSkill().get(this.posSelectSkillAbstract);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public SkillAbstract getSkillForHotBar(int pos) {
        return this.skills.get(pos);
    }

    @Override
    public int getCooldownSkill() {
        return 0;
    }

    @Override
    public int getCastingTimer() {
        return this.castingTimer;
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getCastingClientTimer() {
        return this.castingClientTimer;
    }

    @OnlyIn(Dist.CLIENT)
    public int getMaxCastingClientTimer() {
        return this.maxCastingClientTimer;
    }

    @Override
    public int getStartTime() {
        return 0;
    }

    @Override
    public boolean lastUsingSkill() {
        return false;
    }

    @Override
    public SkillAbstract getLastUsingSkill() {
        return this.lastUsingSkillAbstract;
    }

    @Override
    public void setLastUsingSkill(SkillAbstract power) {
        this.lastUsingSkillAbstract=power;
    }

    @Override
    public void tick(Player player) {
        if(Util.isVampire(player)){
            if (this.cooldownReUse>0){
                this.cooldownReUse--;
            }
            if(player instanceof ServerPlayer){
                if (this.cooldowns.hasCooldownsActive()){
                    this.cooldowns.tick(1);
                }
                if(this.durationEffect.hasDurationsActive()){
                    this.durationEffect.tick(1);
                }
                PacketHandler.sendToPlayer(new PacketSyncPosHotBar(this.posSelectSkillAbstract), (ServerPlayer) player);
            }else if(this.level.isClientSide){
                if (this.cooldowns.hasCooldownsActive()){
                    this.cooldowns.tick(1);
                }
                if(this.durationEffect.hasDurationsActive()){
                    this.durationEffect.tickDurations(this);
                }
            }
            if(!this.skills.getSkills().isEmpty()){
                this.skills.getSkills().forEach(e->{
                    if(this.durationEffect.hasDurationForSkill(e.getSkillAbstract())){
                        e.getSkillAbstract().tick(this);
                        this.durationEffect.decrementDurationCount(e.getSkillAbstract());
                    }
                });
            }
            if(!this.passives.getSkills().isEmpty()){
                this.passives.getSkills().forEach(e->{
                    e.getSkillAbstract().tick(this);
                });
            }
            if(this.level.isClientSide){
                if(this.castingClientTimer>0){
                    this.castingClientTimer--;
                }
            }else {
                if (this.isCasting()){
                    this.castingTimer--;
                }
            }
        }
    }

    public boolean isCasting(){
        return this.castingTimer>0;
    }

    @Override
    public void onJoinGame(Player player, EntityJoinLevelEvent event) {
        SkillAbstracts skillAbstracts=new SkillAbstracts(new HashMap<>());
        skillAbstracts.addSkillAbstracts(0,new BloodSlash());
        skillAbstracts.addSkillAbstracts(1,new BloodOrb());
        skillAbstracts.addSkillAbstracts(2,new BloodSpikes());
        skillAbstracts.addSkillAbstracts(3,new BloodPool());
        skillAbstracts.addSkillAbstracts(4,new BloodTendrils());
        this.setSetHotbar(skillAbstracts);
        this.passives.addSkillAbstracts(0,new SpeedPassive());
    }

    public void setSetHotbar(SkillAbstracts skillAbstracts){
        this.skills = skillAbstracts;
    }

    @Override
    public void handledSkill(SkillAbstract power) {
        power.startSkillAbstract(this);
    }

    @Override
    public void stopSkill(SkillAbstract power) {
        this.setLastUsingSkill(SkillAbstract.NONE);
        power.stopSkillAbstract(this);
        this.syncSkill(this.getPlayer());
    }

    @Override
    public void handledPassive(Player player, SkillAbstract power) {

    }

    @Override
    public boolean canUseSkill(SkillAbstract skillAbstract) {
        return !this.getCooldowns().isOnCooldown(skillAbstract) && !this.getSelectSkill().isPassive && !this.isCasting() && this.getPlayerVampire().getBlood()>=skillAbstract.getCostBloodBase();
    }

    @Override
    public Map<Integer, SkillAbstract> getPassives() {
        return null;
    }

    @Override
    public SkillAbstracts getHotBarSkill() {
        return this.skills;
    }

    @Override
    public void syncSkill(Player player) {
        if(player instanceof ServerPlayer serverPlayer){
            this.cooldowns.syncToPlayer(serverPlayer);
            this.durationEffect.syncAllToPlayer();
        }
    }
    public void syncPos(int pos,Player player){
        PacketHandler.sendToAllTracking(new PacketSyncPosHotBar(pos),player);
    }

    @Override
    public void upSkill() {

    }

    @Override
    public void downSkill() {

    }

    @Override
    public void startCasting(Player player,SkillAbstract skillAbstract) {

        if(!this.level.isClientSide){
            PacketHandler.sendToPlayer(new PacketHandlerPowers(0,player, player,skillAbstract.name), (ServerPlayer) player);
        }

        if(this.canUseSkill(skillAbstract)){
            boolean skillActive=this.durationEffect.hasDurationForSkill(skillAbstract);
            if(!skillActive || skillAbstract.isCanReActive()){
                if(skillAbstract.canReActive && skillActive){
                    DurationInstance instance=this.durationEffect.getDurationInstance(skillAbstract.name);
                    this.removeActiveEffect(instance);
                }else {
                    if(skillAbstract.isCasting){
                        this.startCasting(skillAbstract,player);
                    }else if(skillAbstract.instantUse){
                        this.setLastUsingSkill(skillAbstract);
                        this.handledSkill(skillAbstract);
                    }else {
                        DurationInstance instance=new DurationInstance(skillAbstract.name,skillAbstract.level,skillAbstract.duration+50*skillAbstract.level,200);
                        this.addActiveEffect(instance,player);
                        this.setLastUsingSkill(skillAbstract);
                        this.handledSkill(skillAbstract);
                    }
                }
            }
        }
    }
    public void stopCasting(Player player) {
        if(!this.level.isClientSide){
            PacketHandler.sendToPlayer(new PacketHandlerPowers(2,player, player), (ServerPlayer) player);
        }
        boolean skillActive=this.durationEffect.hasDurationForSkill(this.getLastUsingSkill());
        if (this.isCasting()){
            if(skillActive){
                SkillAbstract skill=this.getLastUsingSkill();
                DurationInstance instance=this.durationEffect.getDurationInstance(skill.name);
                this.removeActiveEffect(instance,DurationResult.CLICKUP);
            }
        }
        this.castingClientTimer=0;
        this.maxCastingClientTimer=0;
        this.castingTimer=0;
    }

    public void startCasting(SkillAbstract power,Player player){
        DurationInstance instance=new DurationInstance(power.name,power.level,power.castingDuration,200);
        this.addActiveEffect(instance,player);
        this.setLastUsingSkill(power);
        this.handledSkill(power);
        if(this.level.isClientSide){
            this.castingClientTimer=power.castingDuration;
            this.maxCastingClientTimer=power.castingDuration;
        }else {
            this.castingTimer=power.castingDuration;
        }
    }

    public void removeActiveEffect(DurationInstance instance){
        this.durationEffect.removeDuration(instance,DurationResult.TIMEOUT,true);
    }

    public void removeActiveEffect(DurationInstance instance,DurationResult result){
        this.durationEffect.removeDuration(instance,result,true);
    }

    public void addActiveEffect(DurationInstance instance, Player player){
        this.durationEffect.addDuration(instance,this);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag=new CompoundTag();
        tag.putBoolean("isTransform",false);
        this.skills.save(tag);
        tag.putInt("select_power",this.posSelectSkillAbstract);
        tag.putString("form",this.getForm().name());
        if(this.cooldowns.hasCooldownsActive()){
            tag.put("cooldowns",this.cooldowns.saveNBTData());
        }
        if(this.durationEffect.hasDurationsActive()){
            tag.put("activeEffect",this.durationEffect.saveNBTData());
        }

        return tag;

    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.skills =new SkillAbstracts(nbt);
        this.posSelectSkillAbstract=nbt.getInt("select_power");
        if(nbt.contains("form")){
            this.setForm(Forms.valueOf(nbt.getString("form")));
        }else {
            this.setForm(Forms.NONE);
        }

        if(nbt.contains("cooldowns")){
            ListTag listTag=nbt.getList("cooldowns",10);
            this.cooldowns.loadNBTData(listTag);
        }
        if(nbt.contains("activeEffect")){
            ListTag listTag=nbt.getList("activeEffect",10);
            this.durationEffect.loadNBTData(listTag);
        }
    }

    public void init(Player player) {
        this.setPlayer(player);
        this.level=player.level();
        if(player instanceof ServerPlayer serverPlayer){
            this.durationEffect=new ActiveEffectDuration(serverPlayer);
        }
    }

    public PlayerCooldowns getCooldowns() {
        return this.cooldowns;
    }
    public ActiveEffectDuration getActiveEffectDuration(){
        return this.durationEffect;
    }
    public void setActiveEffectDuration(ActiveEffectDuration activeEffectDuration){
        this.durationEffect=activeEffectDuration;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    public <P extends SkillPlayerCapability> P getPatch(LivingEntity replaced, Class<P> pClass){
        return SGCapability.getEntityCap(replaced,pClass);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }


    public static class SkillPlayerProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
        private final LazyOptional<ISkillPlayer> instance = LazyOptional.of(SkillPlayerCapability::new);

        @NonNull
        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return SGCapability.POWER_CAPABILITY.orEmpty(cap,instance.cast());
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.orElseThrow(NullPointerException::new).serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
        }
    }
}
