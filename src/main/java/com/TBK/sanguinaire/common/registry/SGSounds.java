package com.TBK.sanguinaire.common.registry;

import com.TBK.sanguinaire.Sanguinaire;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SGSounds {
        public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
                DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Sanguinaire.MODID);

        //SKILLS & INTERACTIONS

        public static final RegistryObject<SoundEvent> VAMPIRE_RESURRECT =
                registerSoundEvent("vampire_resurrect");

        public static final RegistryObject<SoundEvent> BLOOD_DRINK =
                registerSoundEvent("blood_drink");

        public static final RegistryObject<SoundEvent> BLOOD_SLASH =
            registerSoundEvent("blood_slash");

        public static final RegistryObject<SoundEvent> BLOOD_SLASH_HIT =
                registerSoundEvent("blood_slash_hit");

        //ENTITY

        public static final RegistryObject<SoundEvent> VAMPILLER_HURT =
                registerSoundEvent("vampiller_hurt");

        //MUSIC

        public static final RegistryObject<SoundEvent> WASTED_BLOOD_IVAN_DUCH =
                registerSoundEvent("wasted_blood_ivan_duch");



        public static RegistryObject<SoundEvent> registerSoundEvent(String name){
            return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Sanguinaire.MODID, name)));
        }

        public static void register(IEventBus eventBus){
            SOUND_EVENTS.register(eventBus);
        }
}
