package com.TBK.sanguinaire.server.network.messager;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.manager.DurationInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketActiveEffect {
    private final DurationInstance recastInstance;

    public PacketActiveEffect(DurationInstance recastInstance) {
        this.recastInstance = recastInstance;
    }

    public PacketActiveEffect(FriendlyByteBuf buf) {
        recastInstance = new DurationInstance();
        recastInstance.readFromBuffer(buf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        if (recastInstance != null) {
            recastInstance.writeToBuffer(buf);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
        });
        return true;
    }
}
