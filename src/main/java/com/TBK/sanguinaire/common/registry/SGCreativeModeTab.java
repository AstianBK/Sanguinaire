package com.TBK.sanguinaire.common.registry;

import com.TBK.sanguinaire.Sanguinaire;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SGCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Sanguinaire.MODID);

    public static final RegistryObject<CreativeModeTab> SANGUINAIRE_TAB = TABS.register("bk_items",()-> CreativeModeTab.builder()
            .icon(()->new ItemStack(SGItems.MUSIC_DISC_WASTED_BLOOD.get()))
            .title(Component.translatable("itemGroup.sanguinarieTab"))
            .displayItems((s,a)-> {
                a.accept(SGItems.MUSIC_DISC_WASTED_BLOOD.get());
            })
            .build());
}
