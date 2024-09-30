package com.TBK.sanguinaire.common.mixin;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;


@Mixin(StructureTemplatePool.class)
public interface StructureTemplatePoolAccessor {
    @Accessor("templates")
    @Final
    @Mutable
    ObjectArrayList<StructurePoolElement> get$Template();

    @Accessor("rawTemplates")
    @Final
    @Mutable
    List<Pair<StructurePoolElement, Integer>> get$RawTemplate();

    @Accessor("rawTemplates")
    @Final
    @Mutable
    void set$RawTemplate(List<Pair<StructurePoolElement, Integer>> list);
}
