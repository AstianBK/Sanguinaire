package com.TBK.sanguinaire.common.api;

import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public interface IBiterEntity extends INBTSerializable<CompoundTag> {
    boolean canBiter();
    void setCurrentEntity(LivingEntity currentEntity);
    LivingEntity getCurrentEntity();
    int getMaxBlood();
    int getBlood();
    void setBlood(int blood);
    void onBite(VampirePlayerCapability target);
    void tick(Player player);
}
