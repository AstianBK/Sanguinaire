package com.TBK.sanguinaire.common;

import com.AstianBk.the_gifted.common.keybind.BKKeybinds;
import com.AstianBk.the_gifted.server.network.PacketHandler;
import com.AstianBk.the_gifted.server.network.message.PacketKeySync;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheGifted.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InputEvents {
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
        if (mc.screen == null && BKKeybinds.attackKey1.consumeClick()) {
            PacketHandler.sendToServer(new PacketKeySync(key));
        }else if (mc.screen == null && BKKeybinds.attackKey2.consumeClick()) {
            PacketHandler.sendToServer(new PacketKeySync(key));
        }
    }
}
