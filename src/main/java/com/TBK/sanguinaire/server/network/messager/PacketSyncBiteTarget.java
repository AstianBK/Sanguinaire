package com.TBK.sanguinaire.server.network.messager;

import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
        context.get().enqueueWork(this::sync);
        context.get().setPacketHandled(true);
    }
    @OnlyIn(Dist.CLIENT)
    public void sync(){
        Minecraft mc = Minecraft.getInstance();
        Player player=mc.player;
        assert mc.level!=null;
        if(player!=null){
            Entity entity=mc.level.getEntity(this.idTarget);
            VampirePlayerCapability cap=VampirePlayerCapability.get(player);
            if (cap!=null){
                cap.bite(player,entity);
            }
        }
    }

}
