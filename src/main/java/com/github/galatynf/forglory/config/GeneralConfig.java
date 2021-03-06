package com.github.galatynf.forglory.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "general")
public class GeneralConfig implements ConfigData {
    @ConfigEntry.Gui.RequiresRestart
    @ConfigEntry.BoundedDiscrete(min = 16, max = 40)
    public int struct_min_distance = 32;

    @ConfigEntry.Gui.RequiresRestart
    @ConfigEntry.BoundedDiscrete(min = 40, max = 64)
    public int struct_max_distance = 48;
}
