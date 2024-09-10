package com.TBK.sanguinaire.server;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.common.api.Clan;
import com.TBK.sanguinaire.server.capability.BiterEntityCap;
import com.TBK.sanguinaire.server.capability.SGCapability;
import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import com.TBK.sanguinaire.server.manager.RegenerationInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class ModBusEvent {
    @SubscribeEvent
    public static void onJoinGame(EntityJoinLevelEvent event) {
        if(event.getEntity() instanceof Player){
            SkillPlayerCapability cap = SGCapability.getEntityCap(event.getEntity(), SkillPlayerCapability.class);
            if(cap!=null){
                cap.onJoinGame((Player) event.getEntity(),event);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event){
        ItemStack stack=event.getEntity().getItemInHand(event.getHand());
        if(stack.is(Items.STICK) && !event.getLevel().isClientSide){
            VampirePlayerCapability cap = SGCapability.getEntityVam(event.getEntity(), VampirePlayerCapability.class);
            if(cap!=null){
                boolean isVampire=cap.isVampire();
                event.getEntity().sendSystemMessage(Component.nullToEmpty(cap.isVampire() ?  "Te convertiste humano beta" :"Te convertiste en Vampiro Gigachad" ));
                cap.convert(isVampire);
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

            if (oldCap == null && oldVamp!=null) {
                SkillPlayerCapability.SkillPlayerProvider prov = new SkillPlayerCapability.SkillPlayerProvider();
                SkillPlayerCapability cap=prov.getCapability(SGCapability.POWER_CAPABILITY).orElse(null);
                cap.init(player);
                event.addCapability(new ResourceLocation(Sanguinaire.MODID, "power_cap"), prov);

            }
        }else if(event.getObject() instanceof LivingEntity living){
            BiterEntityCap oldVamp = SGCapability.getEntityEntity(event.getObject(), BiterEntityCap.class);

            if (oldVamp == null) {
                BiterEntityCap.BiterEntityPlayerProvider prov = new BiterEntityCap.BiterEntityPlayerProvider();
                BiterEntityCap cap=prov.getCapability(SGCapability.ENTITY_CAPABILITY).orElse(null);
                cap.setCurrentEntity(living);
                cap.setBlood(cap.getMaxBlood());
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
    public static void hurtEntity(LivingHurtEvent event){
        LivingEntity target=event.getEntity();
        DamageSource source=event.getSource();
        if(target instanceof Player player){
            VampirePlayerCapability cap=VampirePlayerCapability.get(player);
            if(cap!=null){
                if(cap.isVampire()){
                    if(source.is(DamageTypeTags.IS_FALL) && source.getEntity()==null){
                        if(!cap.legsLess()){
                            loseLegs(cap,player);
                        }else if(!cap.bodyLess()){
                            loseBody(cap,player);
                        }else {
                            cap.losePart("head",new RegenerationInstance(50),player);
                        }
                    }else if(source.is(DamageTypeTags.IS_EXPLOSION)){
                        cap.losePart("head",new RegenerationInstance(200),player);
                        loseBody(cap,player);
                        event.setCanceled(true);
                        player.setHealth(1.0F);
                    }else if(source.getEntity()!=null){
                        cap.losePart("right_arm",new RegenerationInstance(50),player);
                    }
                }
            }
        }
    }

    public static void loseLegs(VampirePlayerCapability cap,Player player){
        cap.losePart("right_leg",new RegenerationInstance(50),player);
        cap.losePart("left_leg",new RegenerationInstance(50),player);
    }
    public static void loseBody(VampirePlayerCapability cap,Player player){
        cap.losePart("body",new RegenerationInstance(100),player);
        cap.losePart("right_arm",new RegenerationInstance(50),player);
        cap.losePart("left_arm",new RegenerationInstance(50),player);
        loseLegs(cap,player);
    }
}
