package com.TBK.sanguinaire.server.capability;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class SGCapability {
    public static final Capability<BiterEntityCap> ENTITY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<VampirePlayerCapability> VAMPIRE_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<SkillPlayerCapability> POWER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    //public static final Capability<AnimationPlayerCapability> ANIMATION_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});


    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(SkillPlayerCapability.class);
        event.register(VampirePlayerCapability.class);
        event.register(BiterEntityCap.class);
        //event.register(AnimationPlayerCapability.class);
    }

    @SuppressWarnings("unchecked")
    public static <T extends SkillPlayerCapability> T getEntityCap(Entity entity, Class<T> type) {
        if (entity != null) {
            SkillPlayerCapability entitypatch = entity.getCapability(SGCapability.POWER_CAPABILITY).orElse(null);

            if (entitypatch != null && type.isAssignableFrom(entitypatch.getClass())) {
                return (T)entitypatch;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends VampirePlayerCapability> T getEntityVam(Entity entity, Class<T> type) {
        if (entity != null) {
            VampirePlayerCapability entitypatch = entity.getCapability(SGCapability.VAMPIRE_CAPABILITY).orElse(null);

            if (entitypatch != null && type.isAssignableFrom(entitypatch.getClass())) {
                return (T)entitypatch;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends BiterEntityCap> T getEntityEntity(Entity entity, Class<T> type) {
        if (entity != null) {
            BiterEntityCap entitypatch = entity.getCapability(SGCapability.ENTITY_CAPABILITY).orElse(null);

            if (entitypatch != null && type.isAssignableFrom(entitypatch.getClass())) {
                return (T)entitypatch;
            }
        }

        return null;
    }

    /*@SuppressWarnings("unchecked")
    public static <T extends AnimationPlayerCapability> T getEntityPatch(Entity entity, Class<T> type) {
        if (entity != null) {
            AnimationPlayerCapability entitypatch = entity.getCapability(PwCapability.ANIMATION_CAPABILITY).orElse(null);

            if (entitypatch != null && type.isAssignableFrom(entitypatch.getClass())) {
                return (T)entitypatch;
            }
        }

        return null;
    }*/
}
