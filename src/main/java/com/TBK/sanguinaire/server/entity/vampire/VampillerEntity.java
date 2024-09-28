package com.TBK.sanguinaire.server.entity.vampire;

import com.TBK.sanguinaire.common.registry.SGSkillAbstract;
import com.TBK.sanguinaire.common.registry.SGSounds;
import com.TBK.sanguinaire.server.entity.projetile.BloodOrbProjetile;
import com.TBK.sanguinaire.server.manager.ActiveEffectDuration;
import com.TBK.sanguinaire.server.manager.CooldownInstance;
import com.TBK.sanguinaire.server.manager.DurationInstance;
import com.TBK.sanguinaire.server.manager.PlayerCooldowns;
import com.TBK.sanguinaire.server.skill.SkillAbstract;
import com.TBK.sanguinaire.server.skill.drakul.BloodOrb;
import com.TBK.sanguinaire.server.skill.drakul.BloodSlash;
import com.TBK.sanguinaire.server.skill.drakul.BloodTendrils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class VampillerEntity extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache= GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(VampillerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FORM_BAT =
            SynchedEntityData.defineId(VampillerEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> IS_CASTING =
            SynchedEntityData.defineId(VampillerEntity.class, EntityDataSerializers.BOOLEAN);

    private Map<CooldownInstance, String> cooldownSkill=new HashMap<>();
    private Map<Integer,SkillAbstract> skills=new HashMap<>();

    private int attackTimer=0;
    private int castingTimer=0;
    private int spellId=-1;


    public VampillerEntity(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 40.D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D).build();

    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> {
            if(this.isAttacking()) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("vampiller_drakul.bite"));
            }else if(this.isCasting()){
                state.getController().setAnimation(RawAnimation.begin().thenLoop("vampiller_drakul.blood_orb"));
            }else {
                state.getController().setAnimationSpeed(this.isAggressive() ? 2.0D : 1.0D);
                state.getController().setAnimation(RawAnimation.begin().thenLoop("vampiller_drakul.idle"));
            }
            return PlayState.CONTINUE;
        }));
        controllers.add(new AnimationController<>(this, "controller_legs", 0, state -> {
            boolean isMove= !(state.getLimbSwingAmount() > -0.15F && state.getLimbSwingAmount() < 0.15F);
            if(isMove) {
                state.getController().setAnimationSpeed(this.isAggressive() ? 2.0D : 1.0D);
                state.getController().setAnimation(RawAnimation.begin().thenLoop("vampiller_drakul.legs1"));
            }else {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("vampiller_drakul.legs2"));
            }
            return PlayState.CONTINUE;
        }));
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
        this.entityData.set(FORM_BAT,pIsCasting);
    }
    public boolean isCasting(){
        return this.entityData.get(FORM_BAT);
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING,false);
        this.entityData.define(FORM_BAT,false);
        this.entityData.define(IS_CASTING,false);
    }

    @Override
    public void handleEntityEvent(byte p_21375_) {
        if(p_21375_==4){
            this.setAttacking(true);
        }
        super.handleEntityEvent(p_21375_);

    }

    @Override
    public void tick() {
        super.tick();
        if(!this.cooldownSkill.isEmpty()){
            this.cooldownSkill.forEach((c,s)->c.decrementBy(1));
        }
        if(this.isCasting() && this.getTarget()!=null){
            this.castingTimer--;
            if(this.castingTimer==0){
                LivingEntity target=this.getTarget();
                SkillAbstract skillAbstract=this.skills.get(this.spellId);
                this.setIsCasting(false);
                if(skillAbstract.equals(SGSkillAbstract.BLOOD_ORB)){
                    BloodOrbProjetile orb =new BloodOrbProjetile(this.level(),this,0);
                    orb.setPos(this.getEyePosition());
                    orb.setChargedLevel(4);
                    orb.shoot(target.getX()-this.getX(),target.getY()-this.getY(),target.getZ()-this.getZ(),1.0F,1.0F);
                    this.level().addFreshEntity(orb);
                    this.cooldownSkill.put(new CooldownInstance(200),skillAbstract.name);
                }
            }

        }else if (this.getTarget()==null){
            this.setIsCasting(false);
            this.castingTimer=0;
        }

        if(this.isAttacking()){
            if(this.attackTimer--<=0){
                this.setAttacking(false);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);
        var listTag = new ListTag();
        this.cooldownSkill.forEach((cooldown, spellId) -> {
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
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);
        if(p_21450_.contains("cooldowns")){
            ListTag listTag=p_21450_.getList("cooldowns",10);
            if (listTag != null) {
                listTag.forEach(tag -> {
                    CompoundTag t = (CompoundTag) tag;
                    String powerId = t.getString("name");
                    this.cooldownSkill.put(new CooldownInstance(t),powerId);
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
        this.goalSelector.addGoal(2,new AttackGoal(this,1.0D,true));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(4,new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new FloatGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, true));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        super.registerGoals();
    }



    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
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
            return super.canUse() && !this.goalOwner.isVehicle();
        }


        @Override
        protected void checkAndPerformAttack(@NotNull LivingEntity entity, double distance) {
            double d0 = this.getAttackReachSqr(entity) + 5.0D;
            if (distance <= d0 && this.goalOwner.attackTimer <= 0) {
                this.resetAttackCooldown();
                this.goalOwner.level().playSound(null,this.goalOwner, SGSounds.VAMPILLER_HURT.get(), SoundSource.HOSTILE,1.5F,1.0F);
                this.goalOwner.navigation.stop();
                this.goalOwner.getLookControl().setLookAt(entity,30,30);
                this.goalOwner.setYBodyRot(this.goalOwner.getYHeadRot());
            }else if(distance<16.0F && !this.goalOwner.cooldownSkill.containsValue(this.goalOwner.skills.get(0).name)){
                int id=0;
                SkillAbstract skillAbstract=this.goalOwner.skills.get(id);
                this.goalOwner.setIsCasting(true);
                this.goalOwner.spellId=id;
                this.goalOwner.castingTimer=skillAbstract.castingDuration;
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
}
