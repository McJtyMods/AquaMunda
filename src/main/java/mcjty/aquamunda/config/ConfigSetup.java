package mcjty.aquamunda.config;

import mcjty.aquamunda.AquaMunda;
import mcjty.lib.thirteen.ConfigSpec;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.io.File;

public class ConfigSetup {

    private static Configuration mainConfig;
    private static Configuration recipesConfig;

    private static final ConfigSpec.Builder SERVER_BUILDER = new ConfigSpec.Builder();
    private static final ConfigSpec.Builder CLIENT_BUILDER = new ConfigSpec.Builder();

    static {
        GeneralConfiguration.init(SERVER_BUILDER, CLIENT_BUILDER);
    }

    public static ConfigSpec SERVER_CONFIG;
    public static ConfigSpec CLIENT_CONFIG;

    public static void init() {
        mainConfig = new Configuration(new File(AquaMunda.setup.getModConfigDir().getPath(), "aquamunda.cfg"));
        recipesConfig = new Configuration(new File(AquaMunda.setup.getModConfigDir().getPath(), "aquamunda_recipes.cfg"));

        Configuration cfg = mainConfig;
        try {
            cfg.load();
            SERVER_CONFIG = SERVER_BUILDER.build(cfg);
            CLIENT_CONFIG = CLIENT_BUILDER.build(cfg);
        } catch (Exception e1) {
            FMLLog.log(Level.ERROR, e1, "Problem loading config file!");
        } finally {
            if (mainConfig.hasChanged()) {
                mainConfig.save();
            }
        }
    }

    public static void readRecipesConfig() {
        Configuration cfg = recipesConfig;
        try {
            cfg.load();
            cfg.addCustomCategoryComment(GeneralConfiguration.CATEGORY_RECIPES_CUTTINGBOARD, "Cuttingboard recipes");
            cfg.addCustomCategoryComment(GeneralConfiguration.CATEGORY_RECIPES_COOKER, "Cooker recipes");
            cfg.addCustomCategoryComment(GeneralConfiguration.CATEGORY_RECIPES_GRINDSTONE, "Grindstone recipes");

            GeneralConfiguration.initCookingBoardRecipes(cfg);
            GeneralConfiguration.initCookerRecipes(cfg);
            GeneralConfiguration.initGrindstoneRecipes(cfg);
        } catch (Exception e1) {
            FMLLog.log(Level.ERROR, e1, "Problem loading config file!");
        } finally {
            if (mainConfig.hasChanged()) {
                mainConfig.save();
            }
        }
    }

    public static void postInit() {
        if (mainConfig.hasChanged()) {
            mainConfig.save();
        }

        if (recipesConfig.hasChanged()) {
            recipesConfig.save();
        }
    }

}
