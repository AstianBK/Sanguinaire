package com.TBK.sanguinaire.server;

import com.TBK.sanguinaire.client.layer.VampireLayer;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import net.minecraft.world.entity.player.Player;

public class Util {
    public static boolean isVampire(Player player){
        VampirePlayerCapability cap= VampirePlayerCapability.get(player);
        return cap!=null && cap.isVampire();
    }
}
