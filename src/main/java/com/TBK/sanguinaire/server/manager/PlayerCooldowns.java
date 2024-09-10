package com.TBK.sanguinaire.server.manager;

import com.TBK.sanguinaire.server.network.PacketHandler;
import com.TBK.sanguinaire.server.network.messager.PacketSyncCooldown;
import com.TBK.sanguinaire.server.skill.SkillAbstract;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public class PlayerCooldowns {
    private final Map<String, CooldownInstance> powerCooldowns;

    private int tickBuffer = 0;

    public PlayerCooldowns() {
        this.powerCooldowns = Maps.newHashMap();
    }

    public void setTickBuffer(int tickBuffer) {
        this.tickBuffer = tickBuffer;
    }

    public void tick(int actualTicks) {
        var powers = powerCooldowns.entrySet().stream().filter(x -> decrementCooldown(x.getValue(), actualTicks)).toList();
        powers.forEach(power -> powerCooldowns.remove(power.getKey()));
    }

    public boolean hasCooldownsActive() {
        return !powerCooldowns.isEmpty();
    }

    public Map<String, CooldownInstance> getSkillAbstractCooldowns() {
        return powerCooldowns;
    }

    public boolean removeCooldown(String powerId) {
        return powerCooldowns.remove(powerId) != null;
    }

    public void clearCooldowns() {
        powerCooldowns.clear();
    }

    public void addCooldown(SkillAbstract power, int durationTicks) {
        powerCooldowns.put(power.name, new CooldownInstance(durationTicks));
    }

    public void addCooldown(SkillAbstract power, int durationTicks, int remaining) {
        powerCooldowns.put(power.name, new CooldownInstance(durationTicks, remaining));
    }

    public void addCooldown(String powerID, int durationTicks) {
        powerCooldowns.put(powerID, new CooldownInstance(durationTicks));
    }

    public void addCooldown(String powerID, int durationTicks, int remaining) {
        powerCooldowns.put(powerID, new CooldownInstance(durationTicks, remaining));
    }

    public boolean isOnCooldown(SkillAbstract power) {
        return powerCooldowns.containsKey(power.name);
    }

    public float getCooldownPercent(SkillAbstract power) {
        return powerCooldowns.getOrDefault(power.name, new CooldownInstance(0)).getCooldownPercent();
    }

    public boolean decrementCooldown(CooldownInstance c, int amount) {
        c.decrementBy(amount);
        return c.getCooldownRemaining() <= tickBuffer;
    }

    public void syncToPlayer(ServerPlayer serverPlayer) {
        PacketHandler.sendToPlayer(new PacketSyncCooldown(this.powerCooldowns), serverPlayer);
    }

    public ListTag saveNBTData() {
        var listTag = new ListTag();
        powerCooldowns.forEach((powerId, cooldown) -> {
            if (cooldown.getCooldownRemaining() > 0) {
                CompoundTag ct = new CompoundTag();
                ct.putString("name", powerId);
                ct.putInt("cooldown", cooldown.getPowerCooldown());
                ct.putInt("cooldown_remaining", cooldown.getCooldownRemaining());
                listTag.add(ct);
            }
        });
        return listTag;
    }

    public void loadNBTData(ListTag listTag) {
        if (listTag != null) {
            listTag.forEach(tag -> {
                CompoundTag t = (CompoundTag) tag;
                String powerId = t.getString("name");
                int powerCooldown = t.getInt("cooldown");
                int cooldownRemaining = t.getInt("cooldown_remaining");
                powerCooldowns.put(powerId, new CooldownInstance(powerCooldown, cooldownRemaining));
            });
        }
    }
}
