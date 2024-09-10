package com.TBK.sanguinaire.server.manager;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.INBTSerializable;

public class DurationInstance implements  INBTSerializable<CompoundTag> {
    protected String powerId;
    protected int powerLevel;
    protected int remainingDuration;
    protected int totalRecasts;
    protected int ticksToLive;
    protected int remainingTicks;

    public DurationInstance() {
    }

    public DurationInstance(String powerId, int powerLevel, int totalRecasts, int ticksToLive) {
        this.powerId = powerId;
        this.powerLevel = powerLevel;
        this.remainingDuration = totalRecasts - 1;
        this.totalRecasts = totalRecasts;
        this.ticksToLive = ticksToLive;
        this.remainingTicks = ticksToLive;
    }

    public String getSpellId() {
        return powerId;
    }

    public int getSpellLevel() {
        return powerLevel;
    }

    public int getRemainingDuration() {
        return remainingDuration;
    }

    public int getTotalRecasts() {
        return totalRecasts;
    }

    public int getTicksToLive() {
        return ticksToLive;
    }

    public int getTicksRemaining() {
        return remainingTicks;
    }



    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeUtf(powerId);
        buffer.writeInt(powerLevel);
        buffer.writeInt(remainingDuration);
        buffer.writeInt(totalRecasts);
        buffer.writeInt(ticksToLive);
        buffer.writeInt(remainingTicks);

    }

    public void readFromBuffer(FriendlyByteBuf buffer) {
        powerId = buffer.readUtf();
        powerLevel = buffer.readInt();
        remainingDuration = buffer.readInt();
        totalRecasts = buffer.readInt();
        ticksToLive = buffer.readInt();
        remainingTicks = buffer.readInt();
    }

    @Override
    public CompoundTag serializeNBT() {
        var tag = new CompoundTag();
        tag.putString("powerId", powerId);
        tag.putInt("powerLevel", powerLevel);
        tag.putInt("remainingDuration", remainingDuration);
        tag.putInt("totalRecasts", totalRecasts);
        tag.putInt("ticksToLive", ticksToLive);
        tag.putInt("ticksRemaining", remainingTicks);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        powerId = compoundTag.getString("powerId");
        powerLevel = compoundTag.getInt("powerLevel");
        remainingDuration = compoundTag.getInt("remainingDuration");
        totalRecasts = compoundTag.getInt("totalRecasts");
        ticksToLive = compoundTag.getInt("ticksToLive");
        remainingTicks = compoundTag.getInt("ticksRemaining");

    }

    @Override
    public String toString() {
        return String.format("powerId:%s, powerLevel:%d, remainingDuration:%d, totalRecasts:%d, ticksToLive:%d, ticksRemaining:%d, castData:%s", powerId, powerLevel, remainingDuration, totalRecasts, ticksToLive, remainingTicks);
    }
}
