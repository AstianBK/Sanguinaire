package com.TBK.sanguinaire.common;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.server.network.PacketHandler;
import net.minecraft.client.Minecraft;
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

    private static void onInput(Minecraft mc, int key, int action) {
        /*if (mc.screen == null && BKKeybinds.attackKey1.consumeClick()) {
            PacketHandler.sendToServer(new PacketKeySync(key));
        }else if (mc.screen == null && BKKeybinds.attackKey2.consumeClick()) {
            PacketHandler.sendToServer(new PacketKeySync(key));
        }*/
    }
}
