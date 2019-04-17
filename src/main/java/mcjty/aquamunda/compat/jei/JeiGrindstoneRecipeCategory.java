package mcjty.aquamunda.compat.jei;

import mcjty.aquamunda.AquaMunda;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class JeiGrindstoneRecipeCategory implements IRecipeCategory<JeiGrindstoneRecipeWrapper> {

    private final IDrawable background;

    public JeiGrindstoneRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation(AquaMunda.MODID, "textures/gui/recipe_backgrounds.png");
        background = guiHelper.createDrawable(location, 0, 80, 160, 80);
    }

    @Nonnull
    @Override
    public String getUid() {
        return JeiPlugin.GRINDSTONE_ID;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return "Grindstone";
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
    public void setRecipe(IRecipeLayout recipeLayout, JeiGrindstoneRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 78, 32);
        guiItemStacks.init(1, false, 129, 32);

        List<ItemStack> inputs = ingredients.getInputs(VanillaTypes.ITEM).get(0);
        List<ItemStack> outputs = ingredients.getOutputs(VanillaTypes.ITEM).get(0);

        guiItemStacks.set(0, inputs);
        guiItemStacks.set(1, outputs);
    }

    @Override
    public String getModName() {
        return AquaMunda.MODNAME;
    }
}
