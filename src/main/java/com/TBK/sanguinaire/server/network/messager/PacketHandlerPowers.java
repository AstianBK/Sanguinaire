package com.TBK.sanguinaire.server.network.messager;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketHandlerPowers implements Packet<PacketListener> {
    private final int id;
    private final Entity newEntity;
    private final Entity oldEntity;
    public PacketHandlerPowers(FriendlyByteBuf buf) {
        Minecraft mc=Minecraft.getInstance();
        assert mc.level!=null;
        this.id=buf.readInt();
        this.newEntity =mc.level.getEntity(buf.readInt());
        this.oldEntity = mc.level.getEntity(buf.readInt());
    }


    public PacketHandlerPowers(int id, Entity entity, Player player) {
        this.id=id;
        this.newEntity =entity;
        this.oldEntity =player;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeInt(this.newEntity.getId());
        buf.writeInt(this.oldEntity.getId());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(this::handlerAnim);
        context.get().setPacketHandled(true);
    }
    @OnlyIn(Dist.CLIENT)
    private void handlerAnim() {
        Player player=Minecraft.getInstance().player;
        switch (this.id){
            case 0->{
                SkillPlayerCapability cap=SkillPlayerCapability.get(player);
                if(cap!=null){
                    this.start(cap,player);
                }
            }
            case 1->{
                VampirePlayerCapability cap=VampirePlayerCapability.get((Player) this.oldEntity);
                if(cap!=null){
                    cap.clone(cap, (Player) this.oldEntity, (Player) this.newEntity);
                }
            }
            case 2->{
                SkillPlayerCapability cap=SkillPlayerCapability.get(player);
                if(cap!=null){
                    this.stop(cap,player);
                }
            }
        }

    }
    @OnlyIn(Dist.CLIENT)
    public void start(SkillPlayerCapability cap,Player player){
        cap.startCasting(player);
    }

    @OnlyIn(Dist.CLIENT)
    public void stop(SkillPlayerCapability cap,Player player){
        cap.stopCasting(player);
    }
    @Override
    public void handle(PacketListener p_131342_) {


    }
}
