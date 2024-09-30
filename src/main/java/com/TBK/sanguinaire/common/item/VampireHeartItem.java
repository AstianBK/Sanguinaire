package com.TBK.sanguinaire.common.item;

import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class VampireHeartItem extends Item {
    public VampireHeartItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack p_41409_, Level p_41410_, LivingEntity p_41411_) {
        if(p_41411_ instanceof Player){
            VampirePlayerCapability cap=VampirePlayerCapability.get((Player) p_41411_);
            if(cap!=null){
                cap.drainBlood(1);
                if(!cap.isVampire() && p_41410_.random.nextFloat()<0.01F){
                    cap.convert(false);
                }
            }
        }
        return super.finishUsingItem(p_41409_, p_41410_, p_41411_);
    }
}
