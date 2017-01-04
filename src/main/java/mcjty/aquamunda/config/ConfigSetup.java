package mcjty.aquamunda.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;

import java.io.File;

public class ConfigSetup {

    public static File modConfigDir;
    private static Configuration mainConfig;
    private static Configuration recipesConfig;

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
            cfg.addCustomCategoryComment(GeneralConfiguration.CATEGORY_GENERAL, "General settings");

            GeneralConfiguration.init(cfg);
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

            GeneralConfiguration.initCookingBoardRecipes(cfg);
            GeneralConfiguration.initCookerRecipes(cfg);
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
