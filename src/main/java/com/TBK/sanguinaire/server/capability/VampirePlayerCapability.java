package com.TBK.sanguinaire.server.capability;

import com.TBK.sanguinaire.common.registry.SGParticles;
import com.TBK.sanguinaire.common.api.Clan;
import com.TBK.sanguinaire.common.api.IVampirePlayer;
import com.TBK.sanguinaire.common.registry.SGAttribute;
import com.TBK.sanguinaire.server.manager.*;
import com.TBK.sanguinaire.server.network.PacketHandler;
import com.TBK.sanguinaire.server.network.messager.PacketConvertVampire;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class VampirePlayerCapability implements IVampirePlayer {
    Player player;
    Level level;
    public int generation=0;
    public Clan clan=Clan.NONE;
    boolean isVampire=false;
    public int age=0;
    public int growTimerMax=72000;
    public int growTimer=0;
    public LimbsPartRegeneration limbsPartRegeneration=new LimbsPartRegeneration();
    public int loseBlood=0;

    public static VampirePlayerCapability get(Player player){
        return SGCapability.getEntityVam(player, VampirePlayerCapability.class);
    }

    public boolean legsLess(){
        return this.getLimbsPartRegeneration().loseLimb("left_leg") && this.getLimbsPartRegeneration().loseLimb("right_leg");
    }
    public boolean armsLess(){
        return this.getLimbsPartRegeneration().loseLimb("left_arm") && this.getLimbsPartRegeneration().loseLimb("right_arm");
    }
    public boolean bodyLess(){
        return this.getLimbsPartRegeneration().loseLimb("body");
    }
    public boolean headLess(){
        return this.getLimbsPartRegeneration().loseLimb("head");
    }
    public boolean noMoreLimbs(){
        return this.legsLess() && this.bodyLess() && this.armsLess() && this.headLess();
    }
    public boolean cantMove(){
        return this.legsLess() || this.bodyLess();
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public int getGeneration() {
        return this.generation;
    }

    @Override
    public Clan getClan() {
        return this.clan;
    }
    public void convert(boolean isVampire){
        this.setIsVampire(!isVampire);
        if(!isVampire){
            this.age=0;
            this.setGeneration(1);
            this.setClan(Clan.DRAKUL);
        }
        if(!this.level.isClientSide){
            PacketHandler.sendToPlayer(new PacketConvertVampire(isVampire), (ServerPlayer) this.player);
        }
    }

    @Override
    public void setGeneration(int generation) {
        this.generation=generation;
    }

    @Override
    public void setClan(Clan clan) {
        this.clan=clan;
    }

    @Override
    public void bite(Player player, Entity target) {
        if(target instanceof LivingEntity living){
            this.drainBlood(1);
            this.loseBlood=6000;
        }
    }
    public void loseBlood(int blood){
        double bloodActually=player.getAttributeValue(SGAttribute.BLOOD_VALUE.get());
        double finalBlood=Math.max(bloodActually-blood,0);
        player.getAttribute(SGAttribute.BLOOD_VALUE.get()).setBaseValue(finalBlood);
    }
    public void drainBlood(int blood){
        double bloodActually=player.getAttributeValue(SGAttribute.BLOOD_VALUE.get());
        double finalBlood=Math.min(blood+bloodActually,20);
        player.getAttribute(SGAttribute.BLOOD_VALUE.get()).setBaseValue(finalBlood);
    }
    public double getBlood(){
        return player.getAttributeValue(SGAttribute.BLOOD_VALUE.get());
    }
    public void setBlood(double blood){
        player.getAttribute(SGAttribute.BLOOD_VALUE.get()).setBaseValue(blood);
    }

    public void losePart(String id, RegenerationInstance instance, Player player){
        this.limbsPartRegeneration.addLoseLimb(id,instance);
        if(player instanceof ServerPlayer serverPlayer){
            this.limbsPartRegeneration.syncPlayer();
        }
    }
    @Override
    public SkillPlayerCapability getSkillCap(Player player) {
        return SkillPlayerCapability.get(player);
    }

    @Override
    public void tick(Player player) {
        if(this.loseBlood>0){
            this.loseBlood--;
            if(this.loseBlood==0){
                this.loseBlood(1);
                if(this.getBlood()>0){
                    this.loseBlood=6000;
                }
            }
        }
        if(this.noMoreLimbs()){
            player.setPos(player.getX(),player.getY(),player.getZ());
            player.setDeltaMovement(0.0F,0.0F,0.0F);
        }
        if(this.player instanceof ServerPlayer){
            if(this.getLimbsPartRegeneration().hasRegenerationLimbs()){
                this.getLimbsPartRegeneration().tick(player);
            }
            if(this.growTimer++>=this.growTimerMax){
                this.growTimer=0;
                this.age++;
            }
            if(this.isDurationEffectTick(player.tickCount,2+this.age/10)){
                if (player.getHealth() < player.getMaxHealth()) {
                    float f = player.getHealth();
                    if (f > 0.0F) {
                        player.setHealth(f + 1);
                    }
                }
            }
            this.syncCap(player);
        }else if(this.level.isClientSide){
            if(this.getLimbsPartRegeneration().hasRegenerationLimbs()){
                this.getLimbsPartRegeneration().tick(player);
            }
        }

        if(player.level().isClientSide){
            RegenerationInstance instance=this.getLimbsPartRegeneration().loseLimbs.get("head");
            if(instance!=null){
                float porcentBlood=instance.getCooldownPercent();
                float f=3.0F*porcentBlood;
                ParticleOptions particleoptions = SGParticles.BLOOD_PARTICLES.get();
                int i;
                float f1;
                i = Mth.ceil((float)Math.PI * f * f);
                f1 = f;

                for(int k=0;k<10;k++){
                    for(int j = 0; j < i; ++j) {
                        float f2 = player.getRandom().nextFloat() * ((float)Math.PI * 2F);
                        float f3 = Mth.sqrt(player.getRandom().nextFloat()) * f1;
                        double d0 = player.getX() + (double)(Mth.cos(f2) * f3);
                        double d2 = player.getY()+0.3D;
                        double d4 = player.getZ() + (double)(Mth.sin(f2) * f3);

                        player.level().addParticle(particleoptions, d0, d2, d4, 0.0F, 0.1F, 0.0F);
                    }
                }
            }
        }
    }

    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        int k = 100 >> p_19456_;
        if (k > 0) {
            return p_19455_ % k == 0;
        } else {
            return true;
        }
    }
    public int getRegTimer(){
        return (int) (140-140*(0.5F*(this.age/100)+0.36F*(1.0F-this.generation/10.0F)));
    }

    public void syncCap(Player player){
        if(player instanceof ServerPlayer serverPlayer){
            this.getLimbsPartRegeneration().syncPlayer();
            PacketHandler.sendToPlayer(new PacketConvertVampire(!this.isVampire),serverPlayer);
        }
    }

    @Override
    public boolean isVampire() {
        return this.isVampire;
    }



    @Override
    public void setIsVampire(boolean bol) {
        this.isVampire=bol;
    }

    @Override
    public void setPlayer(Player player) {
        this.player=player;
    }

    public void clone(VampirePlayerCapability capability,Player player){
        capability.init(player);
        capability.setClan(this.getClan());
        capability.setIsVampire(this.isVampire());
        capability.setGeneration(this.getGeneration());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag=new CompoundTag();
        tag.putBoolean("isVampire",this.isVampire);
        tag.putInt("generation",this.generation);
        tag.putString("clan",this.clan.toString());
        tag.putInt("loseBlood",this.loseBlood);
        tag.putDouble("blood",this.getBlood());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.isVampire=nbt.getBoolean("isVampire");
        this.generation=nbt.getInt("generation");
        this.clan=Clan.valueOf(nbt.getString("clan"));
        this.loseBlood=nbt.getInt("loseBlood");
        this.setBlood(nbt.getDouble("blood"));
        if(this.player!=null && !this.level.isClientSide){
            PacketHandler.sendToAllTracking(new PacketConvertVampire(!this.isVampire()),this.player);
        }
    }

    public void init(Player player) {
        this.setPlayer(player);
        this.level=player.level();
        if(player instanceof ServerPlayer){
            this.setLimbsPartRegeneration(new LimbsPartRegeneration((ServerPlayer) player));
        }
    }
    public void setLimbsPartRegeneration(LimbsPartRegeneration limbsPartRegeneration){
        this.limbsPartRegeneration=limbsPartRegeneration;
    }

    public LimbsPartRegeneration getLimbsPartRegeneration() {
        return this.limbsPartRegeneration;
    }


    public static class VampirePlayerProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
        private final LazyOptional<IVampirePlayer> instance = LazyOptional.of(VampirePlayerCapability::new);

        @NonNull
        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return SGCapability.VAMPIRE_CAPABILITY.orEmpty(cap,instance.cast());
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
