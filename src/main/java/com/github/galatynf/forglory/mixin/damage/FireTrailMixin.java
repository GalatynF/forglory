package com.github.galatynf.forglory.mixin.damage;

import com.github.galatynf.forglory.Forglory;
import com.github.galatynf.forglory.Utils;
import com.github.galatynf.forglory.enumFeat.Feats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class FireTrailMixin extends Entity {
    public FireTrailMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at=@At("INVOKE"), method = "tick")
    void addFireTrail(CallbackInfo ci) {
        if (Utils.canUseFeat(this, Feats.FIRE_TRAIL)) {
            BlockPos blockPos = this.getBlockPos().offset(this.getMovementDirection().getOpposite());
            spawnFire(blockPos);
        }
    }

    private void spawnFire(BlockPos blockPos) {
        BlockPos belowBlockPos = blockPos.down();

        if (this.world.getBlockState(blockPos).isAir()
                && !this.world.getBlockState(belowBlockPos).isAir()
                && !this.world.getBlockState(belowBlockPos).getBlock().equals(Forglory.quickFireBlock)) {
            this.world.setBlockState(blockPos, Forglory.quickFireBlock.getDefaultState());
        } else if (this.world.getBlockState(blockPos.down()).isAir()
                && !this.world.getBlockState(belowBlockPos.down()).isAir()
                && !this.world.getBlockState(belowBlockPos.down()).getBlock().equals(Forglory.quickFireBlock)) {
            this.world.setBlockState(blockPos.down(), Forglory.quickFireBlock.getDefaultState());
        }
    }
}
