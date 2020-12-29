package com.github.galatynf.forglory.mixin.damage;

import com.github.galatynf.forglory.config.constants.FeatsConfig;
import com.github.galatynf.forglory.enumFeat.Feats;
import com.github.galatynf.forglory.enumFeat.Tier;
import com.github.galatynf.forglory.imixin.IAdrenalinMixin;
import com.github.galatynf.forglory.imixin.IFeatsMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


@Mixin(LivingEntity.class)
public abstract class SmiteMixin extends Entity{

    public SmiteMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract boolean isUndead();

    @ModifyArg(method = "damage", at=@At(value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private float injectedAmount(DamageSource source, float amount) {
        Entity sourceAttacker = source.getAttacker();
        if(sourceAttacker instanceof PlayerEntity) {
            Feats feat = ((IFeatsMixin)sourceAttacker).getFeat(Tier.TIER1);
            if (feat == null) return amount;
                if (feat.equals(Feats.SMITE)) {
                    if (((IAdrenalinMixin) sourceAttacker).getAdrenalin() > Tier.TIER1.threshold)
                        if (this.isUndead()) {
                            return (amount * FeatsConfig.SMITE_MULTIPLIER);
                        }
                }
        }
        return amount;
    }
}
