package com.TBK.sanguinaire.common.block;

import com.TBK.sanguinaire.common.registry.SGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BloodBlockEntity extends BlockEntity {
    public BloodBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(SGBlockEntities.BLOOD.get(), pPos, pBlockState);
    }

}
