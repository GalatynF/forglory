package com.github.galatynf.forglory.mixin.damage;

import com.github.galatynf.forglory.Utils;
import com.github.galatynf.forglory.config.ModConfig;
import com.github.galatynf.forglory.enumFeat.Feats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class SmiteMixin extends Entity {

    public SmiteMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract boolean isUndead();

    @ModifyArg(method = "damage", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private float injectedAmount(DamageSource source, float amount) {
        if (Utils.canUseFeat(source.getAttacker(), Feats.SMITE)) {
            if (this.isUndead()) {
                return (amount * (ModConfig.get().featConfig.smite_multiplier / 10.F));
            }
        }

        return amount;
    }
}
