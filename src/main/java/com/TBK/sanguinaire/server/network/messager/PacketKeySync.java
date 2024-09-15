package com.TBK.sanguinaire.server.network.messager;

import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketKeySync {
    private final int key;


    public PacketKeySync(FriendlyByteBuf buf) {
        this.key=buf.readInt();
    }

    public PacketKeySync(int key) {
        this.key = key;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.key);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() ->{
            Player player = context.get().getSender();
            if(player!=null){
                handlerAnim(player);
            }
        });
        context.get().setPacketHandled(true);
    }

    private void handlerAnim(LivingEntity entity) {
        Minecraft mc=Minecraft.getInstance();
        Player player=mc.player;
        HitResult hit = mc.hitResult;
        assert player!=null && hit!=null;
        if(hit.getType() == HitResult.Type.ENTITY){
            VampirePlayerCapability cap=VampirePlayerCapability.get(player);
            cap.bite(player,((EntityHitResult)hit).getEntity());
        }
    }
}
