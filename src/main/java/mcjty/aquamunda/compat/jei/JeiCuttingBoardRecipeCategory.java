package mcjty.aquamunda.compat.jei;

import mcjty.aquamunda.AquaMunda;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class JeiCuttingBoardRecipeCategory implements IRecipeCategory<JeiCuttingBoardRecipeWrapper> {

    private final IDrawable background;

    public JeiCuttingBoardRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation(AquaMunda.MODID, "textures/gui/recipe_backgrounds.png");
        background = guiHelper.createDrawable(location, 0, 160, 160, 80);
    }

    @Nonnull
    @Override
    public String getUid() {
        return JeiPlugin.CUTTINGBOARD_ID;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return "CuttingBoard";
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, JeiCuttingBoardRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 78, 32-18);
        guiItemStacks.init(1, true, 78, 32);
        guiItemStacks.init(2, true, 78, 32+18);
        guiItemStacks.init(3, false, 129, 32);

        List<ItemStack> inputs0 = ingredients.getInputs(VanillaTypes.ITEM).get(0);
        List<ItemStack> inputs1 = ingredients.getInputs(VanillaTypes.ITEM).get(1);
        List<ItemStack> inputs2 = ingredients.getInputs(VanillaTypes.ITEM).get(2);
        List<ItemStack> outputs = ingredients.getOutputs(VanillaTypes.ITEM).get(0);

        guiItemStacks.set(0, inputs0);
        guiItemStacks.set(1, inputs1);
        guiItemStacks.set(2, inputs2);
        guiItemStacks.set(3, outputs);
    }

    @Override
    public String getModName() {
        return AquaMunda.MODNAME;
    }
}
