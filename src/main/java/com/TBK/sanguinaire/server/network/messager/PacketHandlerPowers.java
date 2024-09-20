package com.TBK.sanguinaire.server.network.messager;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.manager.SkillAbstractInstance;
import com.TBK.sanguinaire.server.skill.SkillAbstract;
import com.TBK.sanguinaire.server.skill.SkillAbstracts;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PacketHandlerPowers implements Packet<PacketListener> {
    public PacketHandlerPowers(FriendlyByteBuf buf) {

    }


    public PacketHandlerPowers() {
    }

    @Override
    public void write(FriendlyByteBuf buf) {

    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(this::handlerAnim);
        context.get().setPacketHandled(true);
    }
    @OnlyIn(Dist.CLIENT)
    private void handlerAnim() {
        Player player=Minecraft.getInstance().player;
        SkillPlayerCapability cap=SkillPlayerCapability.get(player);
        if(cap!=null){
            cap.swingHand(player);
        }
    }

    @Override
    public void handle(PacketListener p_131342_) {


    }
}
