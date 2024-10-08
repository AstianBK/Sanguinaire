package com.TBK.sanguinaire.common.registry;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.common.item.AncientBloodItem;
import com.TBK.sanguinaire.common.item.GobletItem;
import com.TBK.sanguinaire.common.item.VampireHeartItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SGItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Sanguinaire.MODID);


    public static final RegistryObject<Item> VAMPIRE_HEART = ITEMS.register("vampire_heart",
            ()-> new VampireHeartItem(new Item.Properties().food(new FoodProperties.Builder().alwaysEat().nutrition(0).fast().build()).stacksTo(64)));

    public static final RegistryObject<Item> ANCIENT_BLOOD = ITEMS.register("ancient_blood",
            ()-> new AncientBloodItem(new Item.Properties().rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().alwaysEat().nutrition(0).fast().build()).stacksTo(1)));

    public static final RegistryObject<Item> GOLD_GOBLET = ITEMS.register("gold_goblet",
            ()-> new GobletItem(new Item.Properties().stacksTo(1).durability(10).defaultDurability(1)));

    public static final RegistryObject<Item> CRIMSON_MIRROR = ITEMS.register("crimson_mirror",
            ()-> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> VAMPILLER_SPAWN_EGG = ITEMS.register("vampiller_spawn_egg",
            () -> new ForgeSpawnEggItem(SGEntityType.VAMPILLER,0xf7fafa, 0xc6e2f5,
                    new Item.Properties()));

    public static final RegistryObject<Item> MUSIC_DISC_WASTED_BLOOD = ITEMS.register("music_disc_vamp",()->new RecordItem(14, SoundEvents.MUSIC_DISC_RELIC, (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), 218));
}