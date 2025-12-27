package com.TBK.sanguinaire.server.skill.drakul;

import com.TBK.sanguinaire.common.block.BloodBlockEntity;
import com.TBK.sanguinaire.common.registry.SGBlocks;
import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.entity.summon.BloodSpikesEntity;
import com.TBK.sanguinaire.server.skill.SkillAbstract;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class BloodSpikes extends SkillAbstract {
    public BloodSpikes() {
        super("blood_spikes", 20, 20, 20, 1, false, false, false, true, false, 2);
    }

    @Override
    public void startSkillAbstract(SkillPlayerCapability skill) {
        super.startSkillAbstract(skill);
        if(!skill.getPlayer().level().isClientSide){
            this.summon(skill);
        }
    }
    
    public void summon(SkillPlayerCapability cap){

        Player player = cap.getPlayer();
        double d0 = player.getY();
        double d1 = player.getY() + 1.0D;
        float f  = (player.getYHeadRot() + 90.0F) * (float) Math.PI/180.0F;
        for(int l = 0; l < 16; ++l) {
            double d2 = 1.25*(l + 1);
            this.createSpellEntity(player,player.getX() + (double)Mth.cos(f) * d2, player.getZ() + (double)Mth.sin(f) * d2, d0, d1, f, l);
        }
    }

    private void createSpellEntity(Player player, double pX, double pZ, double pMinY, double pMaxY, float pYRot, int pWarmupDelay) {
        BlockPos blockpos = BlockPos.containing(pX, pMaxY, pZ);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = player.level().getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(player.level(), blockpos1, Direction.UP)) {
                if (!player.level().isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = player.level().getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(player.level(), blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= Mth.floor(pMinY) - 1);

        if (flag) {
            BlockPos initialPos = BlockPos.containing( pX, (double)blockpos.getY() + d0, pZ).below();
            for (BlockPos pos : BlockPos.betweenClosed(initialPos.offset(1,0,1),initialPos.offset(-1,0,-1))){
                BlockState oldState = player.level().getBlockState(pos);
                if(!oldState.isAir()){
                   if ((player.level().getBlockState(pos.above()).isAir())){
                       player.level().setBlock(pos.above(),SGBlocks.DIRT_BLOOD_PATH.get().defaultBlockState(),3);
                       BloodBlockEntity.startDegra(player.level(),pos.above(),player.level().getBlockState(pos.above()),((BloodBlockEntity)player.level().getBlockEntity(pos.above())));
                   }
                }
            }
            player.level().addFreshEntity(new BloodSpikesEntity(player.level(), pX, (double)blockpos.getY() + d0, pZ, pYRot, pWarmupDelay, player));
        }

    }

    @Override
    public List<String> getSequence() {
        List<String> sequenceRequest = new ArrayList<>();
        sequenceRequest.add("UP");
        sequenceRequest.add("UP");
        sequenceRequest.add("DOWN");
        return sequenceRequest;
    }
}
