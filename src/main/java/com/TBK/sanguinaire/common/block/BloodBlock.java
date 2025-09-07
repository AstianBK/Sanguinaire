package com.TBK.sanguinaire.common.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class BloodBlock extends BaseEntityBlock {
    public final  VoxelShape  SHAPE= Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);; 
    public BloodBlock(Properties pProperties) {
        super(pProperties);
    }
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return SHAPE;
    }

    public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BloodBlockEntity(pPos,pState);
    }
}
