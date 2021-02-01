package com.github.galatynf.forglory.cardinal;

import com.github.galatynf.forglory.config.ConstantsConfig;
import com.github.galatynf.forglory.config.ModConfig;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

public class PlayerAdrenalinComponent implements AdrenalinComponent, AutoSyncedComponent {

    private final PlayerEntity provider;
    private float forglory_adrenalin;

    public PlayerAdrenalinComponent(final PlayerEntity playerEntity) {
        provider = playerEntity;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        forglory_adrenalin = tag.getFloat("adrenalin");
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putFloat("adrenalin", forglory_adrenalin);
    }

    @Override
    public float getAdrenalin() {
        return forglory_adrenalin;
    }

    @Override
    public void setAdrenalin(final float newAdrenalin) {
        forglory_adrenalin = newAdrenalin;
        MyComponents.ADRENALIN.sync(provider);
    }

    @Override
    public void addAdrenalin(final float amount) {
        forglory_adrenalin += amount;

        if (forglory_adrenalin > ModConfig.get().adrenalinConfig.max_amount) {
            forglory_adrenalin = ModConfig.get().adrenalinConfig.max_amount;
        }
        if (forglory_adrenalin < ConstantsConfig.MIN_AMOUNT) {
            forglory_adrenalin = ConstantsConfig.MIN_AMOUNT;
        }
        MyComponents.ADRENALIN.sync(provider);
    }
}
