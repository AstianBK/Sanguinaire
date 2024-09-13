package com.TBK.sanguinaire.server.network.messager;

import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketRemoveActiveEffect {
    private final int id;
    private final String powerId;

    public PacketRemoveActiveEffect(String powerId,int id) {
        this.powerId = powerId;
        this.id=id;
    }

    public PacketRemoveActiveEffect(FriendlyByteBuf buf) {
        this.powerId = buf.readUtf();
        this.id=buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.powerId);
        buf.writeInt(this.id);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            SkillPlayerCapability cap = SkillPlayerCapability.get(mc.player);
            VampirePlayerCapability cap1 = VampirePlayerCapability.get(mc.player);
            assert cap!=null && cap1!=null;
            if(this.id==0){
                cap.getActiveEffectDuration().removeDuration(powerId);
            }else {
                cap1.getLimbsPartRegeneration().regenerateLimbClient(powerId);
            }
        });
        return true;
    }
}
