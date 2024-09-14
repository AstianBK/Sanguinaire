package com.TBK.sanguinaire.common.registry;

import com.TBK.sanguinaire.Sanguinaire;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SGItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Sanguinaire.MODID);

    public static final RegistryObject<Item> MUSIC_DISC_WASTED_BLOOD = ITEMS.register("music_disc_wasted_blood",()->new RecordItem(14, SoundEvents.MUSIC_DISC_RELIC, (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), 218));
}