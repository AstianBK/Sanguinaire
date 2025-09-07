package com.TBK.sanguinaire.common;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.common.keybind.SGKeybinds;
import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import com.TBK.sanguinaire.server.network.PacketHandler;
import com.TBK.sanguinaire.server.network.messager.PacketKeySync;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Sanguinaire.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeInputEvent {
    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        onInput(mc, event.getKey(), event.getAction());
    }

    @SubscribeEvent
    public static void onMouseClick(InputEvent.MouseButton event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        onInput(mc, event.getButton(), event.getAction());
    }
    @SubscribeEvent
    public static void onMouseScrolling(InputEvent.MouseScrollingEvent event){
        Minecraft mc = Minecraft.getInstance();
        if(mc.level == null) return;
        if(SGKeybinds.attackKey3.isDown()){
            event.setCanceled(true);
            PacketHandler.sendToServer(new PacketKeySync(0x12,event.getScrollDelta()<0 ? 0 : 1,-1));
        }
    }

    private static boolean onInput(Minecraft mc, int key, int action) {
        SkillPlayerCapability cap = SkillPlayerCapability.get(mc.player);
        if(cap!=null){
            cap.hotbarActive = SGKeybinds.attackKey3.isDown();
        }
        if (mc.screen == null && (key==1)) {
            PacketHandler.sendToServer(new PacketKeySync(0x52,action,-1));
            return true;
        }else if(mc.screen==null && SGKeybinds.attackKey1.consumeClick() && mc.hitResult!=null && mc.hitResult.getType()== HitResult.Type.ENTITY){
            PacketHandler.sendToServer(new PacketKeySync(key,action,((EntityHitResult)mc.hitResult).getEntity().getId()));
        }
        return false;
    }
}
