package com.TBK.sanguinaire.common.item;

import com.TBK.sanguinaire.common.registry.SGSounds;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GobletItem extends Item {
    public GobletItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public int getDamage(ItemStack stack) {
        return getBlood(stack)>0 ? 10-getBlood(stack) : 10;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 10;
    }

    @Override
    public void onCraftedBy(ItemStack p_41447_, Level p_41448_, Player p_41449_) {
        super.onCraftedBy(p_41447_, p_41448_, p_41449_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        ItemStack stack=p_41433_.getItemInHand(p_41434_);
        if(stack.getItem() instanceof GobletItem) {
            int blood=getBlood(stack);
            if(blood>0){
                VampirePlayerCapability cap=VampirePlayerCapability.get(p_41433_);
                if(cap!=null && cap.isVampire()){
                    if(cap.getBlood()<cap.getMaxBlood()){
                        cap.drainBlood(1);
                        p_41432_.playSound(null,p_41433_, SGSounds.BLOOD_DRINK.get(), SoundSource.PLAYERS,1.0F,1.0F);
                        setBlood(stack,blood-1);
                    }
                }
            }
        }
        return super.use(p_41432_, p_41433_, p_41434_);
    }
    public static int getBlood(ItemStack stack){
        CompoundTag compoundtag = stack.getTag();
        return compoundtag != null ? compoundtag.getInt("blood") : 0;
    }
    public static boolean canFillGoblet(ItemStack stack){
        return stack.getItem() instanceof GobletItem && getBlood(stack)>=0 && getBlood(stack)<10;
    }

    public static void setBlood(ItemStack p_40885_, int blood) {
        CompoundTag compoundtag = p_40885_.getOrCreateTag();
        compoundtag.putInt("blood", blood);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.translatable("item.sanguinaire.gold_goblet.blood"+getBlood(p_41421_)));
    }
}
