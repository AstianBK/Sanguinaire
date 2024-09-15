package com.TBK.sanguinaire.server.network.messager;

import com.TBK.sanguinaire.common.registry.SGAttribute;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncBlood {
    private final double blood;
    public PacketSyncBlood(FriendlyByteBuf buf) {
        this.blood =buf.readDouble();
    }

    public PacketSyncBlood(double blood) {
        this.blood = blood;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeDouble(this.blood);
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
        entity.getAttribute(SGAttribute.BLOOD_VALUE.get()).setBaseValue(this.blood);
    }
}
