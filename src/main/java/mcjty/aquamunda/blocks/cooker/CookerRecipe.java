package mcjty.aquamunda.blocks.cooker;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CookerRecipe {

    private final ItemStack inputItem;
    private final ItemStack outputItem;
    private final int cookTime;

    public CookerRecipe(ItemStack inputItem, ItemStack outputItem, int cookTime) {
        this.inputItem = inputItem;
        this.outputItem = outputItem;
        this.cookTime = cookTime;
    }

    public CookerRecipe(Item inputItem, Item outputItem, int cookTime) {
        this.inputItem = new ItemStack(inputItem);
        this.outputItem = new ItemStack(outputItem);
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
}
