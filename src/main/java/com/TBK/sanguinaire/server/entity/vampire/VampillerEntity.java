package com.TBK.sanguinaire.server.entity.vampire;

import com.TBK.sanguinaire.common.registry.SGSkillAbstract;
import com.TBK.sanguinaire.common.registry.SGSounds;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import com.TBK.sanguinaire.server.entity.projetile.BloodOrbProjetile;
import com.TBK.sanguinaire.server.manager.ActiveEffectDuration;
import com.TBK.sanguinaire.server.manager.CooldownInstance;
import com.TBK.sanguinaire.server.manager.DurationInstance;
import com.TBK.sanguinaire.server.manager.PlayerCooldowns;
import com.TBK.sanguinaire.server.skill.SkillAbstract;
import com.TBK.sanguinaire.server.skill.drakul.BloodOrb;
import com.TBK.sanguinaire.server.skill.drakul.BloodSlash;
import com.TBK.sanguinaire.server.skill.drakul.BloodTendrils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;

public class VampillerEntity extends Monster implements GeoEntity, RangedAttackMob {
    private final AnimatableInstanceCache cache= GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(VampillerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FORM_BAT =
            SynchedEntityData.defineId(VampillerEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> IS_CASTING =
            SynchedEntityData.defineId(VampillerEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> STRATEGY =
            SynchedEntityData.defineId(VampillerEntity.class, EntityDataSerializers.INT);

    private Map<String,CooldownInstance > cooldownSkill=new HashMap<>();
    private Map<Integer,SkillAbstract> skills=new HashMap<>();

    private int attackTimer=0;
    private int castingTimer=0;
    private int spellId=-1;
    private int cooldownBite=0;
    private int convertBat=0;
    private int avoidTimer=0;
    private int transformBatClient=0;


    public VampillerEntity(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 40.D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D).build();

    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> {
            if(this.isBat())return PlayState.STOP;
            if(this.isAttacking()) {
                state.getController().setAnimationSpeed(2.5D);
                state.getController().setAnimation(RawAnimation.begin().thenPlay("vampiller_drakul.bite"));
            }else if(this.isCasting()){
                state.getController().setAnimation(RawAnimation.begin().thenPlay("vampiller_drakul.blood_orb"));
            }else if(this.convertBat>0){
                state.getController().setAnimation(RawAnimation.begin().thenPlay("vampiller_drakul.batform"));
            }else {
                state.getController().setAnimationSpeed(this.isAggressive() ? 2.0D : 1.0D);
                state.getController().setAnimation(RawAnimation.begin().thenLoop("vampiller_drakul.idle"));
            }
            return PlayState.CONTINUE;
        }));
        controllers.add(new AnimationController<>(this, "controller_legs", 0, state -> {
            if(this.isBat())return PlayState.STOP;
            boolean isMove= !(state.getLimbSwingAmount() > -0.15F && state.getLimbSwingAmount() < 0.15F);
            if(isMove) {
                state.getController().setAnimationSpeed(this.isAggressive() ? 3.0D : 1.0D);
                state.getController().setAnimation(RawAnimation.begin().thenLoop("vampiller_drakul.legs1"));
            }else {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("vampiller_drakul.legs2"));
            }
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        super.onSyncedDataUpdated(p_21104_);
        if(p_21104_.equals(FORM_BAT)){

        }
    }

    public void setAttacking(boolean pAttacking){
        this.entityData.set(ATTACKING,pAttacking);
        this.attackTimer=pAttacking ? 20 : 0;
    }
    public boolean isAttacking(){
        return this.entityData.get(ATTACKING);
    }

    public void setFormBat(boolean pIsBat){
        this.entityData.set(FORM_BAT,pIsBat);
    }

    public boolean isBat(){
        return this.entityData.get(FORM_BAT);
    }
    public void setIsCasting(boolean pIsCasting){
        this.entityData.set(IS_CASTING,pIsCasting);
    }
    public boolean isCasting(){
        return this.entityData.get(IS_CASTING);
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING,false);
        this.entityData.define(FORM_BAT,false);
        this.entityData.define(IS_CASTING,false);
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        if(!super.doHurtTarget(p_21372_)){
            return false;
        }
        if(p_21372_ instanceof Player player){
            VampirePlayerCapability cap=VampirePlayerCapability.get(player);
            if(!cap.isVampire() && this.random.nextFloat()<0.05F){
                cap.convert(false);
            }
        }
        return true;
    }

    @Override
    public void handleEntityEvent(byte p_21375_) {
        if(p_21375_==4){
            this.setAttacking(true);
        }else if (p_21375_==2){
            this.convertBat=12;
            this.cooldownBite=600;
        }else if (p_21375_==8){
            this.avoidTimer=150;
            this.setFormBat(true);
        } else if (p_21375_==0){
            this.setIsCasting(true);
            this.castingTimer=10;
            this.spellId=0;
        }
        super.handleEntityEvent(p_21375_);

    }

    public boolean decrementCooldown(CooldownInstance c, int amount) {
        c.decrementBy(amount);
        return c.getCooldownRemaining() <= 0;
    }

    @Override
    public void tick() {
        super.tick();
        if(!this.cooldownSkill.isEmpty()){
            var powers = this.cooldownSkill.entrySet().stream().filter(x -> decrementCooldown(x.getValue(), 1)).toList();
            powers.forEach(power -> cooldownSkill.remove(power.getKey()));
        }
        if(this.isCasting() && this.getTarget()!=null){
            this.castingTimer--;
            if(this.castingTimer==0){
                LivingEntity target=this.getTarget();
                SkillAbstract skillAbstract=this.skills.get(this.spellId);
                this.setIsCasting(false);
                this.spellId=-1;
                if(skillAbstract.equals(SGSkillAbstract.BLOOD_ORB)){
                    BloodOrbProjetile orb =new BloodOrbProjetile(this.level(),this,0);
                    orb.setPos(this.getEyePosition());
                    orb.setChargedLevel(4);
                    orb.shoot(target.getX()-this.getX(),target.getY()-this.getY(),target.getZ()-this.getZ(),1.0F,1.0F);
                    this.level().addFreshEntity(orb);
                    this.cooldownSkill.put(skillAbstract.name,new CooldownInstance(200));
                }
            }
        }
        if(this.isAttacking()){
            this.attackTimer--;
            if(this.attackTimer==0){
                this.setAttacking(false);
                this.convertBat=12;
                this.cooldownBite=600;
                if(!this.level().isClientSide){
                    this.level().broadcastEntityEvent(this,(byte) 2);
                }
            }
        }
        if(this.isDurationEffectTick(this.tickCount,5) && !this.isOnFire()){
            if (this.getHealth() < this.getMaxHealth()) {
                float f = this.getHealth();
                if (f > 0.0F) {
                    this.setHealth(f + 1);
                }
            }
        }
        if(this.convertBat>0){
            this.convertBat--;
            if(this.convertBat==2){
                this.setFormBat(true);
                this.avoidTimer=150;
            }
        }

        if(this.isBat()){
            this.avoidTimer--;
            if (this.avoidTimer<=0){
                this.setFormBat(false);
            }
        }

        if(this.cooldownBite>0){
            this.cooldownBite--;
        }

        if(!this.canMove()){
            this.getNavigation().stop();
        }
        this.refreshDimensions();
    }
    public boolean canMove(){
        return this.convertBat<=0 && !this.isCasting();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);
        var listTag = new ListTag();
        this.cooldownSkill.forEach((spellId,cooldown) -> {
            if (cooldown.getCooldownRemaining() > 0) {
                CompoundTag ct = new CompoundTag();
                ct.putString("name", spellId);
                cooldown.saveNBTData(ct);
                listTag.add(ct);
            }
        });
        p_21484_.put("cooldown",listTag);
    }

    @Override
    public EntityDimensions getDimensions(Pose p_21047_) {
        return this.isBat() ? EntityDimensions.scalable(0.5F,0.5F) : super.getDimensions(p_21047_);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);
        if(p_21450_.contains("cooldowns")){
            ListTag listTag=p_21450_.getList("cooldowns",10);
            if (listTag != null) {
                listTag.forEach(tag -> {
                    CompoundTag t = (CompoundTag) tag;
                    String powerId = t.getString("name");
                    this.cooldownSkill.put(powerId,new CooldownInstance(t));
                });
            }
        }
        this.initSkill();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag p_21438_) {
        this.initSkill();
        return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
    }
    public void initSkill(){
        this.skills.put(0,new BloodOrb());
        this.skills.put(1,new BloodSlash());
        this.skills.put(2,new BloodTendrils());
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2,new AttackGoal(this,2.0D,true));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(4,new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 15, 10.0F){
            @Override
            public boolean canUse() {
                return super.canUse() && VampillerEntity.this.cooldownBite>0 && !VampillerEntity.this.isCasting();
            }
        });
        this.goalSelector.addGoal(1,new AvoidEntityGoal<>(this,LivingEntity.class, 15.0f, 3d, 3d){
            @Override
            public boolean canUse() {
                if(this.mob.getTarget()==null){
                    return false;
                }else if(!super.canUse()){
                    return false;
                }
                this.toAvoid=this.mob.getTarget();
                return  VampillerEntity.this.avoidTimer>0;
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, true));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        super.registerGoals();
    }



    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void performRangedAttack(LivingEntity p_33317_, float p_33318_) {
        int id=0;
        this.setIsCasting(true);
        this.spellId=id;
        this.castingTimer=10;
        if(!this.level().isClientSide){
            this.level().broadcastEntityEvent(this,(byte) 0);
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

    static class AttackGoal extends MeleeAttackGoal {
        private final VampillerEntity goalOwner;

        public AttackGoal(VampillerEntity entity, double speedModifier, boolean followWithoutLineOfSight) {
            super(entity, speedModifier, followWithoutLineOfSight);
            this.goalOwner = entity;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.goalOwner.isBat() && this.goalOwner.cooldownBite<=0;
        }

        @Override
        public void tick() {
            super.tick();
        }

        @Override
        protected void checkAndPerformAttack(@NotNull LivingEntity entity, double distance) {
            double d0 = this.getAttackReachSqr(entity) + 5.0D;
            if (distance <= d0 && this.goalOwner.attackTimer <= 0 && this.goalOwner.cooldownBite<=0) {
                this.resetAttackCooldown();
                this.goalOwner.doHurtTarget(entity);
                this.goalOwner.level().playSound(null,this.goalOwner, SGSounds.VAMPILLER_HURT.get(), SoundSource.HOSTILE,1.5F,1.0F);
                this.goalOwner.navigation.stop();
                this.goalOwner.getLookControl().setLookAt(entity,30,30);
                this.goalOwner.setYBodyRot(this.goalOwner.getYHeadRot());
            }
        }

        @Override
        protected void resetAttackCooldown() {
            this.goalOwner.setAttacking(true);
            if(!this.goalOwner.level().isClientSide){
                this.goalOwner.level().broadcastEntityEvent(this.goalOwner,(byte) 4);
            }
        }
    }

    public void playStepSound(BlockPos p_28864_, BlockState p_28865_) {
        this.playSound(SoundEvents.WOLF_STEP, 1.0F, 1.0F);
    }

    public SoundEvent getAmbientSound() {
        return SoundEvents.ILLUSIONER_AMBIENT;
    }
    public SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }
    public SoundEvent getHurtSound(DamageSource p_28872_) {
        return SGSounds.VAMPILLER_HURT.get();
    }
    enum Strategy{
        MELEE(0),
        BACK(1),
        RE_POS(2),
        RANGER(3);
        private static final Strategy[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(Strategy::getId)).toArray(Strategy[]::new);

        private final int id;
        Strategy(int id){
            this.id=id;
        }

        public int getId() {
            return id;
        }
        public static Strategy byId(int p_30987_) {
            return BY_ID[p_30987_ % BY_ID.length];
        }
    }
}
