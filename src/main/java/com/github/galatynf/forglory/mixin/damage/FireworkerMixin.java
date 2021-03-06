package com.github.galatynf.forglory.mixin.damage;

import com.github.galatynf.forglory.Utils;
import com.github.galatynf.forglory.cardinal.MyComponents;
import com.github.galatynf.forglory.config.ModConfig;
import com.github.galatynf.forglory.enumFeat.Feats;
import com.github.galatynf.forglory.enumFeat.FeatsClass;
import com.github.galatynf.forglory.init.NetworkInit;
import com.github.galatynf.forglory.init.SoundsInit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.FireworkItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(CrossbowItem.class)
public class FireworkerMixin {

    @Inject(method = "loadProjectiles", at = @At("HEAD"))
    private static void setFireworkProjectile(LivingEntity shooter, ItemStack projectile, CallbackInfoReturnable<Boolean> cir) {
        if (Utils.canUseFeat(shooter, Feats.FIREWORKER)) {
            if(MyComponents.FEATS.get(shooter).getForgloryClass() == FeatsClass.HUNTER) {
                NetworkInit.playSound(SoundsInit.FIREWORKER_VOICE_ID, (ServerPlayerEntity) shooter, true);
            }
            CompoundTag compoundTag = new CompoundTag();
            CompoundTag firework = new CompoundTag();
            ListTag explosionsList = new ListTag();
            CompoundTag explosions = new CompoundTag();

            firework.putInt("Flight", 3);

            List<Integer> colors = new ArrayList<>();
            colors.add(DyeColor.RED.getFireworkColor());
            colors.add(DyeColor.WHITE.getFireworkColor());
            explosions.putIntArray("Colors", colors);
            explosions.putInt("Type", FireworkItem.Type.SMALL_BALL.getId());

            for (int i = 0; i < ModConfig.get().featConfig.fireworker_power; i++) {
                explosionsList.add(explosions);
            }
            firework.put("Explosions", explosionsList);

            compoundTag.put("Fireworks", firework);
            ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET);
            stack.setTag(compoundTag);

            ItemStack stackOld = shooter.getOffHandStack();
            stackOld.getOrCreateSubTag("Offhand");
            shooter.setStackInHand(Hand.OFF_HAND, stack);
            ((PlayerEntity) shooter).giveItemStack(stackOld);
        }
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;setCharged(Lnet/minecraft/item/ItemStack;Z)V"))
    private void giveOffhandBack(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (Utils.canUseFeat(user, Feats.FIREWORKER)) {
            for (int i = 0; i < user.inventory.main.size(); i++) {
                ItemStack itemstack = user.inventory.main.get(i);
                if (itemstack.getSubTag("Offhand") != null) {
                    itemstack.removeSubTag("Offhand");
                    user.setStackInHand(Hand.OFF_HAND, itemstack);
                    user.inventory.main.set(i, ItemStack.EMPTY);
                    return;
                }
            }
        }
    }
}
