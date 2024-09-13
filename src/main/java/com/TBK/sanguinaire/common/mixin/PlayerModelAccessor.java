package com.TBK.sanguinaire.common.mixin;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(PlayerModel.class)
public interface PlayerModelAccessor<T extends LivingEntity> {

    @Accessor("slim")
    boolean isSlimModel();

}
