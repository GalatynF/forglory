package com.github.galatynf.forglory.items.misc;

import com.github.galatynf.forglory.enumFeat.Feats;
import com.github.galatynf.forglory.init.GemsInit;
import com.github.galatynf.forglory.items.PoweredGem;

public class ArrowComboGem extends PoweredGem {
    public ArrowComboGem(Settings settings) {
        super(settings);
        feat = Feats.ARROW_COMBO;
        gem = GemsInit.miscGem;
    }
}
