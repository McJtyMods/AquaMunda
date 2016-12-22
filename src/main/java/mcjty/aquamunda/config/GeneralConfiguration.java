package mcjty.aquamunda.config;

import net.minecraftforge.common.config.Configuration;

public class GeneralConfiguration {
    public static final String CATEGORY_GENERAL = "general";

    public static float baseCookerVolume = 0.6f;     // Use 0 to turn off
    public static float baseChoppingVolume = 1.0f;   // Use 0 to turn off

    // @todo!!!!!
    public static void init(Configuration cfg) {
        baseCookerVolume = (float) cfg.get(CATEGORY_GENERAL, "baseCookerVolume", baseCookerVolume,
                "The volume for the cooker sound (0.0 is off)").getDouble();
        baseChoppingVolume = (float) cfg.get(CATEGORY_GENERAL, "baseChoppingVolume", baseChoppingVolume,
                "The volume for the chopping sound (0.0 is off)").getDouble();
    }
}
