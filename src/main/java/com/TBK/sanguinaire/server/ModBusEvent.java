package com.TBK.sanguinaire.server;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.common.api.IVampirePlayer;
import com.TBK.sanguinaire.common.registry.SGAttribute;
import com.TBK.sanguinaire.common.registry.SGEffect;
import com.TBK.sanguinaire.common.registry.SGItems;
import com.TBK.sanguinaire.common.registry.SGSounds;
import com.TBK.sanguinaire.server.capability.BiterEntityCap;
import com.TBK.sanguinaire.server.capability.SGCapability;
import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import com.TBK.sanguinaire.server.entity.projetile.BloodOrbProjetile;
import com.TBK.sanguinaire.server.manager.RegenerationInstance;
import com.TBK.sanguinaire.server.network.PacketHandler;
import com.TBK.sanguinaire.server.network.messager.PacketSyncBlood;
import com.TBK.sanguinaire.server.network.messager.PacketSyncBloodEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber()
public class ModBusEvent {
    @SubscribeEvent
    public static void onJoinGame(EntityJoinLevelEvent event) {
        if(event.getEntity() instanceof Player){
            SkillPlayerCapability cap = SGCapability.getEntityCap(event.getEntity(), SkillPlayerCapability.class);
            if(cap!=null){
                cap.onJoinGame((Player) event.getEntity(),event);
            }
            VampirePlayerCapability cap1 = SGCapability.getEntityVam(event.getEntity(), VampirePlayerCapability.class);
            if(cap1!=null){
                cap1.syncCap((Player) event.getEntity());
            }
        }else if (event.getEntity() instanceof LivingEntity){
            BiterEntityCap cap1 = SGCapability.getEntityEntity(event.getEntity(), BiterEntityCap.class);
            if(cap1!=null){
                if(!event.getEntity().level().isClientSide){
                    PacketHandler.sendToAllTracking(new PacketSyncBloodEntity(cap1.blood, event.getEntity()), (LivingEntity) event.getEntity());
                }
            }
        }
    }
    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event){
        ItemStack stack=event.getEntity().getItemInHand(event.getHand());
        if(stack.is(SGItems.CRIMSON_MIRROR.get()) && !event.getEntity().isShiftKeyDown() && !event.getLevel().isClientSide){
            VampirePlayerCapability cap = SGCapability.getEntityVam(event.getEntity(), VampirePlayerCapability.class);
            if(cap!=null){
                boolean isVampire=cap.isVampire();
                event.getEntity().sendSystemMessage(Component.nullToEmpty(cap.isVampire() ?  "Te convertiste en humano OmegaGigaChad" :"Te convertiste en Vampiro Cuck" ));
                cap.convert(isVampire);
            }
        }else if(stack.is(Items.STICK) && event.getEntity().isShiftKeyDown()){
            VampirePlayerCapability cap = SGCapability.getEntityVam(event.getEntity(), VampirePlayerCapability.class);
            if(cap!=null){
                cap.age++;
                event.getEntity().sendSystemMessage(Component.nullToEmpty(String.valueOf(cap.age)));
            }
        }

    }

    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event){
        if(event.getEntity() instanceof Player){
            VampirePlayerCapability vampismo = SGCapability.getEntityVam(event.getEntity(), VampirePlayerCapability.class);
            SkillPlayerCapability cap = SGCapability.getEntityCap(event.getEntity(), SkillPlayerCapability.class);
            if(cap!=null && event.getEntity().isAlive()){
                cap.tick((Player) event.getEntity());
            }
            if(vampismo!=null && event.getEntity().isAlive() && vampismo.isVampire()){
                vampismo.tick((Player) event.getEntity());
            }
        }else {
            BiterEntityCap cap=SGCapability.getEntityEntity(event.getEntity(), BiterEntityCap.class);
            if(cap!=null && event.getEntity().isAlive()){
                cap.tick(event.getEntity());
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player player){
            VampirePlayerCapability oldVamp = SGCapability.getEntityVam(event.getObject(), VampirePlayerCapability.class);
            SkillPlayerCapability oldCap = SGCapability.getEntityCap(event.getObject(), SkillPlayerCapability.class);

            if (oldVamp == null) {
                VampirePlayerCapability.VampirePlayerProvider prov = new VampirePlayerCapability.VampirePlayerProvider();
                VampirePlayerCapability cap=prov.getCapability(SGCapability.VAMPIRE_CAPABILITY).orElse(null);
                cap.init(player);
                event.addCapability(new ResourceLocation(Sanguinaire.MODID, "vampire_cap"), prov);
            }

            if (oldCap == null) {
                SkillPlayerCapability.SkillPlayerProvider prov = new SkillPlayerCapability.SkillPlayerProvider();
                SkillPlayerCapability cap=prov.getCapability(SGCapability.POWER_CAPABILITY).orElse(null);
                cap.init(player);
                event.addCapability(new ResourceLocation(Sanguinaire.MODID, "skill_cap"), prov);
            }
        }else if(event.getObject() instanceof LivingEntity living){
            BiterEntityCap oldVamp = SGCapability.getEntityEntity(event.getObject(), BiterEntityCap.class);

            if (oldVamp == null) {
                BiterEntityCap.BiterEntityPlayerProvider prov = new BiterEntityCap.BiterEntityPlayerProvider();
                BiterEntityCap cap=prov.getCapability(SGCapability.ENTITY_CAPABILITY).orElse(null);
                cap.init(living);
                event.addCapability(new ResourceLocation(Sanguinaire.MODID, "bite_cap"), prov);
            }
        }

        /*AnimationPlayerCapability oldPatch=SGCapability.getEntityPatch(event.getObject(), AnimationPlayerCapability.class);
        if (oldPatch==null){
            AnimationPlayerCapability.AnimationPlayerProvider prov = new AnimationPlayerCapability.AnimationPlayerProvider();
            AnimationPlayerCapability getSkillCap=prov.getCapability(SGCapability.ANIMATION_CAPABILITY).orElse(null);
            if(event.getObject() instanceof Player player){
                getSkillCap.init(player);
                event.addCapability(new ResourceLocation(Sanguinaire.MODID, "animation_patch"), prov);
            }
        }*/
    }
    @SubscribeEvent
    public static void clonePlayer(PlayerEvent.Clone event){
        Player player=event.getOriginal();
        Player newPlayer=event.getEntity();
        player.reviveCaps();
        VampirePlayerCapability cap=VampirePlayerCapability.get(player);
        VampirePlayerCapability newCap=VampirePlayerCapability.get(newPlayer);
        newCap.clone(cap,player,newPlayer);
        player.invalidateCaps();
    }
    @SubscribeEvent
    public static void deathEntity(LivingDeathEvent event){
        LivingEntity target=event.getEntity();
        if(target instanceof Player player){
            VampirePlayerCapability cap=VampirePlayerCapability.get(player);
            if(cap!=null && cap.isVampire() && cap.canRevive()){
                if (!cap.noMoreLimbs()){
                    player.setHealth(1.0F);
                    cap.loseBlood(4);
                    player.setInvulnerable(true);
                    event.setCanceled(true);
                    loseBody(cap,player);
                    player.level().playSound(player,player,SGSounds.VAMPIRE_RESURRECT.get(), SoundSource.PLAYERS,1.0F,1.0F);
                }
            }
        }
        if (event.getSource().getDirectEntity() instanceof BloodOrbProjetile orb){
            if(!orb.level().isClientSide){
                LivingEntity collateral=target.level().getNearestEntity(LivingEntity.class, TargetingConditions.DEFAULT,null,target.getX(),target.getY(),target.getZ(),target.getBoundingBox().inflate(10.0D));
                if(collateral!=null){
                    BloodOrbProjetile projetile=new BloodOrbProjetile(collateral.level(),((LivingEntity)orb.getOwner()),Math.max(orb.getPowerLevel()-1,0));
                    projetile.setPos(target.getEyePosition());
                    Vec3 delta=collateral.getEyePosition().subtract(target.getEyePosition());
                    projetile.shoot(delta.x,delta.y,delta.z,1.0F,1.0F);
                    collateral.level().addFreshEntity(projetile);
                }
            }
        }
    }



    public static void loseBody(VampirePlayerCapability cap,Player player){
        int timer=cap.getRegTimer();
        cap.losePart("head",new RegenerationInstance(timer),player);
        cap.losePart("body",new RegenerationInstance(timer),player);
        cap.losePart("right_arm",new RegenerationInstance(timer),player);
        cap.losePart("left_arm",new RegenerationInstance(timer),player);
        cap.losePart("right_leg",new RegenerationInstance(timer),player);
        cap.losePart("left_leg",new RegenerationInstance(timer),player);
    }

    @SubscribeEvent
    public static void healPlayer(LivingHealEvent event){
        if(event.getEntity() instanceof Player player){
            VampirePlayerCapability cap=VampirePlayerCapability.get(player);
            if(cap!=null && cap.isVampire()){
                event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public static void hurtPlayer(LivingHurtEvent event){
        LivingEntity living=event.getEntity();
        if(living.hasEffect(SGEffect.BLEEDING.get())){
            if(event.getSource().getEntity() instanceof IVampirePlayer vampirePlayer){
                vampirePlayer.loseBlood(1);
            }
        }
    }
}
