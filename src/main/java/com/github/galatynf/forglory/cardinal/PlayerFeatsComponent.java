package com.github.galatynf.forglory.cardinal;

import com.github.galatynf.forglory.config.ConstantsConfig;
import com.github.galatynf.forglory.enumFeat.Feats;
import com.github.galatynf.forglory.enumFeat.FeatsClass;
import com.github.galatynf.forglory.enumFeat.Tier;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;

public class PlayerFeatsComponent implements FeatsComponent, AutoSyncedComponent {

    private final PlayerEntity provider;
    private final HashMap<Tier, Feats> forglory_feats = new HashMap<>();
    private final HashMap<Tier, Integer> forglory_cooldowns = new HashMap<>();
    private FeatsClass forglory_class = FeatsClass.NONE;
    private boolean forglory_has_a_feat = false;

    public PlayerFeatsComponent(final PlayerEntity playerEntity) {
        provider = playerEntity;
        for (Tier tier : Tier.values()) {
            forglory_feats.put(tier, Feats.NO_FEAT);
            forglory_cooldowns.put(tier, Feats.NO_FEAT.cooldown);
        }
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        for (Tier tier : Tier.values()) {
            this.forglory_feats.put(tier, Feats.valueOf(tag.getString("feat" + tier.toString())));
            this.forglory_cooldowns.put(tier, tag.getInt("cooldown" + tier.toString()));
        }
        this.forglory_class = FeatsClass.valueOf(tag.getString("class"));
        this.forglory_has_a_feat = tag.getBoolean("hasFeat");
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        for (Tier tier : Tier.values()) {
            tag.putString("feat" + tier.toString(), forglory_feats.get(tier).toString());
            tag.putInt("cooldown" + tier.toString(), forglory_cooldowns.get(tier));
        }
        tag.putString("class", forglory_class.toString());
        tag.putBoolean("hasFeat", forglory_has_a_feat);
    }

    @Override
    public Feats getFeat(final Tier tier) {
        return forglory_feats.get(tier);
    }

    @Override
    public Integer getCooldown(final Tier tier) {
        return forglory_cooldowns.get(tier);
    }

    public FeatsClass getForgloryClass() {
        return forglory_class;
    }

    @Override
    public void addOrUpdateFeat(final Feats feat) {
        forglory_feats.put(feat.tier, feat);
        forglory_cooldowns.put(feat.tier, ConstantsConfig.NO_COOLDOWN);
        forglory_class = FeatsClass.hasClass(forglory_feats);
        forglory_has_a_feat = true;
        MyComponents.FEATS.sync(provider);
    }

    @Override
    public void removeFeat(Tier tier) {
        forglory_feats.put(tier, Feats.NO_FEAT);
        forglory_cooldowns.put(tier, ConstantsConfig.NO_COOLDOWN);
        forglory_class = FeatsClass.hasClass(forglory_feats);
        forglory_has_a_feat = false;
        for(Feats aFeat : forglory_feats.values()) {
            if(aFeat != Feats.NO_FEAT) {
                forglory_has_a_feat = true;
                break;
            }
        }
        MyComponents.FEATS.sync(provider);
    }

    @Override
    public void resetCooldown(final Tier tier) {
        Feats feat = forglory_feats.get(tier);
        forglory_cooldowns.put(tier, feat.cooldown);
    }

    @Override
    public void setUniqueCooldown(final Tier tier) {
        forglory_cooldowns.put(tier, ConstantsConfig.UNIQUE_COOLDOWN);
    }

    @Override
    public void decrementCooldowns() {
        for (Tier tier : Tier.values()) {
            Integer cooldown = forglory_cooldowns.get(tier);
            if (cooldown != null && cooldown > 0) {
                forglory_cooldowns.put(tier, cooldown - 1);
            }
        }
    }

    @Override
    public boolean hasAFeat() {
        return forglory_has_a_feat;
    }
}
