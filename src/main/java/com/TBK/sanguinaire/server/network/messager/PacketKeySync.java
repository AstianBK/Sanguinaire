package com.TBK.sanguinaire.server.network.messager;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketKeySync implements Packet<PacketListener>{
    private final int key;
    private final int action;
    private final int idTarget;

    public PacketKeySync(FriendlyByteBuf buf) {
        this.key=buf.readInt();
        this.action=buf.readInt();
        this.idTarget=buf.readInt();
    }

    public PacketKeySync(int key,int action,int idTarget) {
        this.key = key;
        this.action=action;
        this.idTarget=idTarget;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.key);
        buf.writeInt(this.action);
        buf.writeInt(this.idTarget);
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
        Player player=contextSupplier.get().getSender();
        SkillPlayerCapability skillPlayerCapability=SkillPlayerCapability.get(player);
        assert skillPlayerCapability != null;
        switch (this.key){
            case 0x52->{
                if(skillPlayerCapability.isVampire()){
                    if(this.action==0){
                        skillPlayerCapability.stopCasting(player);
                    }else if(this.action==1){
                        skillPlayerCapability.startCasting(player);
                    }
                }
            }
            case 0x43->{
                downPower(skillPlayerCapability);
            }
            case 0x56->{
                upPower(skillPlayerCapability);
            }
            default ->{
                bite(player);
            }
        }
    }
    public void bite(Player player){
        Entity target= player.level().getEntity(this.idTarget);
        if(target!=null){
            VampirePlayerCapability cap=VampirePlayerCapability.get(player);
            if(cap.isVampire() && cap.clientDrink<=0){
                cap.bite(player,target);
            }
        }
    }

    public static void upPower(SkillPlayerCapability skillPlayerCapability){
        if(skillPlayerCapability.isVampire()){
            skillPlayerCapability.upSkill();
        }
    }

    public static void downPower(SkillPlayerCapability skillPlayerCapability){
        if(skillPlayerCapability.isVampire()){
            skillPlayerCapability.downSkill();
        }
    }

}
