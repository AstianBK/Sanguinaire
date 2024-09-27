package com.TBK.sanguinaire.server.network.messager;

import com.TBK.sanguinaire.common.registry.SGAttribute;
import com.TBK.sanguinaire.server.capability.BiterEntityCap;
import com.TBK.sanguinaire.server.capability.SGCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncBloodEntity {
    private final double blood;
    private final Entity entity;
    public PacketSyncBloodEntity(FriendlyByteBuf buf) {
        this.blood =buf.readDouble();
        Minecraft mc=Minecraft.getInstance();
        assert mc.level!=null;

        this.entity=mc.level.getEntity(buf.readInt());
    }

    public PacketSyncBloodEntity(double blood, Entity entity) {
        this.blood = blood;
        this.entity=entity;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeDouble(this.blood);
        buf.writeInt(this.entity.getId());
    }
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(()->{
            BiterEntityCap cap = SGCapability.getEntityEntity(entity,BiterEntityCap.class);
            if(cap!=null){
                cap.setBlood((int) this.blood);
            }
        });
        context.get().setPacketHandled(true);
    }
}
