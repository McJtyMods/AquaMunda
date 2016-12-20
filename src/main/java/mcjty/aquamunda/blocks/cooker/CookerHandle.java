package mcjty.aquamunda.blocks.cooker;

import mcjty.aquamunda.items.ModItems;
import mcjty.immcraft.api.handles.DefaultInterfaceHandle;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;

class CookerHandle extends DefaultInterfaceHandle<DefaultInterfaceHandle> {

    private final CookerTE cookerTE;

    public CookerHandle(CookerTE cookerTE) {
        this.cookerTE = cookerTE;
    }

    @Override
    public int getInsertAmount(boolean sneak) {
        return 1;
    }

    @Override
    public Vec3d getRenderOffset() {
        long t = System.currentTimeMillis();
        float offset = (t % 3000) / 3000.0f;
        if (offset > .5) {
            offset = 1.0f - offset;
        }
        float dy = cookerTE.getContentsHeight() - .6f;
        return super.getRenderOffset().addVector(0, (offset - 0.5f) / 20.0f + dy, 0);
    }

    @Override
    public ItemStack extractOutput(TileEntity genericTE, EntityPlayer player, int amount) {
        ItemStack stack = super.extractOutput(genericTE, player, amount);
        if (isItemThatNeedsExtractionItem(stack)) {
            stack = new ItemStack(ModItems.dish, ItemStackTools.getStackSize(stack), 0);
        }
        return stack;
    }

    @Override
    public boolean acceptAsInput(ItemStack stack) {
        return CookerTE.getRecipe(stack) != null;
    }

    @Override
    public boolean isOutputWithItem() {
        return true;
    }

    @Override
    public boolean isItemThatNeedsExtractionItem(ItemStack item) {
        if (ItemStackTools.isValid(item)) {
            return item.getItem() instanceof ICookerResult;
        }
        return false;
    }

    @Override
    public boolean isSuitableExtractionItem(ItemStack item) {
        if (ItemStackTools.isValid(item)) {
            return item.getItem() == Items.BOWL;
        }
        return false;
    }

    @Override
    public String getExtractionMessage() {
        return TextFormatting.YELLOW + "You need a bowl to get the food out";
    }
}
