package mcjty.aquamunda.blocks.grindstone;

import mcjty.aquamunda.blocks.generic.GenericInventoryTE;
import mcjty.aquamunda.config.GeneralConfiguration;
import mcjty.aquamunda.items.ModItems;
import mcjty.aquamunda.sound.SoundController;
import mcjty.immcraft.api.handles.IInterfaceHandle;
import mcjty.immcraft.api.handles.InputInterfaceHandle;
import mcjty.immcraft.api.handles.OutputInterfaceHandle;
import mcjty.immcraft.api.helpers.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.Random;

public class GrindStoneTE extends GenericInventoryTE implements ITickable {

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int MAX_GRIND_COUNTER = 100;

    private int grindCounter = -1;
    private int maxGrindCounter = 0;
    private int counter = 0;

    public GrindStoneTE() {
        super(2);

        addInterfaceHandle(new InputInterfaceHandle("input").slot(SLOT_INPUT).scale(.60f));
        addInterfaceHandle(new OutputInterfaceHandle("output").slot(SLOT_OUTPUT).scale(.80f));
    }

    public void grind(EntityPlayer player) {
        ItemStack outputItem = new ItemStack(ModItems.flour);
        if (!getStackInSlot(SLOT_OUTPUT).isEmpty() && !ItemStack.areItemStackTagsEqual(outputItem, getStackInSlot(SLOT_OUTPUT))) {
            ITextComponent component = new TextComponentString(TextFormatting.YELLOW + "Clean up the grinder first!");
            if (player instanceof EntityPlayer) {
                ((EntityPlayer) player).sendStatusMessage(component, false);
            } else {
                player.sendMessage(component);
            }
        } else {
            grindCounter = 0;
            maxGrindCounter = MAX_GRIND_COUNTER;
            markDirtyClient();
        }
    }

    private void grind() {
        if (grindCounter >= 0) {
            grindCounter++;
            if (grindCounter >= maxGrindCounter) {
                grindCounter = -1;
                maxGrindCounter = 0;
                ItemStack input = getStackInSlot(SLOT_INPUT);
                if (getStackInSlot(SLOT_OUTPUT).isEmpty() || ItemStack.areItemStackTagsEqual(new ItemStack(ModItems.flour), getStackInSlot(SLOT_OUTPUT))) {
                    if (!input.isEmpty() && input.getItem() == Items.WHEAT) {
                        int amount = -1;
                        input.grow(amount);
                        input = input;
                        setInventorySlotContents(SLOT_INPUT, input);
                        if (getStackInSlot(SLOT_OUTPUT).isEmpty()) {
                            setInventorySlotContents(SLOT_OUTPUT, new ItemStack(ModItems.flour));
                        } else {
                            ItemStack stack = getStackInSlot(SLOT_OUTPUT);
                            stack.grow(1);
                        }
                    }
                }
                markDirtyClient();
            }
        }
    }

    public int getGrindCounter() {
        return grindCounter;
    }

    public int getMaxGrindCounter() {
        return maxGrindCounter;
    }

    @Override
    public void update() {
        if (!getWorld().isRemote) {
            grind();
            markDirty();
        } else {
            if (grindCounter >= 0) {
                startGrindingSound();
                grindCounter++;     // Update grindCounter client side too
            } else {
                SoundController.stopSound(getWorld(), getPos());
            }
        }
    }

    public void startGrindingSound() {
        if (GeneralConfiguration.baseChoppingVolume > 0.01f) {
            if (!SoundController.isGrindstonePlaying(getWorld(), pos)) {
                SoundController.playGrindstone(getWorld(), getPos(), 1.0f);
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
        IInterfaceHandle handle = getHandle(player);
        if (handle == null && !getWorld().isRemote) {
            grind(player);
            return true;
        }
        return super.onActivate(player);
    }

    private static Random random = new Random();

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        counter = tagCompound.getInteger("counter");
        grindCounter = tagCompound.getInteger("grindCounter");
        maxGrindCounter = tagCompound.getInteger("maxGrindCounter");
    }

    @Override
    public void writeToNBT(NBTHelper helper) {
        super.writeToNBT(helper);
        helper
                .set("counter", counter)
                .set("grindCounter", grindCounter)
                .set("maxGrindCounter", maxGrindCounter);
    }
}
