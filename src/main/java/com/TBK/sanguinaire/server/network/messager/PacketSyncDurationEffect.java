package com.TBK.sanguinaire.server.network.messager;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.manager.ActiveEffectDuration;
import com.TBK.sanguinaire.server.manager.DurationInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class PacketSyncDurationEffect implements Packet<PacketListener> {
    private final Map<String, DurationInstance> recastLookup;

    public PacketSyncDurationEffect(Map<String, DurationInstance> recastLookup) {
        this.recastLookup = recastLookup;
    }

    public PacketSyncDurationEffect(FriendlyByteBuf buf) {
        this.recastLookup = buf.readMap(PacketSyncDurationEffect::readPowerID, PacketSyncDurationEffect::readDurationInstance);
    }

    public static String readPowerID(FriendlyByteBuf buffer) {
        return buffer.readUtf();
    }

    public static DurationInstance readDurationInstance(FriendlyByteBuf buffer) {
        var tmp = new DurationInstance();
        tmp.readFromBuffer(buffer);
        return tmp;
    }

    public static void writePowerId(FriendlyByteBuf buf, String powerId) {
        buf.writeUtf(powerId);
    }

    public static void writeDurationInstance(FriendlyByteBuf buf, DurationInstance recastInstance) {
        recastInstance.writeToBuffer(buf);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            SkillPlayerCapability cap = SkillPlayerCapability.get(mc.player);
            assert cap!=null;
            cap.setActiveEffectDuration(new ActiveEffectDuration(recastLookup));
        });
        return true;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeMap(recastLookup, PacketSyncDurationEffect::writePowerId, PacketSyncDurationEffect::writeDurationInstance);
    }

    @Override
    public void handle(PacketListener p_131342_) {

    }
}
