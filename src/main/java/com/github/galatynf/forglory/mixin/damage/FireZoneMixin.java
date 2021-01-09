package com.github.galatynf.forglory.mixin.damage;

import com.github.galatynf.forglory.Forglory;
import com.github.galatynf.forglory.blocks.QuickFireBlock;
import com.github.galatynf.forglory.config.ModConfig;
import com.github.galatynf.forglory.enumFeat.Feats;
import com.github.galatynf.forglory.enumFeat.Tier;
import com.github.galatynf.forglory.imixin.IAdrenalinMixin;
import com.github.galatynf.forglory.imixin.IFeatsMixin;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class FireZoneMixin extends Entity {
    public FireZoneMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    int forglory_playerTick;
    @Unique
    int forglory_fireTick;

    @Inject(at=@At("INVOKE"), method = "tick")
    private void spawnFireZone(CallbackInfo ci) {
        Feats feat = ((IFeatsMixin)this).getFeat(Tier.TIER3);
        if (feat == null) return;
        if (feat.equals(Feats.FIRE_ZONE)) {
            if (((IAdrenalinMixin)this).getAdrenalin() > Tier.TIER3.threshold) {
                ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
                forglory_playerTick = (forglory_playerTick +1) % config.fireZoneConfig.circle_rate;
                if ((forglory_playerTick % (config.fireZoneConfig.circle_rate / config.fireZoneConfig.fire_rate)) == 0) {
                    forglory_fireTick = (forglory_fireTick +1) % config.fireZoneConfig.fire_rate;
                    double xOffset = Math.cos(forglory_fireTick * ((2*Math.PI) / config.fireZoneConfig.fire_rate));
                    double zOffset = Math.sin(forglory_fireTick * ((2*Math.PI) / config.fireZoneConfig.fire_rate));

                    BlockPos blockPos = this.getBlockPos().add(config.fireZoneConfig.radius * xOffset,
                            0,
                            config.fireZoneConfig.radius * zOffset);

                    spawnFire(blockPos);
                }
            }
        }
    }

    private void spawnFire(BlockPos blockPos) {
        BlockPos belowBlockPos = blockPos.down();

        if (this.world.getBlockState(blockPos).isAir()
                && !this.world.getBlockState(belowBlockPos).isAir()
                && !this.world.getBlockState(belowBlockPos).getBlock().equals(Forglory.quickFireBlock)) {
            this.world.setBlockState(blockPos,
                    Forglory.quickFireBlock.getDefaultState().with(QuickFireBlock.SHORT, true));
        } else {
            for (int i = 1; i < 3; i++) {
                if (this.world.getBlockState(blockPos.down(i)).isAir()
                        && !this.world.getBlockState(belowBlockPos.down(i)).isAir()
                        && !this.world.getBlockState(belowBlockPos.down(i)).getBlock().equals(Forglory.quickFireBlock)) {
                    this.world.setBlockState(blockPos.down(i),
                            Forglory.quickFireBlock.getDefaultState().with(QuickFireBlock.SHORT, true));
                    break;
                } else if (this.world.getBlockState(blockPos.up(i)).isAir()
                        && !this.world.getBlockState(belowBlockPos.up(i)).isAir()
                        && !this.world.getBlockState(belowBlockPos.up(i)).getBlock().equals(Forglory.quickFireBlock)) {
                    this.world.setBlockState(blockPos.up(i),
                            Forglory.quickFireBlock.getDefaultState().with(QuickFireBlock.SHORT, true));
                    break;
                }
            }
        }
    }
}