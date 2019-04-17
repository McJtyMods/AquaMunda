package mcjty.aquamunda.compat.jei;

import mcjty.aquamunda.recipes.CuttingBoardRecipe;

public class JeiCuttingBoardRecipe {

    private final CuttingBoardRecipe recipe;

    public JeiCuttingBoardRecipe(CuttingBoardRecipe recipe) {
        this.recipe = recipe;
    }

    public CuttingBoardRecipe getRecipe() {
        return recipe;
    }
}
