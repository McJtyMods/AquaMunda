package mcjty.aquamunda.compat.jei;

import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.recipes.*;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JEIPlugin
public class JeiPlugin implements IModPlugin {

    public static final String GRINDSTONE_ID = "AquaMunda.Grindstone";
    public static final String COOKER_ID = "AquaMunda.Cooker";
    public static final String CUTTINGBOARD_ID = "AquaMunda.CuttingBoard";

    @Override
    public void register(@Nonnull IModRegistry registry) {
        registerGrindstoneHandling(registry);
        registerCookerHandling(registry);
        registerCuttingBoardHandling(registry);
    }

    private void registerCookerHandling(@Nonnull IModRegistry registry) {
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.cookerBlock), COOKER_ID);

        List<JeiCookerRecipe> cookerRecipes = new ArrayList<>();
        for (Map.Entry<ResourceLocation, CookerRecipe> entry : CookerRecipeRepository.getRecipeMap().entrySet()) {
            cookerRecipes.add(new JeiCookerRecipe(entry.getValue()));
        }
        registry.addRecipes(cookerRecipes, COOKER_ID);
        registry.handleRecipes(JeiCookerRecipe.class, JeiCookerRecipeWrapper::new, COOKER_ID);
    }

    private void registerGrindstoneHandling(@Nonnull IModRegistry registry) {
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.grindStoneBlock), GRINDSTONE_ID);

        List<JeiGrindstoneRecipe> grindstoneRecipes = new ArrayList<>();
        for (Map.Entry<ResourceLocation, GrindstoneRecipe> entry : GrindstoneRecipeRepository.getRecipeMap().entrySet()) {
            grindstoneRecipes.add(new JeiGrindstoneRecipe(entry.getValue()));
        }
        registry.addRecipes(grindstoneRecipes, GRINDSTONE_ID);
        registry.handleRecipes(JeiGrindstoneRecipe.class, JeiGrindstoneRecipeWrapper::new, GRINDSTONE_ID);
    }

    private void registerCuttingBoardHandling(@Nonnull IModRegistry registry) {
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.cuttingBoardBlock), CUTTINGBOARD_ID);

        List<JeiCuttingBoardRecipe> cuttingBoardRecipes = new ArrayList<>();
        for (Map.Entry<KeyResourceLocations, CuttingBoardRecipe> entry : CuttingBoardRecipeRepository.getRecipeMap().entrySet()) {
            cuttingBoardRecipes.add(new JeiCuttingBoardRecipe(entry.getValue()));
        }
        registry.addRecipes(cuttingBoardRecipes, CUTTINGBOARD_ID);
        registry.handleRecipes(JeiCuttingBoardRecipe.class, JeiCuttingBoardRecipeWrapper::new, CUTTINGBOARD_ID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IJeiHelpers helpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = helpers.getGuiHelper();

        registry.addRecipeCategories(new JeiCookerRecipeCategory(guiHelper));
        registry.addRecipeCategories(new JeiGrindstoneRecipeCategory(guiHelper));
        registry.addRecipeCategories(new JeiCuttingBoardRecipeCategory(guiHelper));
    }

}
