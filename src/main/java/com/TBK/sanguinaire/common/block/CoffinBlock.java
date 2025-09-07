package com.TBK.sanguinaire.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class CoffinBlock extends BedBlock {

    public CoffinBlock() {
        super(net.minecraft.world.item.DyeColor.BLACK,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_BLACK).strength(2.0f).noOcclusion().pushReaction(PushReaction.DESTROY)
        );

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, net.minecraft.core.Direction.NORTH).setValue(PART, BedPart.FOOT).setValue(OCCUPIED, false));
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, OCCUPIED);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CoffinBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {

        if (!level.isClientSide) {
            player.startSleeping(pos);
            player.playSound(SoundEvents.WOOD_PLACE, 1.0F, 0.8F);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        return super.getStateForPlacement(context);
    }
}
