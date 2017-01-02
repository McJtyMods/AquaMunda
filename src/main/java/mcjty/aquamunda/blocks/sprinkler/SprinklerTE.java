package mcjty.aquamunda.blocks.sprinkler;

import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.blocks.generic.GenericAMTE;
import mcjty.aquamunda.chunkdata.GameData;
import mcjty.aquamunda.environment.EnvironmentData;
import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.aquamunda.api.IHoseConnector;
import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import mcjty.immcraft.api.cable.ICableSubType;
import mcjty.immcraft.api.cable.ICableType;
import mcjty.immcraft.api.helpers.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.Fluid;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

public class SprinklerTE extends GenericAMTE implements IHoseConnector, ITickable {

    public static final int MAX_MOISTNESS = 5;
    public static final int SPRINKLER_COUNTER = 20;
    public static final int INPUT_PER_TICK = 3;
    public static final int MAX_AMOUNT = SPRINKLER_COUNTER * INPUT_PER_TICK;

    public int counter = SPRINKLER_COUNTER;

    private int amount = 0;

    private Set<EnumFacing> connections = EnumSet.noneOf(EnumFacing.class);

    @Override
    public ICableType getType() {
        return ImmersiveCraftHandler.liquidType;
    }

    @Override
    public boolean canConnect(EnumFacing blockSide) {
        if (blockSide == EnumFacing.UP) {
            return false;
        }
        return !connections.contains(blockSide);
    }

    @Override
    public int connect(EnumFacing blockSide, int networkId, ICableSubType subType) {
        markDirty();
        if (!connections.contains(blockSide)) {
            connections.add(blockSide);
            return blockSide.ordinal() * 4;
        }
        return -1;
    }

    @Override
    public Vec3d getConnectorLocation(int connectorId, EnumFacing rotation) {
        EnumFacing side = EnumFacing.values()[connectorId >> 2];
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

    @Override
    public void disconnect(int connectorId) {
        EnumFacing side = EnumFacing.values()[connectorId >> 2];
        connections.remove(side);
        markDirty();;
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
        return 0;
    }

    @Override
    public void update() {
        if (!getWorld().isRemote) {
            counter--;
            if (counter > 0) {
                return;
            }
            counter = SPRINKLER_COUNTER;
            // Only sprinkle if we have enough water
            if (amount >= MAX_AMOUNT) {
                sprinkle();
                amount = 0;
            }
        }
    }

    private void sprinkle() {
        EnvironmentData environment = EnvironmentData.getEnvironmentData(getWorld());
        GameData data = environment.getData();
        boolean dirty = false;
        int xCoord = getPos().getX();
        int yCoord = getPos().getY();
        int zCoord = getPos().getZ();
        for (int x = xCoord-4 ; x <= xCoord+4; x++) {
            for (int y = yCoord-1 ; y <= yCoord+2; y++) {
                for (int z = zCoord-4 ; z <= zCoord+4; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    IBlockState blockState = getWorld().getBlockState(pos);
                    Block block = blockState.getBlock();
                    if (!getWorld().isAirBlock(pos)) {
                        spawnParticles(EnumParticleTypes.WATER_SPLASH, 10, x, y+1, z);
                        // splash, dripWater
                    }

                    if (block == ModBlocks.customFarmLand) {
                        byte moistness = data.get(getWorld().provider.getDimension(), pos);
                        if (moistness < MAX_MOISTNESS) {
                            moistness++;
                            data.set(getWorld().provider.getDimension(), pos, moistness);
                            dirty = true;
                        }
                    } else if (block instanceof IGrowable) {
                        if (random.nextInt(2) == 0) {
                            block.updateTick(getWorld(), pos, blockState, random);
                        }
//                        if (random.nextInt(10) == 5) {
//                            ((IGrowable) block).grow(getWorld(), random, pos, blockState);
//                        }
                    }
                }
            }
        }
        if (dirty) {
            environment.save(getWorld());
        }
    }

    private static Random random = new Random();

    private void spawnParticles(EnumParticleTypes type, int amount, int x, int y, int z) {
        if (amount <= 0) {
            return;
        }
        float vecX = (random.nextFloat() - 0.5F) * 1.0F;
        float vecY = (random.nextFloat()) * 1.0F;
        float vecZ = (random.nextFloat() - 0.5F) * 1.0F;
        ((WorldServer) getWorld()).spawnParticle(type, x + 0.5f, y + 0.5f, z + 0.5f, amount, vecX, vecY, vecZ, 0.3f);
    }


    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        amount = tagCompound.getInteger("amount");
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
        helper.set("amount", amount);
        for (EnumFacing direction : EnumFacing.VALUES) {
            if (connections.contains(direction)) {
                helper.set("c" + direction.ordinal(), true);
            }
        }
    }
}
