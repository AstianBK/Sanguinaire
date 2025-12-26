package com.TBK.sanguinaire.client.gui;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.server.capability.SGCapability;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = Sanguinaire.MODID,
        value = Dist.CLIENT
)
public class BloodStarvationClientHandler {

    private static final ResourceLocation DESATURATE =
            new ResourceLocation("minecraft", "shaders/post/desaturate.json");


    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null) return;

        VampirePlayerCapability cap = SGCapability.getEntityVam(player, VampirePlayerCapability.class);

        if (cap == null) return;

        boolean noBlood = cap.getBlood() <= 0;
        boolean shaderActuallyActive = mc.gameRenderer.currentEffect() != null;

        if (noBlood && !shaderActuallyActive) {
            mc.gameRenderer.loadEffect(DESATURATE);
        }

        if (!noBlood && shaderActuallyActive) {
            mc.gameRenderer.shutdownEffect();
        }
    }
}
