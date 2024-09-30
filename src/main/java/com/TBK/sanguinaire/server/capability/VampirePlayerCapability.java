package com.TBK.sanguinaire.server.capability;

import com.TBK.sanguinaire.common.item.GobletItem;
import com.TBK.sanguinaire.common.registry.SGParticles;
import com.TBK.sanguinaire.common.api.Clan;
import com.TBK.sanguinaire.common.api.IVampirePlayer;
import com.TBK.sanguinaire.common.registry.SGAttribute;
import com.TBK.sanguinaire.common.registry.SGSounds;
import com.TBK.sanguinaire.server.manager.*;
import com.TBK.sanguinaire.server.network.PacketHandler;
import com.TBK.sanguinaire.server.network.messager.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
    public int clientDrink=0;
    private int hugeTick=0;


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
            this.setGeneration(10);
            this.setClan(Clan.DRAKUL);
        }else {
            SkillPlayerCapability capability=this.getSkillCap(player);
            capability.passives.getSkills().forEach(e->e.getSkillAbstract().stopSkillAbstract(capability));
        }
        if(!this.level.isClientSide){
            PacketHandler.sendToPlayer(new PacketConvertVampire(isVampire), (ServerPlayer) this.player);
        }
    }

    public int getMaxBlood(){
        return ((this.age/10)*2)+((11-this.generation)*2);
    }

    @Override
    public void setGeneration(int generation) {
        this.generation=Math.max(generation,1);
    }
    public void setAge(int age){
        this.age=Math.min(age,100);
    }

    @Override
    public void setClan(Clan clan) {
        this.clan=clan;
    }

    @Override
    public void bite(Player player, Entity target) {
        BiterEntityCap cap=SGCapability.getEntityEntity(target, BiterEntityCap.class);
        if(cap!=null && cap.canBiter()){
            if(this.getBlood()<this.getMaxBlood()){
                this.drainBlood(1);
                cap.onBite(this,target);
                this.hugeTick=0;
                player.level().playSound(null,target, SGSounds.BLOOD_DRINK.get(), SoundSource.PLAYERS,1.0F,1.0F);
            }else if(this.canFillGobletItem(player)){
                ItemStack goblet=getGobletInHand(player);
                int finalBlood =GobletItem.getBlood(goblet)+1;
                cap.onBite(this,target);
                GobletItem.setBlood(goblet, finalBlood);
                if(finalBlood==10){
                    player.level().playSound(null,target, SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS,1.0F,1.0F);
                }else {
                    player.level().playSound(null,target, SoundEvents.CHICKEN_DEATH, SoundSource.PLAYERS,1.0F,1.0F);
                }
            }
        }
        if(this.level.isClientSide){
            this.clientDrink=10;
            PacketHandler.sendToServer(new PacketSyncBiteTarget(target.getId(),target.getUUID()));
        }
    }
    public ItemStack getGobletInHand(Player player){
        return GobletItem.canFillGoblet(player.getMainHandItem()) ? player.getMainHandItem()  : player.getOffhandItem();
    }

    public boolean canFillGobletItem(Player player){
        return GobletItem.canFillGoblet(player.getMainHandItem()) || GobletItem.canFillGoblet(player.getOffhandItem());
    }
    public boolean loseBlood(int blood){
        double bloodActually=player.getAttributeValue(SGAttribute.BLOOD);
        double finalBlood=Math.max(bloodActually-blood,0);
        this.setBlood(finalBlood);
        return bloodActually!=finalBlood;
    }

    public void drainBlood(int blood){
        double bloodActually=player.getAttributeValue(SGAttribute.BLOOD);
        double finalBlood=Math.min(blood+bloodActually,this.getMaxBlood());
        this.setBlood(finalBlood);
    }
    public double getBlood(){
        return player.getAttributeValue(SGAttribute.BLOOD);
    }
    public void setBlood(double blood){
        player.getAttribute(SGAttribute.BLOOD).setBaseValue(blood);
    }

    public void losePart(String id, RegenerationInstance instance, Player player){
        this.limbsPartRegeneration.addLoseLimb(id,instance);
        if(player instanceof ServerPlayer){
            this.limbsPartRegeneration.syncPlayer();
        }
    }
    @Override
    public SkillPlayerCapability getSkillCap(Player player) {
        return SkillPlayerCapability.get(player);
    }

    @Override
    public void tick(Player player) {
        if(this.getBlood()>0){
            if(this.hugeTick++>2000){
                this.loseBlood(2);
                this.hugeTick=0;
            }
        }
        if (player.isAlive() && this.getSkillCap(player).isTransform) {
            boolean flag = this.isSunBurnTick(player);
            if (flag) {
                ItemStack itemstack = player.getItemBySlot(EquipmentSlot.HEAD);
                if (!itemstack.isEmpty()) {
                    if (itemstack.isDamageableItem()) {
                        itemstack.setDamageValue(itemstack.getDamageValue() + player.level().random.nextInt(2));
                        if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                            player.broadcastBreakEvent(EquipmentSlot.HEAD);
                            player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    player.setSecondsOnFire(8);
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
            if(this.isDurationEffectTick(player.tickCount,2+this.age/10)  && !player.isOnFire()){
                if (player.getHealth() < player.getMaxHealth()) {
                    float f = player.getHealth();
                    if (f > 0.0F) {
                        player.setHealth(f + 1);
                    }
                }
            }
            player.getFoodData().eat(20,0.0F);
        }else if(this.level.isClientSide){
            if(this.getLimbsPartRegeneration().hasRegenerationLimbs()){
                this.getLimbsPartRegeneration().tick(player);
            }
            if (this.clientDrink>0){
                this.clientDrink--;
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

    protected boolean isSunBurnTick(Player player) {
        if (this.level.isDay() && !this.level.isClientSide) {
            float f = player.getLightLevelDependentMagicValue();
            BlockPos blockpos = BlockPos.containing(player.getX(), player.getEyeY(), player.getZ());
            boolean flag = player.isInWaterRainOrBubble() || player.isInPowderSnow || player.wasInPowderSnow;
            if (f > 0.5F && player.level().random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && !flag && player.level().canSeeSky(blockpos)) {
                return true;
            }
        }

        return false;
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
            PacketHandler.sendToPlayer(new PacketSyncVampireData(this.age,this.generation,this.isVampire),serverPlayer);
        }
    }

    @Override
    public boolean isVampire() {
        return this.isVampire;
    }


    public boolean canRevive() {
        return this.getBlood()>=4;
    }


    @Override
    public void setIsVampire(boolean bol) {
        this.isVampire=bol;
    }

    @Override
    public void setPlayer(Player player) {
        this.player=player;
    }

    public void clone(VampirePlayerCapability capability,Player player,Player newPlayer){
        if(!this.level.isClientSide){
            PacketHandler.sendToPlayer(new PacketHandlerPowers(1, newPlayer,player), (ServerPlayer) player);
        }
        this.init(newPlayer);
        this.setClan(capability.getClan());
        this.setIsVampire(capability.isVampire);
        this.setGeneration(capability.getGeneration());
        this.setBlood(0);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag=new CompoundTag();
        tag.putBoolean("isVampire",this.isVampire);
        tag.putInt("age",this.age);
        tag.putInt("generation",this.generation);
        tag.putString("clan",this.clan.toString());
        tag.putDouble("blood",this.getBlood());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.isVampire=nbt.getBoolean("isVampire");
        this.setAge(nbt.getInt("age"));
        this.generation=nbt.getInt("generation");
        this.clan=Clan.valueOf(nbt.getString("clan"));
        this.setBlood(nbt.getDouble("blood"));
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
