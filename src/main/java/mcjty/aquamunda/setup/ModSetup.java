package mcjty.aquamunda.setup;

import com.google.common.base.Function;
import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.config.ConfigSetup;
import mcjty.aquamunda.events.ForgeEventHandlers;
import mcjty.aquamunda.fluid.EntityFallingFreshWaterBlock;
import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.aquamunda.compat.immcraft.ImmersiveCraftHandler;
import mcjty.aquamunda.items.ModItems;
import mcjty.aquamunda.network.AMPacketHandler;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.lib.compat.MainCompatHandler;
import mcjty.lib.setup.DefaultModSetup;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import javax.annotation.Nullable;

public class ModSetup extends DefaultModSetup {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());

        AMPacketHandler.registerMessages("aquamunda");

        ConfigSetup.init();
        FluidSetup.init();
        ModBlocks.init();
        ModItems.init();
    }

    @Override
    protected void setupModCompat() {
        FMLInterModComms.sendFunctionMessage("immcraft", "getApi", "mcjty.aquamunda.setup.ModSetup$GetImmCraftApi");
        MainCompatHandler.registerWaila();
        MainCompatHandler.registerTOP();
    }

    public static class GetImmCraftApi implements Function<IImmersiveCraft, Void> {
        @Nullable
        @Override
        public Void apply(IImmersiveCraft immcraft) {
            ImmersiveCraftHandler.setImmersiveCraft(immcraft);
            return null;
        }
    }


    @Override
    public void createTabs() {
        createTab("aquamunda", new ItemStack(Items.WATER_BUCKET));
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);

        ModItems.initCrafting();
        EntityRegistry.registerModEntity(new ResourceLocation(AquaMunda.MODID, "fresh_water_falling"), EntityFallingFreshWaterBlock.class, "fresh_water_falling", 1, AquaMunda.instance, 250, 5, true);

        ConfigSetup.readRecipesConfig();
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        ConfigSetup.postInit();
        ModBlocks.postInit();
    }
}
