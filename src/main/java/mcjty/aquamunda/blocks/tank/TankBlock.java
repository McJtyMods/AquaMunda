package mcjty.aquamunda.blocks.tank;

import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.blocks.generic.GenericBlockWithTE;
import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import mcjty.immcraft.api.helpers.InventoryHelper;
import mcjty.immcraft.api.multiblock.IMultiBlockClientInfo;
import mcjty.lib.McJtyLib;
import mcjty.lib.varia.FluidTools;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class TankBlock extends GenericBlockWithTE<TankTE> {

    // Properties that indicate if there is a compatible tank in a certain direction.
    public static final UnlistedPropertyTankAvailable NORTH = new UnlistedPropertyTankAvailable("north");
    public static final UnlistedPropertyTankAvailable SOUTH = new UnlistedPropertyTankAvailable("south");
    public static final UnlistedPropertyTankAvailable WEST = new UnlistedPropertyTankAvailable("west");
    public static final UnlistedPropertyTankAvailable EAST = new UnlistedPropertyTankAvailable("east");

    public TankBlock() {
        super(Material.IRON, "tank", TankTE.class);
        setHardness(2.0f);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 0);
        setTickRandomly(true);
    }

    @Override
    public RotationType getRotationType() {
        return RotationType.NONE;
    }

    @Override
    public void initModel() {
        McJtyLib.proxy.initStateMapper(this, TankBakedModel.BAKED_TANK);
        TankTESR.register();
    }

    public void initItemModel() {
        Item itemBlock = ForgeRegistries.ITEMS.getValue(new ResourceLocation(AquaMunda.MODID, "tank"));
        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(getRegistryName(), "inventory");
        final int DEFAULT_ITEM_SUBTYPE = 0;
        McJtyLib.proxy.initItemModelMesher(itemBlock, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
    }


    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
       getTE(world, pos).checkRainAndSun();
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        TankTE tankTE = (TankTE) accessor.getTileEntity();
        int blockID = tankTE.getID();
        if (blockID != -1) {
            IMultiBlockClientInfo clientInfo = ImmersiveCraftHandler.tankNetwork.getClientInfo(blockID);
            if (clientInfo != null) {
                TankClientInfo tankClientInfo = (TankClientInfo) clientInfo;
                currenttip.add(TextFormatting.GREEN + "Id: " + blockID);
                Fluid fluid = tankClientInfo.getFluid();
                if (fluid != null) {
                    currenttip.add(TextFormatting.GREEN + "Liquid: " + Tank.getFluidName(fluid));
                }
                currenttip.add(TextFormatting.GREEN + "Contents: " + tankClientInfo.getContents() + " (" + tankClientInfo.getBlockCount() * TankTE.MAX_CONTENTS + ")");
            }
        }

        return currenttip;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        super.addProbeInfo(mode, probeInfo, player, world, blockState, data);
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof TankTE) {
            TankTE tankTE = (TankTE) te;
            int blockID = tankTE.getID();
            if (blockID != -1) {
                IMultiBlockClientInfo clientInfo = ImmersiveCraftHandler.tankNetwork.getClientInfo(blockID);
                if (clientInfo != null) {
                    TankClientInfo tankClientInfo = (TankClientInfo) clientInfo;
                    probeInfo.text(TextFormatting.GREEN + "Id: " + blockID);
                    Fluid fluid = tankClientInfo.getFluid();
                    if (fluid != null) {
                        probeInfo.text(TextFormatting.GREEN + "Liquid: " + Tank.getFluidName(fluid));
                    }
                    probeInfo.text(TextFormatting.GREEN + "Contents: " + tankClientInfo.getContents() + " (" + tankClientInfo.getBlockCount() * TankTE.MAX_CONTENTS + ")");
                }
            }
        }
    }

    private void fillFromContainer(EntityPlayer player, World world, Tank tank) {
        ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
        if (heldItem.isEmpty()) {
            return;
        }
        ItemStack container = heldItem.copy().splitStack(1);
        FluidStack fluidStack = FluidTools.convertBucketToFluid(container);
        if (fluidStack != null) {
            if (tank.getFluid() == fluidStack.getFluid() || tank.getFluid() == null) {
                int newAmount = tank.getContents() + fluidStack.amount;
                if (newAmount <= tank.getMaxContents()) {
                    tank.setContents(newAmount);
                    tank.setFluid(fluidStack.getFluid());
                    ImmersiveCraftHandler.tankNetwork.save(world);
                    if (!player.capabilities.isCreativeMode) {
                        heldItem.splitStack(1);
                        ItemStack emptyContainer = FluidTools.drainContainer(container);
                        InventoryHelper.giveItemToPlayer(player, emptyContainer);
                    }
                }
            }
        }
    }

    private void extractIntoContainer(EntityPlayer player, Tank tank) {
        if (tank.getContents() > 0) {
            FluidStack fluidStack = new FluidStack(tank.getFluid(), 1);
            ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
            if (heldItem.isEmpty()) {
                return;
            }
            ItemStack container = heldItem.copy().splitStack(1);
            int capacity = FluidTools.getCapacity(fluidStack, container);
            if (capacity != 0) {
                if (tank.getContents() >= capacity) {
                    fluidStack.amount = capacity;
                    ItemStack filledContainer = FluidTools.fillContainer(fluidStack, container);
                    if (!filledContainer.isEmpty()) {
                        tank.setContents(tank.getContents() - capacity);
                        heldItem.splitStack(1);
                        InventoryHelper.giveItemToPlayer(player, filledContainer);
                    } else {
                        // Try to insert the fluid back into the tank
                        InventoryHelper.giveItemToPlayer(player, container);
                    }
                    ImmersiveCraftHandler.tankNetwork.save(player.getEntityWorld());
                }
            }
            player.openContainer.detectAndSendChanges();
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
            if (!heldItem.isEmpty()) {
                TankTE tankTE = getTE(world, pos);
                int id = tankTE.getID();

                Tank tank = ImmersiveCraftHandler.tankNetwork.getOrCreateMultiBlock(id);

                if (FluidTools.isEmptyContainer(heldItem)) {
                    extractIntoContainer(player, tank);
                    return true;
                } else if (FluidTools.isFilledContainer(heldItem)) {
                    fillFromContainer(player, world, tank);
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        super.onBlockPlacedBy(world, pos, state, entityLivingBase, itemStack);
        if (!world.isRemote) {
            NBTTagCompound tagCompound = itemStack.getTagCompound();
            Fluid fluid = null;
            if (tagCompound != null) {
                fluid = FluidRegistry.getFluid(tagCompound.getString("fluid"));
            }
            TankTE te = getTE(world, pos);
            te.addBlockToNetwork(fluid);
            Tank tank = te.getTank();
            if (tank != null) {
                if (tagCompound != null) {
                    tank.setContents(tank.getContents() + tagCompound.getInteger("contents"));
                    tank.setFluidByName(tagCompound.getString("fluid"));
                    ImmersiveCraftHandler.tankNetwork.save(world);
                }
            }
        }
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            int contents = 0;
            String fluidName = null;
            TankTE te = getTE(world, pos);
            Tank tank = te.getTank();
            if (tank != null && tank.getBlockCount() != 0) {
                contents = tank.getContents() / tank.getBlockCount();
                fluidName = tank.getFluidName();
                tank.setContents(tank.getContents() - contents);
            }

            te.removeBlockFromNetwork();

            ItemStack stack = new ItemStack(this);
            if (fluidName != null && !fluidName.isEmpty()) {
                stack.setTagCompound(new NBTTagCompound());
                stack.getTagCompound().setInteger("contents", contents);
                stack.getTagCompound().setString("fluid", fluidName);

            }
            EntityItem entityItem = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            world.spawnEntity(entityItem);
        }
        super.breakBlock(world, pos, state);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        IProperty[] listedProperties = new IProperty[0]; // no listed properties
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] { NORTH, SOUTH, WEST, EAST };
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;

        TankTE tankTE = getTE(world, pos);
        int id = tankTE.getID();
        boolean north = isSameTank(id, world, pos.north());
        boolean south = isSameTank(id, world, pos.south());
        boolean west = isSameTank(id, world, pos.west());
        boolean east = isSameTank(id, world, pos.east());

        return extendedBlockState.withProperty(NORTH, north).withProperty(SOUTH, south).withProperty(WEST, west).withProperty(EAST, east);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    private boolean isSameTank(int id, IBlockAccess world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() != ModBlocks.tankBlock) {
            return false;
        }
        TankTE otherTankTE = (TankTE) world.getTileEntity(pos);
        return id == otherTankTE.getID();
    }
}
