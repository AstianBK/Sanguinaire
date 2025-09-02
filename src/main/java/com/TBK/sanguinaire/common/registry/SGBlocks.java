package com.TBK.sanguinaire.common.registry;

import com.TBK.sanguinaire.Sanguinaire;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SGBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Sanguinaire.MODID);


    public static final RegistryObject<Block> DREADWOOD_LOG = registerBlock("dreadwood_log",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD).strength(1.0F).sound(SoundType.WOOD).ignitedByLava().requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> DREADWOOD_STRIPPED_LOG = registerBlock("dreadwood_stripped_log",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD).strength(1.0F).sound(SoundType.WOOD).ignitedByLava().requiresCorrectToolForDrops()));








    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block ) {
        return SGItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register (IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}

