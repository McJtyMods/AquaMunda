package mcjty.aquamunda.blocks.tank;

import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.blocks.generic.GenericBlockWithTE;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
        super(Material.iron, "tank", TankTE.class);
        setHardness(2.0f);
        setStepSound(soundTypeMetal);
        setHarvestLevel("pickaxe", 0);
        setTickRandomly(true);
    }

    @Override
    public MetaUsage getMetaUsage() {
        return MetaUsage.NONE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initModel() {
        StateMapperBase ignoreState = new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
                return TankISBM.modelResourceLocation;
            }
        };
        ModelLoader.setCustomStateMapper(this, ignoreState);
        ClientRegistry.bindTileEntitySpecialRenderer(TankTE.class, new TankTESR());
    }

    @SideOnly(Side.CLIENT)
    public void initItemModel() {
        Item itemBlock = GameRegistry.findItem(AquaMunda.MODID, "tank");
        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(AquaMunda.MODID + ":tank", "inventory");
        final int DEFAULT_ITEM_SUBTYPE = 0;
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlock, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
    }


    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
       getTE(world, pos).checkRainAndSun();
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        TankTE tankTE = (TankTE) accessor.getTileEntity();
        int networkId = tankTE.getID();
        if (networkId != -1) {
            TankNetwork tankNetwork = TankNetwork.getClientSide();
            Tank tank = tankNetwork.getOrCreateTank(networkId);
            currenttip.add(EnumChatFormatting.GREEN + "Id: " + networkId);
            currenttip.add(EnumChatFormatting.GREEN + "Liquid: " + tank.getFluidName());
            currenttip.add(EnumChatFormatting.GREEN + "Contents: " + tank.getContents() + " (" + tank.getMaxContents() + ")");
        }

        return currenttip;
    }

    private void fillFromContainer(EntityPlayer player, World world, Tank tank) {
        FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(player.getHeldItem());
        if (fluidStack != null) {
            if (tank.getFluid() == fluidStack.getFluid() || tank.getFluid() == null) {
                int newAmount = tank.getContents() + fluidStack.amount;
                if (newAmount <= tank.getMaxContents()) {
                    tank.setContents(newAmount);
                    tank.setFluid(fluidStack.getFluid());
                    TankNetwork.get(world).save(world);
                    if (!player.capabilities.isCreativeMode) {
                        ItemStack emptyContainer = FluidContainerRegistry.drainFluidContainer(player.getHeldItem());
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, emptyContainer);
                    }
                }
            }
        }
    }

    private void extractIntoContainer(EntityPlayer player, Tank tank) {
        if (tank.getContents() > 0) {
            FluidStack fluidStack = new FluidStack(tank.getFluid(), 1);
            int capacity = FluidContainerRegistry.getContainerCapacity(fluidStack, player.getHeldItem());
            if (capacity != 0) {
                if (tank.getContents() >= capacity) {
                    fluidStack.amount = capacity;
                    ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(fluidStack, player.getHeldItem());
                    if (filledContainer != null) {
                        tank.setContents(tank.getContents() - capacity);
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                        if (!player.inventory.addItemStackToInventory(filledContainer)) {
                            EntityItem entityItem = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, filledContainer);
                            player.worldObj.spawnEntityInWorld(entityItem);
                        }
                        player.openContainer.detectAndSendChanges();
                    } else {
                        // Try to insert the fluid back into the tank
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, filledContainer);
                        tank.setContents(tank.getContents() + capacity);
                    }
                    TankNetwork.get(player.worldObj).save(player.worldObj);
                }
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            if (player.getHeldItem() != null) {
                TankTE tankTE = getTE(world, pos);
                int id = tankTE.getID();
                TankNetwork tankNetwork = TankNetwork.get(world);
                Tank tank = tankNetwork.getOrCreateTank(id);

                if (FluidContainerRegistry.isEmptyContainer(player.getHeldItem())) {
                    if (!world.isRemote) {
                        extractIntoContainer(player, tank);
                    }
                    return true;
                } else if (FluidContainerRegistry.isFilledContainer(player.getHeldItem())) {
                    if (!world.isRemote) {
                        fillFromContainer(player, world, tank);
                    }
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
                    TankNetwork.get(world).save(world);
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
            world.spawnEntityInWorld(entityItem);
        }
        super.breakBlock(world, pos, state);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    protected BlockState createBlockState() {
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
