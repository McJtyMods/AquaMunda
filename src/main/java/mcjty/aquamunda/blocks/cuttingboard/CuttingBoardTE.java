package mcjty.aquamunda.blocks.cuttingboard;

import mcjty.aquamunda.blocks.generic.GenericInventoryTE;
import mcjty.aquamunda.config.GeneralConfiguration;
import mcjty.aquamunda.items.ModItems;
import mcjty.aquamunda.recipes.CuttingBoardRecipe;
import mcjty.aquamunda.recipes.CuttingBoardRecipeRepository;
import mcjty.aquamunda.sound.SoundController;
import mcjty.immcraft.api.handles.InputInterfaceHandle;
import mcjty.immcraft.api.handles.OutputInterfaceHandle;
import mcjty.immcraft.api.helpers.NBTHelper;
import mcjty.lib.tools.ChatTools;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CuttingBoardTE extends GenericInventoryTE implements ITickable {

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 3;

    public static final int TICKS_PER_OPERATION = 20;

    private int chopCounter = -1;
    private int maxChopCounter = 0;
    private int counter = 0;
    private boolean kneading = false;

    public CuttingBoardTE() {
        super(4);

        addInterfaceHandle(new InputInterfaceHandle(CuttingBoardBlock.SEL_INPUT1).slot(SLOT_INPUT).scale(.60f));
        addInterfaceHandle(new InputInterfaceHandle(CuttingBoardBlock.SEL_INPUT2).slot(SLOT_INPUT+1).scale(.60f));
        addInterfaceHandle(new InputInterfaceHandle(CuttingBoardBlock.SEL_INPUT3).slot(SLOT_INPUT+2).scale(.60f));
        addInterfaceHandle(new OutputInterfaceHandle(CuttingBoardBlock.SEL_OUTPUT).slot(SLOT_OUTPUT).scale(.80f));
    }

    public boolean kneadDough(EntityPlayer player) {
        CuttingBoardRecipe recipe = getCurrentRecipe();
        if (recipe == null) {
            ChatTools.addChatMessage(player, new TextComponentString(TextFormatting.YELLOW + "Nothing to knead here"));
            return false;
        }
        if (!recipe.isUseRoller()) {
            ChatTools.addChatMessage(player, new TextComponentString(TextFormatting.YELLOW + "You can't knead those ingredients"));
            return false;
        }

        return startProcessing(player, recipe);
    }

    public void chopChop(EntityPlayer player) {
        CuttingBoardRecipe recipe = getCurrentRecipe();
        if (recipe == null) {
            ChatTools.addChatMessage(player, new TextComponentString(TextFormatting.YELLOW + "You can't find anything useful to do with these ingredients"));
            return;
        }
        if (recipe.isUseRoller()) {
            ChatTools.addChatMessage(player, new TextComponentString(TextFormatting.YELLOW + "Using a knife on these ingredients wouldn't work very well"));
            return;
        }

        startProcessing(player, recipe);
    }

    private boolean startProcessing(EntityPlayer player, CuttingBoardRecipe recipe) {
        maxChopCounter = recipe.getChopTime();
        ItemStack outputItem = recipe.getOutputItem();
        if (ItemStackTools.isValid(getStackInSlot(SLOT_OUTPUT)) && !ItemStack.areItemStackTagsEqual(outputItem, getStackInSlot(SLOT_OUTPUT))) {
            ChatTools.addChatMessage(player, new TextComponentString(TextFormatting.YELLOW + "Clean up the board first!"));
            return false;
        } else {
            chopCounter = 0;
            kneading = recipe.isUseRoller();
            markDirtyClient();
        }
        return true;
    }

    private CuttingBoardRecipe getCurrentRecipe() {
        return CuttingBoardRecipeRepository.getRecipe(
                    getStackInSlot(SLOT_INPUT),
                    getStackInSlot(SLOT_INPUT+1),
                    getStackInSlot(SLOT_INPUT+2));
    }

    private void chop() {
        if (chopCounter >= 0) {
            chopCounter++;
            if (chopCounter >= maxChopCounter) {
                chopCounter = -1;
                maxChopCounter = 0;
                CuttingBoardRecipe recipe = getCurrentRecipe();
                if (recipe != null) {
                    ItemStack output = recipe.getOutputItem().copy();
                    if (ItemStackTools.isValid(getStackInSlot(SLOT_INPUT))) {
                        ItemStackTools.incStackSize(getStackInSlot(SLOT_INPUT), -1);
                    }
                    if (ItemStackTools.isValid(getStackInSlot(SLOT_INPUT+1))) {
                        ItemStackTools.incStackSize(getStackInSlot(SLOT_INPUT+1), -1);
                    }
                    if (ItemStackTools.isValid(getStackInSlot(SLOT_INPUT+2))) {
                        ItemStackTools.incStackSize(getStackInSlot(SLOT_INPUT+2), -1);
                    }
                    if (ItemStackTools.isEmpty(getStackInSlot(SLOT_OUTPUT))) {
                        setInventorySlotContents(SLOT_OUTPUT, output);
                    } else {
                        ItemStackTools.incStackSize(getStackInSlot(SLOT_OUTPUT), 1);
                    }
                    markDirtyClient();
                }
            }
        }
    }

    public int getChopCounter() {
        return chopCounter;
    }

    public int getMaxChopCounter() {
        return maxChopCounter;
    }

    @Override
    public void update() {
        if (!getWorld().isRemote) {

            counter--;
            if (counter <= 0) {
                counter = TICKS_PER_OPERATION;

                chop();
            }
            markDirty();
        } else {
            if (!kneading) {
                if (chopCounter >= 0) {
                    startChoppingSound();
                } else {
                    SoundController.stopSound(getWorld(), getPos());
                }
            }
        }
    }

    public void startChoppingSound() {
        if (GeneralConfiguration.baseChoppingVolume > 0.01f) {
            if (!SoundController.isChoppingPlaying(getWorld(), pos)) {
                SoundController.playChopping(getWorld(), getPos(), 1.0f);
            }
        } else {
            SoundController.stopSound(getWorld(), getPos());
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (getWorld().isRemote) {
            SoundController.stopSound(getWorld(), getPos());
        }
    }

    @Override
    public boolean onActivate(EntityPlayer player) {
        if (ItemStackTools.isValid(player.getHeldItem(EnumHand.MAIN_HAND)) && player.getHeldItem(EnumHand.MAIN_HAND).getItem() == ModItems.kitchenKnife) {
            chopChop(player);
            return true;
        } else if (ItemStackTools.isValid(player.getHeldItem(EnumHand.MAIN_HAND)) && player.getHeldItem(EnumHand.MAIN_HAND).getItem() == ModItems.doughRoller) {
            kneadDough(player);
            return true;
        } else {
            return super.onActivate(player);
        }
    }

    @Override
    public boolean isUsable(EntityPlayer player) {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        counter = tagCompound.getInteger("counter");
        chopCounter = tagCompound.getInteger("chopCounter");
        maxChopCounter = tagCompound.getInteger("maxChopCounter");
        kneading = tagCompound.getBoolean("kneading");
    }

    @Override
    public void writeToNBT(NBTHelper helper) {
        super.writeToNBT(helper);
        helper
                .set("counter", counter)
                .set("chopCounter", chopCounter)
                .set("maxChopCounter", maxChopCounter)
                .set("kneading", kneading);
    }
}
