package com.github.galatynf.forglory.items;

import com.github.galatynf.forglory.Forglory;
import com.github.galatynf.forglory.enumFeat.Feats;
import com.github.galatynf.forglory.imixin.IAdrenalinMixin;
import com.github.galatynf.forglory.imixin.IFeatsMixin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public abstract class PoweredGem extends Item {
    protected Feats feat;
    protected PoweredGem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        BlockPos pos = user.getBlockPos().add(new Vec3i(0, -1, 0));

        if(((IAdrenalinMixin)user).getAdrenalin() == 0
            && world.getBlockState(pos).isOf(Forglory.essenceInfuser)) {
            ((IFeatsMixin) user).addOrUpdateFeat(feat);
            return new TypedActionResult<>(ActionResult.SUCCESS, user.getStackInHand(hand));
        }
        return new TypedActionResult<>(ActionResult.FAIL, user.getStackInHand(hand));
    }
}