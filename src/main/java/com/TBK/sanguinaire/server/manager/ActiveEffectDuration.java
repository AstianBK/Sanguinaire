package com.TBK.sanguinaire.server.manager;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.network.PacketHandler;
import com.TBK.sanguinaire.server.network.messager.PacketActiveEffect;
import com.TBK.sanguinaire.server.network.messager.PacketRemoveActiveEffect;
import com.TBK.sanguinaire.server.network.messager.PacketSyncDurationEffect;
import com.TBK.sanguinaire.server.skill.SkillAbstract;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ActiveEffectDuration {
    private static final DurationInstance EMPTY = new DurationInstance(SkillAbstract.NONE.name, 0, 0, 0);
    private final Map<String, DurationInstance> recastLookup;

    private final ServerPlayer serverPlayer;

    public ActiveEffectDuration() {
        this.recastLookup = Maps.newHashMap();
        this.serverPlayer = null;
    }

    public ActiveEffectDuration(ServerPlayer serverPlayer) {
        this.recastLookup = Maps.newHashMap();
        this.serverPlayer = serverPlayer;
    }

    @OnlyIn(Dist.CLIENT)
    public ActiveEffectDuration(Map<String, DurationInstance> recastLookup) {
        this.recastLookup = recastLookup;
        this.serverPlayer = null;
    }

    public boolean addDuration(DurationInstance recastInstance, SkillPlayerCapability powerCap) {
        var existingDurationInstance = recastLookup.get(recastInstance.powerId);

        if (!isDurationActive(existingDurationInstance)) {
            powerCap.getCooldowns().removeCooldown(recastInstance.powerId);
            recastLookup.put(recastInstance.powerId, recastInstance);
            syncToPlayer(recastInstance);
            return true;
        }

        return false;
    }

    public boolean isDurationActive(DurationInstance recastInstance) {
        return recastInstance != null && recastInstance.remainingDuration > 0 && recastInstance.remainingTicks > 0;
    }

    @OnlyIn(Dist.CLIENT)
    public void removeDuration(String powerId) {
        recastLookup.remove(powerId);
    }

    @OnlyIn(Dist.CLIENT)
    public void forceAddDuration(DurationInstance recastInstance) {
        recastLookup.put(recastInstance.powerId, recastInstance);
    }

    @OnlyIn(Dist.CLIENT)
    public void tickDurations(SkillPlayerCapability skillPlayerCapability) {
        if (!recastLookup.isEmpty()) {
            recastLookup.values().stream().toList().forEach(x ->{
                x.remainingTicks--;
                if(x.remainingTicks<=0){
                    skillPlayerCapability.stopSkill(skillPlayerCapability.getHotBarSkill().getForName(x.getSpellId()));
                }
            });
        }
    }

    public boolean hasDurationsActive() {
        return !recastLookup.isEmpty();
    }

    public boolean hasDurationForSkill(SkillAbstract power) {
        return isDurationActive(recastLookup.get(power.name));
    }

    public boolean hasDurationForSkill(String powerId) {
        return isDurationActive(recastLookup.get(powerId));
    }

    public int getRemainingDurationsForSkill(String powerId) {
        var recastInstance = recastLookup.getOrDefault(powerId, EMPTY);

        if (isDurationActive(recastInstance)) {
            return recastInstance.remainingDuration;
        }

        return 0;
    }

    public int getRemainingDurationsForSkill(SkillAbstract power) {
        return getRemainingDurationsForSkill(power.name);
    }

    public DurationInstance getDurationInstance(String powerId) {
        return recastLookup.get(powerId);
    }

    public List<DurationInstance> getAllDurations() {
        return recastLookup.values().stream().toList();
    }

    public List<DurationInstance> getActiveDurations() {
        return recastLookup.values().stream().filter(this::isDurationActive).toList();
    }

    public void decrementDurationCount(String powerId) {
        var recastInstance = recastLookup.get(powerId);

        if (isDurationActive(recastInstance)) {
            recastInstance.remainingDuration--;

            if (recastInstance.remainingDuration > 0) {
                recastInstance.remainingTicks = recastInstance.ticksToLive;
                syncToPlayer(recastInstance);
            } else {
                removeDuration(recastInstance, DurationResult.TIMEOUT);
            }
        } else if (recastInstance != null) {
            removeDuration(recastInstance, DurationResult.TIMEOUT);
        }
    }

    public void decrementDurationCount(SkillAbstract power) {
        decrementDurationCount(power.name);
    }

    public void syncAllToPlayer() {
        if (serverPlayer != null) {
            PacketHandler.sendToPlayer(new PacketSyncDurationEffect(recastLookup), serverPlayer);
        }
    }

    public void syncToPlayer(DurationInstance recastInstance) {
        if (serverPlayer != null) {
            PacketHandler.sendToPlayer(new PacketActiveEffect(recastInstance), serverPlayer);
        }
    }

    public void syncRemoveToPlayer(String powerId) {
        if (serverPlayer != null) {
            PacketHandler.sendToPlayer(new PacketRemoveActiveEffect(powerId,0), serverPlayer);
        }
    }

    private void removeDuration(DurationInstance recastInstance, DurationResult recastResult, boolean doSync) {
        recastLookup.remove(recastInstance.powerId);
        if (doSync) {
            syncRemoveToPlayer(recastInstance.powerId);
        }
    }

    public void removeDuration(DurationInstance recastInstance, DurationResult recastResult) {
        removeDuration(recastInstance, recastResult, true);
    }

    public void removeAll(DurationResult recastResult) {
        recastLookup.values().stream().toList().forEach(recastInstance -> removeDuration(recastInstance, recastResult, false));
        syncAllToPlayer();
    }

    public ListTag saveNBTData() {
        var listTag = new ListTag();
        recastLookup.values().stream().filter(this::isDurationActive).forEach(recastInstance -> {
            if (recastInstance.remainingDuration > 0 && recastInstance.remainingTicks > 0) {
                listTag.add(recastInstance.serializeNBT());
            }
        });
        return listTag;
    }

    public void loadNBTData(ListTag listTag) {
        if (listTag != null) {
            listTag.forEach(tag -> {
                var recastInstance = new DurationInstance();
                recastInstance.deserializeNBT((CompoundTag) tag);
                if (recastInstance.remainingDuration > 0 && recastInstance.remainingTicks > 0) {
                    recastLookup.put(recastInstance.powerId, recastInstance);
                }
            });
        }
    }

    public void tick(int actualTicks, SkillPlayerCapability powerPlayerCapability) {
        if (serverPlayer != null && serverPlayer.level().getGameTime() % actualTicks == 0) {
            recastLookup.values()
                    .stream()
                    .filter(r -> {
                        r.remainingTicks -= actualTicks;
                        return r.remainingTicks <= 0;
                    })
                    .toList()
                    .forEach(recastInstance ->{
                        removeDuration(recastInstance, DurationResult.TIMEOUT);
                        powerPlayerCapability.powers.getSkills().forEach(e->{
                            if(Objects.equals(e.getSkillAbstract().name, recastInstance.getSpellId())){
                                powerPlayerCapability.stopSkill(e.getSkillAbstract());
                            }
                        });
                    });
        }
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();

        recastLookup.values().forEach(recastInstance -> {
            sb.append(recastInstance.toString());
            sb.append("\n");
        });

        return sb.toString();
    }
}
