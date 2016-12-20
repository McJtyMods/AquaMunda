package mcjty.aquamunda.blocks.cooker;

import mcjty.lib.tools.ItemStackTools;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class CookerRecipe {

    private final ItemStack inputItem;
    private final ItemStack outputItem;
    private final String outputSoup;
    private final int cookTime;

    public CookerRecipe(ItemStack inputItem, ItemStack outputItem, @Nonnull String outputSoup, int cookTime) {
        this.inputItem = inputItem;
        this.outputItem = outputItem;
        this.outputSoup = outputSoup;
        this.cookTime = cookTime;
    }

    public CookerRecipe(Item inputItem, Item outputItem, @Nonnull String outputSoup, int cookTime) {
        this.inputItem = new ItemStack(inputItem);
        this.outputItem = outputItem == null ? ItemStackTools.getEmptyStack() : new ItemStack(outputItem);
        this.outputSoup = outputSoup;
        this.cookTime = cookTime;
    }

    public ItemStack getInputItem() {
        return inputItem;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public int getCookTime() {
        return cookTime;
    }

    @Nonnull
    public String getOutputSoup() {
        return outputSoup;
    }
}
