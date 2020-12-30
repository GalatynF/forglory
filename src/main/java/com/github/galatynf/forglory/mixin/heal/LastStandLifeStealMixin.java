package com.github.galatynf.forglory.mixin.heal;

import com.github.galatynf.forglory.Forglory;
import com.github.galatynf.forglory.config.constants.FeatsConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LastStandLifeStealMixin {
    @Inject(method = "damage", at = @At("TAIL"))
    private void giveHealth(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        if(attacker instanceof PlayerEntity
                && ((PlayerEntity) attacker).hasStatusEffect(Forglory.lifeStealStatusEffect)) {
            ((PlayerEntity) attacker).setHealth(((PlayerEntity) attacker).getHealth() +
                    (amount > FeatsConfig.LIFE_STEAL_MAX_AMOUNT ? FeatsConfig.LIFE_STEAL_MAX_AMOUNT : amount));
        }
    }
}