package com.TBK.sanguinaire.server.capability;

import com.TBK.sanguinaire.common.api.ISkillPlayer;
import com.TBK.sanguinaire.server.manager.*;
import com.TBK.sanguinaire.server.skill.SkillAbstract;
import com.TBK.sanguinaire.server.skill.SkillAbstracts;
import com.google.common.collect.Maps;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SkillPlayerCapability implements ISkillPlayer {
    public SkillAbstract lastUsingSkillAbstract=SkillAbstract.NONE;
    Player player;
    Level level;
    public SkillAbstracts powers=new SkillAbstracts(Maps.newHashMap());
    public SkillAbstracts activesSkillAbstracts=new SkillAbstracts(Maps.newHashMap());
    Map<Integer,SkillAbstract> passives= Maps.newHashMap();
    int posSelectSkillAbstract=1;
    int castingTimer=0;
    public PlayerCooldowns cooldowns=new PlayerCooldowns();
    public ActiveEffectDuration durationEffect;
    public static SkillPlayerCapability get(Player player){
        return SGCapability.getEntityCap(player,SkillPlayerCapability.class);
    }


    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void setPlayer(Player player) {
        this.player=player;
    }

    @Override
    public SkillAbstract getSelectSkill() {
        return this.getSkillForHotBar(this.posSelectSkillAbstract);
    }

    @Override
    public SkillAbstract getSkillForHotBar(int pos) {
        return this.powers.get(pos);
    }

    @Override
    public int getCooldownSkill() {
        return 0;
    }

    @Override
    public int getCastingTimer() {
        return this.castingTimer;
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
        if(player instanceof ServerPlayer){
            if (this.cooldowns.hasCooldownsActive()){
                this.cooldowns.tick(1);
            }
            if(this.durationEffect.hasDurationsActive()){
                this.durationEffect.tick(1,this);
            }
        }else if(this.level.isClientSide){
            if (this.cooldowns.hasCooldownsActive()){
                this.cooldowns.tick(1);
            }
            if(this.durationEffect.hasDurationsActive()){
                this.durationEffect.tickDurations();
            }
        }
        if(!this.powers.getSkills().isEmpty()){
            this.powers.getSkills().forEach(e->{
                if(this.durationEffect.hasDurationForSkill(e.getSkillAbstract())){
                    e.getSkillAbstract().tick(this);
                    this.durationEffect.decrementDurationCount(e.getSkillAbstract());
                }else if(e.getSkillAbstract().isPassive){
                    e.getSkillAbstract().effectPassiveForTick(player);
                }
            });
        }
        if(this.getLastUsingSkill()!=null && this.lastUsingSkill()){
            if(this.castingTimer==this.getLastUsingSkill().lauchTime){
                this.handledSkill(player,this.getLastUsingSkill());
            }
            this.castingTimer--;
        }
    }

    @Override
    public void onJoinGame(Player player, EntityJoinLevelEvent event) {

    }

    @Override
    public void handledSkill(Player player, SkillAbstract power) {

    }

    @Override
    public void stopSkill(Player player, SkillAbstract power) {

    }

    @Override
    public void handledPassive(Player player, SkillAbstract power) {

    }

    @Override
    public boolean canUseSkill(SkillAbstract skillAbstract) {
        return this.getCooldowns().isOnCooldown(skillAbstract);
    }

    @Override
    public Map<Integer, SkillAbstract> getPassives() {
        return null;
    }

    @Override
    public SkillAbstract getHotBarSkill() {
        return null;
    }

    @Override
    public void syncSkill(Player player) {
        if(player instanceof ServerPlayer serverPlayer){
            this.cooldowns.syncToPlayer(serverPlayer);
            this.durationEffect.syncAllToPlayer();
        }
    }

    @Override
    public void upSkill() {

    }

    @Override
    public void downSkill() {

    }

    @Override
    public void swingHand(Player player) {

    }


    public void addActiveEffect(DurationInstance instance, Player player){
        this.durationEffect.forceAddDuration(instance);
        if(player instanceof  ServerPlayer serverPlayer){
            this.durationEffect.syncToPlayer(instance);
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag=new CompoundTag();
        this.powers.save(tag);
        tag.putInt("select_power",this.posSelectSkillAbstract);
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
        this.powers=new SkillAbstracts(nbt);
        this.posSelectSkillAbstract=nbt.getInt("select_power");
        if(nbt.contains("cooldowns")){
            ListTag listTag=new ListTag();
            this.cooldowns.loadNBTData(listTag);
        }
        if(nbt.contains("activeEffect")){
            ListTag listTag=new ListTag();
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
