package com.github.galatynf.forglory.mixin.mobility;

import com.github.galatynf.forglory.Utils;
import com.github.galatynf.forglory.enumFeat.Feats;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class NoHungerMixin extends LivingEntity {
    @Shadow
    public abstract HungerManager getHungerManager();

    @Shadow
    protected HungerManager hungerManager;

    protected NoHungerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("INVOKE"), method = "addExhaustion", cancellable = true)
    void preventHungerFromDropping(CallbackInfo ci) {
        if (Utils.canUseFeat(this, Feats.NO_HUNGER)) {
            if (this.getHungerManager().getFoodLevel() < 7) {
                this.hungerManager.setFoodLevel(7);
                ci.cancel();
            }
        }
    }
}
