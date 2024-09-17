package com.TBK.sanguinaire.server.network.messager;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import com.TBK.sanguinaire.server.network.HandlerParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketHandlerParticles implements Packet<PacketListener>{
    private final int id;
    private final Entity entity;
    public PacketHandlerParticles(FriendlyByteBuf buf) {
        Minecraft mc=Minecraft.getInstance();
        assert mc.level!=null;
        this.entity=mc.level.getEntity(buf.readInt());
        this.id =buf.readInt();
    }

    public PacketHandlerParticles(int key, LivingEntity entity) {
        this.entity=entity;
        this.id = key;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entity.getId());
        buf.writeInt(this.id);
    }

    @Override
    public void handle(PacketListener p_131342_) {

    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(this::handlerAnim);
        context.get().setPacketHandled(true);
    }
    @OnlyIn(Dist.CLIENT)
    private void handlerAnim() {
        switch (this.id){
            case 0->{
                HandlerParticles.spawnCuts((LivingEntity) this.entity);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void upPower(SkillPlayerCapability player){
        player.upSkill();
    }

}
