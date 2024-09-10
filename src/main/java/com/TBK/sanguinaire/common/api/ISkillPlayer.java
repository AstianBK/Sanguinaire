package com.TBK.sanguinaire.common.api;

import com.TBK.sanguinaire.server.skill.SkillAbstract;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;

import java.util.Map;

public interface ISkillPlayer extends INBTSerializable<CompoundTag> {
    Player getPlayer();
    void setPlayer(Player player);
    SkillAbstract getSelectSkill();
    SkillAbstract getSkillForHotBar(int pos);
    int getCooldownSkill();
    int getCastingTimer();
    int getStartTime();
    boolean lastUsingSkill();
    SkillAbstract getLastUsingSkill();
    void setLastUsingSkill(SkillAbstract power);
    void tick(Player player);
    void onJoinGame(Player player, EntityJoinLevelEvent event);
    void handledSkill(Player player,SkillAbstract power);
    public void stopSkill(Player player,SkillAbstract power);
    void handledPassive(Player player,SkillAbstract power);
    boolean canUseSkill(SkillAbstract skillAbstract);
    Map<Integer,SkillAbstract> getPassives();
    SkillAbstract getHotBarSkill();
    void syncSkill(Player player);
    void upSkill();
    void downSkill();
    void swingHand(Player player);

}
