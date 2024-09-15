package com.TBK.sanguinaire.common.api;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public interface IVampirePlayer extends INBTSerializable<CompoundTag> {
    boolean isVampire();
    void setIsVampire(boolean bol);
    void setPlayer(Player player);
    Player getPlayer();
    int getGeneration();
    Clan getClan();
    void setGeneration(int generation);
    void setClan(Clan clan);
    void bite(Player player, Entity target);
    SkillPlayerCapability getSkillCap(Player player);
    void tick(Player player);
}
