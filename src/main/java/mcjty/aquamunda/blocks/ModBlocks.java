package mcjty.aquamunda.blocks;

import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.blocks.cooker.CookerBlock;
import mcjty.aquamunda.blocks.customblocks.BlockDeadCrop;
import mcjty.aquamunda.blocks.customblocks.CustomFarmLand;
import mcjty.aquamunda.blocks.cuttingboard.CuttingBoardBlock;
import mcjty.aquamunda.blocks.desalination.DesalinationBoilerBlock;
import mcjty.aquamunda.blocks.desalination.DesalinationTankBlock;
import mcjty.aquamunda.blocks.grindstone.GrindStoneBlock;
import mcjty.aquamunda.blocks.hose.HoseBlock;
import mcjty.aquamunda.blocks.sprinkler.SprinklerBlock;
import mcjty.aquamunda.blocks.tank.TankBlock;
import mcjty.aquamunda.config.GeneralConfiguration;
import mcjty.aquamunda.environment.FarmlandOverhaulType;
import mcjty.aquamunda.fluid.BlockFreshWater;
import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.aquamunda.varia.BlockReplacerHelper;
import mcjty.lib.McJtyRegister;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
    public static BlockFreshWater blockFreshWater;
    public static TankBlock tankBlock;
    public static HoseBlock hoseBlock;

    public static SprinklerBlock sprinklerBlock;
    public static DesalinationBoilerBlock desalinationBoilerBlock;
    public static DesalinationTankBlock desalinationTankBlock;
    public static CookerBlock cookerBlock;
    public static CuttingBoardBlock cuttingBoardBlock;
    public static GrindStoneBlock grindStoneBlock;

    public static CustomFarmLand customFarmLand;
    public static BlockDeadCrop deadCarrot;
    public static BlockDeadCrop deadWheat;

    public static void init() {
        blockFreshWater = new BlockFreshWater(FluidSetup.freshWater, Material.WATER);
        tankBlock = new TankBlock();
        hoseBlock = new HoseBlock();
        sprinklerBlock = new SprinklerBlock();
        desalinationBoilerBlock = new DesalinationBoilerBlock();
        desalinationTankBlock = new DesalinationTankBlock();
        cookerBlock = new CookerBlock();
        cuttingBoardBlock = new CuttingBoardBlock();
        grindStoneBlock = new GrindStoneBlock();

        if (GeneralConfiguration.farmlandOverhaulType.get() != FarmlandOverhaulType.VANILLA) {
            customFarmLand = new CustomFarmLand();
            McJtyRegister.registerLater(customFarmLand, AquaMunda.instance, ItemBlock::new);
            //        try {
            //            GameRegistry.addSubstitutionAlias("minecraft:farmland", GameRegistry.Type.BLOCK, customFarmLand);
            //            GameRegistry.addSubstitutionAlias("minecraft:farmland", GameRegistry.Type.ITEM, new CustomFarmLandItemBlock(customFarmLand));
            //        } catch (ExistingSubstitutionException e) {
            //            throw new RuntimeException(e);
            //        }
            //        BlockReplacerHelper.replaceBlock(Blocks.farmland, customFarmLand);

//            System.out.println("Blocks.farmland = " + Blocks.FARMLAND);
//            System.out.println("Blocks.farmland.getClass() = " + Blocks.FARMLAND.getClass());
        } else {
            customFarmLand = null;
        }

        deadCarrot = new BlockDeadCrop("dead_carrot");
        deadWheat = new BlockDeadCrop("dead_wheat");
    }

    public static void postInit() {
        if (customFarmLand != null) {
            BlockReplacerHelper.replaceBlock(Blocks.FARMLAND, customFarmLand);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        blockFreshWater.initModel();
        tankBlock.initModel();
        hoseBlock.initModel();
        sprinklerBlock.initModel();
        desalinationBoilerBlock.initModel();
        desalinationTankBlock.initModel();
        cookerBlock.initModel();
        cuttingBoardBlock.initModel();
        grindStoneBlock.initModel();

        deadCarrot.initModel();
        deadWheat.initModel();
        if (customFarmLand != null) {
            customFarmLand.initModel();
        }
    }

    @SideOnly(Side.CLIENT)
    public static void initItemModels() {
        ModBlocks.tankBlock.initItemModel();
    }
}
