package mcjty.aquamunda.blocks;

import mcjty.aquamunda.blocks.hose.HoseBlock;
import mcjty.aquamunda.blocks.customblocks.BlockDeadCrop;
import mcjty.aquamunda.blocks.customblocks.CustomFarmLand;
import mcjty.aquamunda.blocks.customblocks.CustomFarmLandItemBlock;
import mcjty.aquamunda.blocks.desalination.DesalinationBoilerBlock;
import mcjty.aquamunda.blocks.desalination.DesalinationTankBlock;
import mcjty.aquamunda.blocks.sprinkler.SprinklerBlock;
import mcjty.aquamunda.blocks.tank.TankBlock;
import mcjty.aquamunda.fluid.BlockFreshWater;
import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.aquamunda.varia.BlockReplacerHelper;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.ExistingSubstitutionException;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
    public static BlockFreshWater blockFreshWater;
    public static TankBlock tankBlock;
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
        sprinklerBlock = new SprinklerBlock();
        desalinationBoilerBlock = new DesalinationBoilerBlock();
        desalinationTankBlock = new DesalinationTankBlock();

        customFarmLand = new CustomFarmLand();
        GameRegistry.registerBlock(customFarmLand);
//        try {
//            GameRegistry.addSubstitutionAlias("minecraft:farmland", GameRegistry.Type.BLOCK, customFarmLand);
//            GameRegistry.addSubstitutionAlias("minecraft:farmland", GameRegistry.Type.ITEM, new CustomFarmLandItemBlock(customFarmLand));
//        } catch (ExistingSubstitutionException e) {
//            throw new RuntimeException(e);
//        }
//        BlockReplacerHelper.replaceBlock(Blocks.farmland, customFarmLand);

        System.out.println("Blocks.farmland = " + Blocks.farmland);
        System.out.println("Blocks.farmland.getClass() = " + Blocks.farmland.getClass());

        deadCarrot = new BlockDeadCrop("dead_carrot");
        deadWheat = new BlockDeadCrop("dead_wheat");
    }

    public static void postInit() {
        BlockReplacerHelper.replaceBlock(Blocks.farmland, customFarmLand);
    }

    public static void initCrafting() {

    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        blockFreshWater.initModel();
        tankBlock.initModel();
        hoseBlock.initModel();
        sprinklerBlock.initModel();
        desalinationBoilerBlock.initModel();
        desalinationTankBlock.initModel();
        customFarmLand.initModel();

        deadCarrot.initModel();
        deadWheat.initModel();
    }

    @SideOnly(Side.CLIENT)
    public static void initItemModels() {
        ModBlocks.tankBlock.initItemModel();
    }
}
