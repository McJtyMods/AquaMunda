package mcjty.aquamunda.items;

import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.compat.immcraft.ImmersiveCraftHandler;
import mcjty.immcraft.api.book.IBook;
import mcjty.lib.McJtyRegister;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GrindstoneManual extends Item implements IBook {

    public GrindstoneManual() {
        setMaxStackSize(1);
        setRegistryName("grindstone_manual");
        setUnlocalizedName(AquaMunda.MODID + ".grindstone_manual");
        setCreativeTab(AquaMunda.setup.getTab());
        McJtyRegister.registerLater(this, AquaMunda.instance);
    }

    @Override
    public String getTitle() {
        return "The Grindstone Manual";
    }

    @Override
    public ResourceLocation getJson() {
        return new ResourceLocation(AquaMunda.MODID, "text/manual_grindstone.json");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        player.sendStatusMessage(new TextComponentString("Use this book on a book stand"), false);
        return EnumActionResult.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) {
            ImmersiveCraftHandler.immersiveCraft.openManual(player);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }


}
