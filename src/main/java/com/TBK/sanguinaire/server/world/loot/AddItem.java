package com.TBK.sanguinaire.server.world.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;


public class AddItem extends LootModifier {
    public static final Supplier<Codec<AddItem>> CODEC_SUPPLIER = Suppliers.memoize(()->RecordCodecBuilder.create((inst)-> codecStart(inst).and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m->m.item))
            .and(Codec.FLOAT.fieldOf("probability").forGetter(e->e.probability)).apply(inst,AddItem::new)));
    public final Item item;
    public final float probability;

    protected AddItem(LootItemCondition[] conditions, Item item,float probability){
        super(conditions);
        this.item=item;
        this.probability=probability;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if(context.getRandom().nextFloat()<this.probability){
            generatedLoot.add(new ItemStack(this.item));
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC_SUPPLIER.get();
    }
}
