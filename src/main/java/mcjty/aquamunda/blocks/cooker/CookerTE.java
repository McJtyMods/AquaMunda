package mcjty.aquamunda.blocks.cooker;

import mcjty.aquamunda.api.IHoseConnector;
import mcjty.aquamunda.blocks.generic.GenericInventoryTE;
import mcjty.aquamunda.config.GeneralConfiguration;
import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.aquamunda.compat.immcraft.ImmersiveCraftHandler;
import mcjty.aquamunda.recipes.CookerRecipe;
import mcjty.aquamunda.recipes.CookerRecipeRepository;
import mcjty.aquamunda.sound.SoundController;
import mcjty.aquamunda.varia.BlockTools;
import mcjty.immcraft.api.cable.ICableSubType;
import mcjty.immcraft.api.cable.ICableType;
import mcjty.immcraft.api.helpers.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.Fluid;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

public class CookerTE extends GenericInventoryTE implements IHoseConnector, ITickable {

    public static final int INPUT_PER_TICK = 3;
    public static final int MAX_AMOUNT = 2000;
    public static final int TICKS_PER_OPERATION = 20;

    public static final int SLOT_INPUT = 0;

    private int amount = 0;
    private float temperature = 20;
    private int counter = 0;
    private int maxCookTime = 0;
    private int cookTime = -1;

    // If the cooker contains soup this will be the tag name of that dish
    private String soup = "";

    public CookerTE() {
        super(1);

        addInterfaceHandle(new CookerHandle(this, "slot").slot(SLOT_INPUT).scale(.80f));
    }

    private Set<EnumFacing> connections = EnumSet.noneOf(EnumFacing.class);

    @Override
    public ICableType getType() {
        return ImmersiveCraftHandler.liquidType;
    }

    @Override
    public boolean canConnect(EnumFacing blockSide) {
        if (blockSide == EnumFacing.UP || blockSide == EnumFacing.DOWN) {
            return false;
        }
        return !connections.contains(blockSide);
    }

    @Override
    public int connect(EnumFacing blockSide, int networkId, ICableSubType subType) {
        markDirty();
        if (!connections.contains(blockSide)) {
            connections.add(blockSide);
            return blockSide.ordinal();
        }
        return -1;
    }

    @Override
    public Vec3d getConnectorLocation(int connectorId, EnumFacing rotation) {
        EnumFacing side = EnumFacing.values()[connectorId];
        int xCoord = getPos().getX();
        int yCoord = getPos().getY();
        int zCoord = getPos().getZ();
        switch (side) {
            case DOWN:
                return new Vec3d(xCoord+.5f, yCoord, zCoord+.5f);
            case UP:
                return new Vec3d(xCoord+.5f, yCoord+1, zCoord+.5f);
            case NORTH:
                return new Vec3d(xCoord+.5f, yCoord+.1f, zCoord);
            case SOUTH:
                return new Vec3d(xCoord+.5f, yCoord+.1f, zCoord+1);
            case WEST:
                return new Vec3d(xCoord, yCoord+.1f, zCoord+.5f);
            case EAST:
                return new Vec3d(xCoord+1, yCoord+.1f, zCoord+.5f);
            default:
                return new Vec3d(xCoord, yCoord, zCoord);
        }
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        int oldAmount = this.amount;
        this.amount = amount;

        if (amount > oldAmount) {
            // There is more water, drop temperature. New water is assumed to be at 20.
            float newtemp = (temperature * oldAmount + 20.0f * (amount - oldAmount)) / amount;
            changeTemperature(newtemp);
        }

        markDirtyClient();
    }

    public float getTemperature() {
        return temperature;
    }

    @Override
    public void disconnect(int connectorId) {
        EnumFacing side = EnumFacing.values()[connectorId];
        connections.remove(side);
        markDirty();
    }

    @Override
    public int extract(int amount) {
        return 0;
    }

    @Override
    public Fluid getSupportedFluid() {
        return FluidSetup.freshWater;
    }

    @Override
    public int getMaxExtractPerTick() {
        return 0;
    }

    @Override
    public int getMaxInsertPerTick() {
        return INPUT_PER_TICK;
    }

    @Override
    public int getEmptyLiquidLeft() {
        return MAX_AMOUNT - amount;
    }

    @Override
    public int insert(Fluid fluid, int a) {
        int inserted = Math.min(MAX_AMOUNT - amount, a);
        amount += inserted;
        return inserted;
    }

    @Override
    public float getFilledPercentage() {
        return 100.0f * amount / MAX_AMOUNT;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        EnumContents oldContents = getContentsState();
        EnumLiquid oldLiquid = getLiquidState();

        super.onDataPacket(net, packet);
        if (getWorld().isRemote) {
            // If needed send a render update.
            EnumContents newContents = getContentsState();
            EnumLiquid newLiquid = getLiquidState();
            if ((!newLiquid.equals(oldLiquid)) || !newContents.equals(oldContents)) {
                getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
            }
        }
    }

    public int getMaxCookTime() {
        return maxCookTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getBoilingState() {
        if (temperature < 70) {
            return 0;
        } else if (temperature < 80) {
            return 1;
        } else if (temperature < 90) {
            return 2;
        } else if (temperature < 100) {
            return (int) ((temperature-90.0) * 8.0f / 10.0f + 3.0);
        }
        return 10;
    }

    public float getContentsHeight() {
        switch (getContentsState()) {
            case EMPTY:
                return 0.1f;
            case LOW:
                return 0.4f * 0.6f;
            case MEDIUM:
                return 0.7f * 0.6f;
            case FULL:
                return 1.0f * 0.6f;
        }
        return 0.0f;
    }

    public String getSoup() {
        return soup;
    }

    public void setSoup(String soup) {
        this.soup = soup;
        markDirtyClient();
    }

    public EnumLiquid getLiquidState() {
        if (soup.isEmpty()) {
            return EnumLiquid.WATER;
        } else {
            return EnumLiquid.SOUP;
        }
    }

    public EnumContents getContentsState() {
        float filled = getFilledPercentage();
        if (filled < 1) {
            return EnumContents.EMPTY;
        } else if (filled < 40) {
            return EnumContents.LOW;
        } else if (filled < 80) {
            return EnumContents.MEDIUM;
        } else {
            return EnumContents.FULL;
        }
    }

    private void changeTemperature(float newtemp) {
        if (newtemp > 100) {
            newtemp = 100;
        }
        if (temperature == newtemp) {
            return;
        }
        int oldBoiling = getBoilingState();
        temperature = newtemp;
        int newBoiling = getBoilingState();
        if (oldBoiling != newBoiling) {
            markDirtyClient();
        } else {
            markDirty();
        }
    }


    private int calculateMaxCookTime(ItemStack stack) {
        CookerRecipe recipe = CookerRecipeRepository.getRecipe(stack);
        if (recipe != null) {
            return (int) (recipe.getCookTime() * (0.5f + (stack.getCount() +3.0f) / 8.0f));
        }
        return 0;
    }

    @Override
    public void update() {
        if (!getWorld().isRemote) {

            if (amount <= 0) {
                coolEmpty();
                return;
            }

            counter--;
            if (counter <= 0) {
                counter = TICKS_PER_OPERATION;

                fixCookTime();
                changeTemperatureBasedOnHeat();
                if (temperature > 99) {
                    boil();
                }
            }
            markDirty();
        } else {
            updateBoilingSound();
        }
    }

    private void fixCookTime() {
        int mc = calculateMaxCookTime(getStackInSlot(SLOT_INPUT));
        if (mc != maxCookTime) {
            maxCookTime = mc;
            if (cookTime > maxCookTime) {
                cookTime = maxCookTime;
            }
        }
    }

    private void coolEmpty() {
        // We have no liquid so we cool
        if (temperature > 20) {
            changeTemperature(temperature-1);
            markDirty();
        }
    }

    private void updateBoilingSound() {
        if (GeneralConfiguration.baseCookerVolume.get() > 0.01f) {
            int boilingState = getBoilingState();
            if (boilingState >= 1) {
                float vol = (boilingState-1.0f)/9.0f;
                if (!SoundController.isBoilingPlaying(getWorld(), pos)) {
                    SoundController.playBoiling(getWorld(), getPos(), vol);
                } else {
                    SoundController.updateVolume(getWorld(), getPos(), vol);
                }
            } else {
                SoundController.stopSound(getWorld(), getPos());
            }
        }
    }

    private void boil() {
        if (amount > 0) {
            // Evaporation
            setAmount(amount-1);
        }

        if (cookTime == -1) {
            return;
        }
        cookTime++;
        if (cookTime >= maxCookTime) {
            cookTime = -1;
            maxCookTime = 0;
            CookerRecipe recipe = CookerRecipeRepository.getRecipe(getStackInSlot(SLOT_INPUT));
            if (recipe != null) {
                if (!recipe.getOutputSoup().isEmpty()) {
                    setInventorySlotContents(SLOT_INPUT, ItemStack.EMPTY);
                    soup = recipe.getOutputSoup();
                } else {
                    ItemStack output = recipe.getOutputItem().copy();
                    int amount1 = getStackInSlot(SLOT_INPUT).getCount();
                    if (amount1 <= 0) {
                        output.setCount(0);
                    } else {
                        output.setCount(amount1);
                    }
                    setInventorySlotContents(SLOT_INPUT, output);
                }
                markDirtyClient();
            }
        }
    }

    private void changeTemperatureBasedOnHeat() {
        if (isHot()) {
            if (temperature < 100) {
                changeTemperature(temperature + (125.0f - getFilledPercentage()) / 100.0f);
            }
        } else {
            if (temperature > 20) {
                changeTemperature(temperature - (125.0f - getFilledPercentage()) / 100.0f);
            }
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
    public void setInventorySlotContents(int index, ItemStack stack) {
        CookerRecipe recipe = CookerRecipeRepository.getRecipe(stack);
        if (recipe != null) {
            maxCookTime = calculateMaxCookTime(stack);
            cookTime = 0;
            markDirty();
        }
        super.setInventorySlotContents(index, stack);
    }

    private boolean isHot() {
        return BlockTools.isHot(getWorld(), getPos().down());
    }

    private static Random random = new Random();

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        amount = tagCompound.getInteger("amount");
        temperature = tagCompound.getFloat("temperature");
        counter = tagCompound.getInteger("counter");
        cookTime = tagCompound.getInteger("cookTime");
        maxCookTime = tagCompound.getInteger("maxCookTime");
        soup = tagCompound.getString("soup");
        connections.clear();
        for (EnumFacing direction : EnumFacing.VALUES) {
            if (tagCompound.hasKey("c" + direction.ordinal())) {
                connections.add(direction);
            }
        }
    }

    @Override
    public void writeToNBT(NBTHelper helper) {
        super.writeToNBT(helper);
        helper
                .set("amount", amount)
                .set("temperature", temperature)
                .set("counter", counter)
                .set("cookTime", cookTime)
                .set("maxCookTime", maxCookTime)
                .set("soup", soup);
        for (EnumFacing direction : EnumFacing.VALUES) {
            if (connections.contains(direction)) {
                helper.set("c" + direction.ordinal(), true);
            }
        }
    }

}
