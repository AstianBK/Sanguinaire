package com.TBK.sanguinaire.server.capability;

import com.TBK.sanguinaire.common.api.ISkillPlayer;
import com.TBK.sanguinaire.server.manager.*;
import com.TBK.sanguinaire.server.network.PacketHandler;
import com.TBK.sanguinaire.server.network.messager.PacketSyncBlood;
import com.TBK.sanguinaire.server.network.messager.PacketSyncPosHotBar;
import com.TBK.sanguinaire.server.skill.*;
import com.google.common.collect.Maps;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
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
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Map;

public class SkillPlayerCapability implements ISkillPlayer, GeoEntity {
    public final AnimatableInstanceCache cache= GeckoLibUtil.createInstanceCache(this);
    public SkillAbstract lastUsingSkillAbstract=SkillAbstract.NONE;
    Player player;
    Level level;
    public SkillAbstracts powers=new SkillAbstracts(Maps.newHashMap());
    int posSelectSkillAbstract=1;
    int castingTimer=0;
    public PlayerCooldowns cooldowns=new PlayerCooldowns();
    public ActiveEffectDuration durationEffect=new ActiveEffectDuration();
    public boolean isTransform=false;
    public Forms form=Forms.NONE;
    public static SkillPlayerCapability get(Player player){
        return SGCapability.getEntityCap(player,SkillPlayerCapability.class);
    }
    public VampirePlayerCapability getPlayerVampire(){
        return VampirePlayerCapability.get(this.player);
    }

    public void setPosSelectSkillAbstract(int pos){
        this.posSelectSkillAbstract=pos;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void setPlayer(Player player) {
        this.player=player;
    }
    public void setIsTransform(boolean isTransform){
        this.isTransform=isTransform;
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
                this.durationEffect.tick(1);
            }
        }else if(this.level.isClientSide){
            if (this.cooldowns.hasCooldownsActive()){
                this.cooldowns.tick(1);
            }
            if(this.durationEffect.hasDurationsActive()){
                this.durationEffect.tickDurations(this);
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
    }

    @Override
    public void onJoinGame(Player player, EntityJoinLevelEvent event) {
        this.powers.addSkillAbstracts(0,new BatForm());
        this.powers.addSkillAbstracts(1,new TentacleBlood());
        this.powers.addSkillAbstracts(2,new SlashBlood());
    }

    @Override
    public void handledSkill(SkillAbstract power) {
        power.startSkillAbstract(this);
    }

    @Override
    public void stopSkill(SkillAbstract power) {
        this.setLastUsingSkill(SkillAbstract.NONE);
        power.stopSkillAbstract(this);
        power.getTargets().clear();
        this.getCooldowns().addCooldown(power,300);
        this.syncSkill(this.getPlayer());
    }

    @Override
    public void handledPassive(Player player, SkillAbstract power) {

    }

    @Override
    public boolean canUseSkill(SkillAbstract skillAbstract) {
        return !this.getCooldowns().isOnCooldown(skillAbstract) && this.getPlayerVampire().getBlood()>=skillAbstract.getCostBloodBase();
    }

    @Override
    public Map<Integer, SkillAbstract> getPassives() {
        return null;
    }

    @Override
    public SkillAbstracts getHotBarSkill() {
        return this.powers;
    }

    @Override
    public void syncSkill(Player player) {
        if(player instanceof ServerPlayer serverPlayer){
            this.cooldowns.syncToPlayer(serverPlayer);
            this.durationEffect.syncAllToPlayer();
        }
    }
    public void syncPos(int pos){
        PacketHandler.sendToServer(new PacketSyncPosHotBar(pos));
    }

    @Override
    public void upSkill() {
        this.posSelectSkillAbstract=this.posSelectSkillAbstract+1==this.powers.powers.size() ? 0 : this.posSelectSkillAbstract+1;
        if (this.getPlayer()!=null){
            this.getPlayer().sendSystemMessage(Component.nullToEmpty(this.posSelectSkillAbstract+" Se cambio al"+this.getSelectSkill().name));
        }
        if(this.level.isClientSide){
            this.syncPos(this.posSelectSkillAbstract);
        }
    }

    @Override
    public void downSkill() {

    }

    @Override
    public void swingHand(Player player) {
        if(this.canUseSkill(this.getSelectSkill())){
            boolean skillActive=this.durationEffect.hasDurationForSkill(this.getSelectSkill());
            if(!skillActive && !this.getSelectSkill().isPassive || this.getSelectSkill().isCanReActive()){
                SkillAbstract power=this.getSelectSkill();
                if(power.canReActive && skillActive){
                    DurationInstance instance=this.durationEffect.getDurationInstance(power.name);
                    this.removeActiveEffect(instance);
                }else {
                    DurationInstance instance=new DurationInstance(power.name,power.level,power.castingDuration+50*power.level,200);
                    this.addActiveEffect(instance,player);
                    this.setLastUsingSkill(this.getSelectSkill());
                    this.getPlayer().sendSystemMessage(Component.nullToEmpty("Se lanzo el poder"));
                    this.handledSkill(power);
                }
            }
        }
    }

    public void removeActiveEffect(DurationInstance instance){
        this.durationEffect.removeDuration(instance,DurationResult.TIMEOUT,true);
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
        tag.putBoolean("isTransform",this.isTransform);
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
        this.isTransform=nbt.getBoolean("isTransform");
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
        controllers.add(new AnimationController<>(this, "controller", 0, state -> {
            Player player1=this.getPlayer();
            SkillPlayerCapability replacedExecutioner = getPatch(player1,SkillPlayerCapability.class);
            if (player1 == null) return PlayState.STOP;
            boolean isMove= !(state.getLimbSwingAmount() > -0.15F && state.getLimbSwingAmount() < 0.15F);
            if(isMove && player1.isSprinting()) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("batform.move"));
            }else {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("batform.idle"));
            }
            return PlayState.CONTINUE;
        }));
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
