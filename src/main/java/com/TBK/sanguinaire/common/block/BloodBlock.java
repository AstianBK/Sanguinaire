package com.TBK.sanguinaire.common.block;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.common.registry.SGBlockEntities;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class BloodBlock extends BaseEntityBlock {
    public static final IntegerProperty AGE_15 = BlockStateProperties.AGE_7;
    public final  VoxelShape  SHAPE_DEGRA= Block.box(0.0D, 0.0D, 0.0D, 1.0D, 2.0D, 1.0D);

    public final  VoxelShape  SHAPE= Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);; 
    public BloodBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE_15,0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(AGE_15);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity entity = pLevel.getBlockEntity(pPos);
        if(entity instanceof BloodBlockEntity block){
            BloodBlockEntity.startDegra(pLevel,pPos,pState,block);
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
    public boolean isDregra (BlockState state){
        return state.getValue(AGE_15)>0;
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return isDregra(pState) ? SHAPE_DEGRA : SHAPE;
    }

    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return isDregra(pState) ? SHAPE_DEGRA : SHAPE;
    }

    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return isDregra(pState) ? SHAPE_DEGRA : SHAPE;

    }

    public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
        return isDregra(pState) ? SHAPE_DEGRA : SHAPE;
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

    @javax.annotation.Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide) {
            return  createTickerHelper(pBlockEntityType, SGBlockEntities.BLOOD.get(), BloodBlockEntity::particleTick);
        } else {
            return  createTickerHelper(pBlockEntityType, SGBlockEntities.BLOOD.get(), BloodBlockEntity::cookTick);
        }
    }
}
