package com.TBK.sanguinaire.common.registry;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class SGTiers {
    public static final ForgeTier VAMPIRE_RELIC = new ForgeTier(5, 200, 0.0F,
            -1F, 0, BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.of(SGItems.ANCIENT_BLOOD.get()));


}
