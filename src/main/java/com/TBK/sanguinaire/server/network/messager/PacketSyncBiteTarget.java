package com.TBK.sanguinaire.server.network.messager;

import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncBiteTarget {
    private final int idTarget;
    private final UUID idEntity;
    public PacketSyncBiteTarget(FriendlyByteBuf buf) {
        this.idTarget =buf.readInt();
        this.idEntity=buf.readUUID();
    }

    public PacketSyncBiteTarget(int idTarget, UUID id) {
        this.idTarget = idTarget;
        this.idEntity= id;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(idTarget);
        buf.writeUUID(this.idEntity);
    }
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(()->{
            ServerPlayer player= context.get().getSender();
            if(player!=null){
                Entity entity=player.level().getEntity(this.idTarget);
                VampirePlayerCapability cap=VampirePlayerCapability.get(player);
                if (cap!=null){
                    cap.bite(player,entity);
                }
            }
        });
        context.get().setPacketHandled(true);
    }

}
