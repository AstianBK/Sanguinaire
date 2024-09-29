package com.TBK.sanguinaire.common.registry;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.common.item.GobletItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class SGItemProperties {
    public static void register() {
        ItemProperties.register(SGItems.GOLD_GOBLET.get(), new ResourceLocation(Sanguinaire.MODID, "Blood"), (p_239425_0_, p_239425_1_, p_239425_2_, intIn) -> {
            return p_239425_2_ != null ? (float) GobletItem.getBlood(p_239425_0_) /2 : 0 ;
        });
    }
}
