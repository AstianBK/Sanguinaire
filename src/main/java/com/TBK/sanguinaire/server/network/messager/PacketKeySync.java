package com.TBK.sanguinaire.server.network.messager;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketKeySync implements Packet<PacketListener>{
    private final int key;
    private final int action;

    public PacketKeySync(FriendlyByteBuf buf) {
        this.key=buf.readInt();
        this.action=buf.readInt();
    }

    public PacketKeySync(int key,int action) {
        this.key = key;
        this.action=action;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.key);
        buf.writeInt(this.action);
    }

    @Override
    public void handle(PacketListener p_131342_) {

    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(()->{
            handlerAnim(context);
        });
        context.get().setPacketHandled(true);
    }

    private void handlerAnim(Supplier<NetworkEvent.Context> contextSupplier) {
        switch (this.key){
            case 0x52->{
                Player player=contextSupplier.get().getSender();
                SkillPlayerCapability skillPlayerCapability=SkillPlayerCapability.get(player);
                assert skillPlayerCapability != null;
                if(skillPlayerCapability.isVampire()){
                    if(this.action==0){
                        skillPlayerCapability.stopCasting(player);
                    }else if(this.action==1){
                        skillPlayerCapability.startCasting(player);
                    }
                }
            }
            case 0x43->{
                Player player=contextSupplier.get().getSender();
                SkillPlayerCapability skillPlayerCapability=SkillPlayerCapability.get(player);
                assert skillPlayerCapability != null;
                if(skillPlayerCapability.isVampire()){
                    downPower(skillPlayerCapability);
                }
            }
            case 0x56->{
                Player player=contextSupplier.get().getSender();
                SkillPlayerCapability skillPlayerCapability=SkillPlayerCapability.get(player);
                assert skillPlayerCapability != null;
                if(skillPlayerCapability.isVampire()){
                    upPower(skillPlayerCapability);
                }
            }
            default ->{

            }
        }

    }

    @OnlyIn(Dist.CLIENT)
    public static void upPower(SkillPlayerCapability player){
        player.upSkill();
    }

    @OnlyIn(Dist.CLIENT)
    public static void downPower(SkillPlayerCapability player){
        player.downSkill();
    }

}
