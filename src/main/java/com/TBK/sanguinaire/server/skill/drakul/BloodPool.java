package com.TBK.sanguinaire.server.skill.drakul;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.common.registry.SGBlocks;
import com.TBK.sanguinaire.common.registry.SGSkillAbstract;
import com.TBK.sanguinaire.server.ModBusEvent;
import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import com.TBK.sanguinaire.server.manager.DurationInstance;
import com.TBK.sanguinaire.server.manager.DurationResult;
import com.TBK.sanguinaire.server.skill.InstantSkill;
import com.TBK.sanguinaire.server.skill.SkillAbstract;
import com.TBK.sanguinaire.server.skill.SkillAbstracts;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class BloodPool extends InstantSkill {
    public BloodPool() {
        super("blood_pool", 100, 1, 10);
    }

    @Override
    public void startSkillAbstract(SkillPlayerCapability skill) {
        super.startSkillAbstract( skill);
        Player player = skill.getPlayer();
        EntityHitResult hit = ProjectileUtil.getEntityHitResult(player.level(),player,player.getEyePosition(),player.getEyePosition().add(calculateViewVector(player.getViewXRot(1.0F),player.getYHeadRot()).scale(100.0D)),player.getBoundingBox().inflate(100.0F), e->!player.is(e),0.5F);
        BlockHitResult blockEnd = player.level().clip(new ClipContext(player.getEyePosition(),player.getEyePosition().add(calculateViewVector(player.getViewXRot(1.0F),player.getYHeadRot()).scale(100.0D)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,player));
        if(blockEnd.getType()!= HitResult.Type.MISS){
            teleportSafePosition(blockEnd,skill,player);
        }else if(hit.getType() != HitResult.Type.MISS){
            BlockPos pos=player.blockPosition();
            teleportSafePosition(new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP,pos,false),skill,player);
        }
    }
    protected void teleportSafePosition (BlockHitResult blockEnd,SkillPlayerCapability cap,Player player){
        BlockPos pos = blockEnd.getBlockPos();
        BlockState state = player.level().getBlockState(pos);
        if(state.is(SGBlocks.DIRT_BLOOD_PATH.get())){
            teleport(cap,player,pos);
        }else {
            for (BlockPos pos1 : BlockPos.betweenClosed(pos.offset(-3,-1,-3),pos.offset(3,1,3))){
                BlockState state1 = player.level().getBlockState(pos1);
                if(state1.is(SGBlocks.DIRT_BLOOD_PATH.get())){
                    teleport(cap,player,pos1);
                    player.level().setBlock(pos1,Blocks.GRASS_BLOCK.defaultBlockState(),3);
                    break;
                }
            }
        }
    }

    protected void teleport(SkillPlayerCapability cap,Player player,BlockPos pos){
        Sanguinaire.LOGGER.debug("Initial Teleport");
        ModBusEvent.loseBody(cap.getPlayerVampire(),player);
        player.teleportTo(pos.getX(),pos.getY()+2,pos.getZ());

    }
    protected final Vec3 calculateViewVector(float p_20172_, float p_20173_) {
        float f = p_20172_ * ((float)Math.PI / 180F);
        float f1 = -p_20173_ * ((float)Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return new Vec3((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
    }
    @Override
    public List<String> getSequence() {
        List<String> sequenceRequest = new ArrayList<>();
        sequenceRequest.add("DOWN");
        sequenceRequest.add("DOWN");
        sequenceRequest.add("DOWN");
        return sequenceRequest;
    }
}
