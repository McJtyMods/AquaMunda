package mcjty.aquamunda.config;

import mcjty.lib.thirteen.ConfigSpec;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;

import java.io.File;

public class ConfigSetup {

    public static File modConfigDir;
    private static Configuration mainConfig;
    private static Configuration recipesConfig;

    private static final ConfigSpec.Builder SERVER_BUILDER = new ConfigSpec.Builder();
    private static final ConfigSpec.Builder CLIENT_BUILDER = new ConfigSpec.Builder();

    static {
        GeneralConfiguration.init(SERVER_BUILDER, CLIENT_BUILDER);
    }

    public static ConfigSpec SERVER_CONFIG;
    public static ConfigSpec CLIENT_CONFIG;

    public static void preInit(FMLPreInitializationEvent e) {
        modConfigDir = e.getModConfigurationDirectory();

        mainConfig = new Configuration(new File(modConfigDir.getPath(), "aquamunda.cfg"));
        recipesConfig = new Configuration(new File(modConfigDir.getPath(), "aquamunda_recipes.cfg"));

        readMainConfig();
    }

    private static void readMainConfig() {
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
        mainConfig = null;

        if (recipesConfig.hasChanged()) {
            recipesConfig.save();
        }
        recipesConfig = null;
    }

}
