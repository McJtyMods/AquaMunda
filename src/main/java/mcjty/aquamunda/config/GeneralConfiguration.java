package mcjty.aquamunda.config;

import net.minecraftforge.common.config.Configuration;

public class GeneralConfiguration {
    public static final String CATEGORY_GENERAL = "general";

    public static float baseCookerVolume = 1.0f;     // Use 0 to turn off cooker sounds

    // @todo!!!!!
    public static void init(Configuration cfg) {
        baseCookerVolume = (float) cfg.get(CATEGORY_GENERAL, "baseCookerVolume", baseCookerVolume,
                "The volume for the cooker sound (1.0 is default, 0.0 is off)").getDouble();
    }
}
