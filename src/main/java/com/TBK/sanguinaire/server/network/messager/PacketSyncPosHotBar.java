package com.TBK.sanguinaire.server.network.messager;

import com.TBK.sanguinaire.common.registry.SGAttribute;
import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncPosHotBar {
    private final int pos;
    public PacketSyncPosHotBar(FriendlyByteBuf buf) {
        this.pos =buf.readInt();
    }

    public PacketSyncPosHotBar(int blood) {
        this.pos = blood;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.pos);
    }
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(()->{
            ServerPlayer player=context.get().getSender();
            assert player!=null;
            sync(player);
        });
        context.get().setPacketHandled(true);
    }
    public void sync(ServerPlayer player){
        SkillPlayerCapability.get(player).setPosSelectSkillAbstract(this.pos);
    }
}
