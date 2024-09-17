package com.TBK.sanguinaire.server.network.messager;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketKeySync implements Packet<PacketListener>{
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

    @Override
    public void handle(PacketListener p_131342_) {

    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(this::handlerAnim);
        context.get().setPacketHandled(true);
    }
    @OnlyIn(Dist.CLIENT)
    private void handlerAnim() {
        Minecraft mc=Minecraft.getInstance();
        if(this.key==0x43){
            SkillPlayerCapability skillPlayerCapability=SkillPlayerCapability.get(mc.player);
            assert skillPlayerCapability != null;
            upPower(skillPlayerCapability);
        }else {
            Player player=mc.player;
            HitResult hit = mc.hitResult;
            assert player!=null && hit!=null;
            if(hit.getType() == HitResult.Type.ENTITY){
                VampirePlayerCapability cap=VampirePlayerCapability.get(player);
                cap.bite(player,((EntityHitResult)hit).getEntity());
            }
        }

    }

    @OnlyIn(Dist.CLIENT)
    public static void upPower(SkillPlayerCapability player){
        player.upSkill();
    }

}
