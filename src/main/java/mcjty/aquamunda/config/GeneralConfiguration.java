package mcjty.aquamunda.config;

import mcjty.aquamunda.environment.FarmlandOverhaulType;
import net.minecraftforge.common.config.Configuration;

public class GeneralConfiguration {
    public static final String CATEGORY_GENERAL = "general";

    public static float baseCookerVolume = 0.6f;     // Use 0 to turn off
    public static float baseChoppingVolume = 1.0f;   // Use 0 to turn off
    public static float baseGrindstoneVolume = 0.6f; // Use 0 to turn off

    public static FarmlandOverhaulType farmlandOverhaulType = FarmlandOverhaulType.FRESH;

    public static void init(Configuration cfg) {
        baseCookerVolume = (float) cfg.get(CATEGORY_GENERAL, "baseCookerVolume", baseCookerVolume,
                "The volume for the cooker sound (0.0 is off)").getDouble();
        baseChoppingVolume = (float) cfg.get(CATEGORY_GENERAL, "baseChoppingVolume", baseChoppingVolume,
                "The volume for the chopping sound (0.0 is off)").getDouble();
        baseGrindstoneVolume = (float) cfg.get(CATEGORY_GENERAL, "baseGrindstoneVolume", baseGrindstoneVolume,
                "The volume for the grindstone sound (0.0 is off)").getDouble();

        String overhaul = cfg.get(CATEGORY_GENERAL, "farmlandOverhaulType", GeneralConfiguration.farmlandOverhaulType.getName(), "The type of overhaul for farmland: 'none' means vanilla water will work, 'fresh' means fresh water is required, 'harsh' means fresh water is required and the farmland must be sprinkled").getString();
        farmlandOverhaulType = FarmlandOverhaulType.getByName(overhaul);
        if (farmlandOverhaulType == null) {
            farmlandOverhaulType = FarmlandOverhaulType.FRESH;
        }
    }
}
