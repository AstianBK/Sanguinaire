package com.TBK.sanguinaire.common.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public interface IAnimationPlayer extends INBTSerializable<CompoundTag> {
    Player getPlayer();
    void setPlayer(Player player);
    void init(Player player);
}
