package com.TBK.sanguinaire.common.block;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.common.registry.SGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;

public class BloodBlockEntity extends BlockEntity {
    public int degraTime = 0;
    public int delay = 0;
    public BloodBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(SGBlockEntities.BLOOD.get(), pPos, pBlockState);
    }


    public static void particleTick(Level pLevel, BlockPos pPos, BlockState pState, BloodBlockEntity pBlockEntity) {
        RandomSource randomsource = pLevel.random;


    }
    public static void startDegra(Level level,BlockPos pos ,BlockState state, BloodBlockEntity entity){
        entity.delay = 2;
        entity.degraTime = 1;
        setChanged(level,pos,state);
    }

    public static void cookTick(Level pLevel, BlockPos pPos, BlockState pState, BloodBlockEntity pBlockEntity) {
        boolean flag = false;
        int degra = pState.getValue(BloodBlock.AGE_15);
        int time = pBlockEntity.degraTime;

        if(time>0){

            pBlockEntity.degraTime--;
            if(pBlockEntity.degraTime<=0){
                if(degra<6){
                    pBlockEntity.degraTime = 10;
                    pLevel.setBlock(pPos,pState.setValue(BloodBlock.AGE_15,degra+1),3);
                }else {
                    pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState() ,3);
                }
            }
            flag=true;
        }

        if (flag) {
            setChanged(pLevel, pPos, pState);
        }
    }
}
