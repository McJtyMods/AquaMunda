package mcjty.aquamunda.blocks;

import mcjty.aquamunda.blocks.bundle.BundleBlock;
import mcjty.aquamunda.blocks.bundle.HoseBlock;
import mcjty.aquamunda.blocks.customblocks.BlockDeadCrop;
import mcjty.aquamunda.blocks.customblocks.CustomFarmLand;
import mcjty.aquamunda.blocks.desalination.DesalinationBoilerBlock;
import mcjty.aquamunda.blocks.desalination.DesalinationTankBlock;
import mcjty.aquamunda.blocks.sprinkler.SprinklerBlock;
import mcjty.aquamunda.blocks.tank.TankBlock;
import mcjty.aquamunda.fluid.BlockFreshWater;
import mcjty.aquamunda.fluid.FluidSetup;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
    public static BlockFreshWater blockFreshWater;
    public static TankBlock tankBlock;
    public static BundleBlock bundleBlock;
    public static HoseBlock hoseBlock;

    public static SprinklerBlock sprinklerBlock;
    public static DesalinationBoilerBlock desalinationBoilerBlock;
    public static DesalinationTankBlock desalinationTankBlock;

    public static CustomFarmLand customFarmLand;
    public static BlockDeadCrop deadCarrot;
    public static BlockDeadCrop deadWheat;

    public static void init() {
        blockFreshWater = new BlockFreshWater(FluidSetup.freshWater, Material.water);
        tankBlock = new TankBlock();
        hoseBlock = new HoseBlock();
        bundleBlock = new BundleBlock();
        sprinklerBlock = new SprinklerBlock();
        desalinationBoilerBlock = new DesalinationBoilerBlock();
        desalinationTankBlock = new DesalinationTankBlock();

        customFarmLand = new CustomFarmLand();
//        BlockReplacerHelper.replaceBlock(Blocks.farmland, customFarmLand);
        deadCarrot = new BlockDeadCrop("dead_carrot");
        deadWheat = new BlockDeadCrop("dead_wheat");
    }

    public static void initCrafting() {

    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
//        blockFreshWater.initModel();
        tankBlock.initModel();
        hoseBlock.initModel();
        bundleBlock.initModel();
        sprinklerBlock.initModel();
        desalinationBoilerBlock.initModel();
        desalinationTankBlock.initModel();

        deadCarrot.initModel();
        deadWheat.initModel();
    }

    @SideOnly(Side.CLIENT)
    public static void initItemModels() {
        ModBlocks.tankBlock.initItemModel();
        ModBlocks.bundleBlock.initItemModel();
    }
}
