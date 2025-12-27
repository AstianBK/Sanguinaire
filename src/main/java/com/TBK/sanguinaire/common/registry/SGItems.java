package com.TBK.sanguinaire.common.registry;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.common.item.AncientBloodItem;
import com.TBK.sanguinaire.common.item.GobletItem;
import com.TBK.sanguinaire.common.item.VampireHeartItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SGItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Sanguinaire.MODID);

    //CONSUMABLE
    public static final RegistryObject<Item> VAMPIRE_HEART = ITEMS.register("vampire_heart",
            ()-> new VampireHeartItem(new Item.Properties().food(new FoodProperties.Builder().alwaysEat().nutrition(0).fast().build()).stacksTo(64)));

    public static final RegistryObject<Item> ANCIENT_BLOOD = ITEMS.register("ancient_blood",
            ()-> new AncientBloodItem(new Item.Properties().rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().alwaysEat().nutrition(0).fast().build()).stacksTo(1)));

    //ITEMS

    public static final RegistryObject<Item> GOLD_GOBLET = ITEMS.register("glass_goblet",
            ()-> new GobletItem(new Item.Properties().stacksTo(1).durability(7).defaultDurability(0)));

    //RELICS

    public static final RegistryObject<Item> CRIMSON_OATH = ITEMS.register("crimson_oath",
            ()->new SwordItem(SGTiers.VAMPIRE_RELIC,15,-3.0F,new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> SANGUINE_STONE = ITEMS.register("sanguine_stone",
            ()-> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> CRIMSON_MIRROR = ITEMS.register("blood_mirror",
            ()-> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    //ARMOR
    public static final RegistryObject<Item> MUSIC_DISC_WASTED_BLOOD = ITEMS.register("music_disc_vamp",()->new RecordItem(14, SoundEvents.MUSIC_DISC_RELIC, (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), 218));
}