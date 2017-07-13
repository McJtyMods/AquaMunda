package mcjty.aquamunda.items;

import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDish extends ItemFood {

    public static class DishInfo {
        private final String unlocName;
        private final int healAmount;
        private final float saturation;
        private final String tag;

        public DishInfo(String unlocName, int healAmount, float saturation, String tag) {
            this.unlocName = unlocName;
            this.healAmount = healAmount;
            this.saturation = saturation;
            this.tag = tag;
        }

        public String getUnlocName() {
            return unlocName;
        }

        public int getHealAmount() {
            return healAmount;
        }

        public float getSaturation() {
            return saturation;
        }

        public String getTag() {
            return tag;
        }
    }

    public static final String DISH_CARROTS = "carrots";
    public static final String DISH_VEGETABLE_SOUP = "vegetable_soup";
    public static final String DISH_POTATO = "potatoes";

    private static DishInfo[] dishInfos = new DishInfo[] {
            new DishInfo("dish_carrots", 5, 0.6f, DISH_CARROTS),
            new DishInfo("dish_vegetable_soup", 5, 0.6f, DISH_VEGETABLE_SOUP),
            new DishInfo("dish_potatoes", 5, 0.6f, DISH_POTATO),
    };

    private static Map<String, Integer> dishToMeta = null;

    private static void setupDishToMeta() {
        if (dishToMeta == null) {
            dishToMeta = new HashMap<>();
            int meta = 0;
            for (DishInfo info : dishInfos) {
                dishToMeta.put(info.getTag(), meta);
                meta++;
            }
        }
    }

    public static int getDishMeta(String dishName) {
        setupDishToMeta();
        return dishToMeta.get(dishName);
    }

    public ItemDish() {
        super(5, 0.6f, false);
        this.setRegistryName("dish");
        this.setCreativeTab(AquaMunda.creativeTab);
        this.setMaxStackSize(1);
        ImmersiveCraftHandler.immersiveCraft.getRegistry().registerLater(this, AquaMunda.MODID);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);
        return new ItemStack(Items.BOWL);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        List<ModelResourceLocation> resources = new ArrayList<>();
        for (DishInfo info : dishInfos) {
            resources.add(new ModelResourceLocation(getRegistryName(), "food=" + info.getTag()));
        }
        ModelLoader.registerItemVariants(this, resources.toArray(new ModelResourceLocation[resources.size()]));

        ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return new ModelResourceLocation(getRegistryName(), "food=" + dishInfos[stack.getItemDamage()].getTag());
            }
        });
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item.aquamunda." + dishInfos[stack.getItemDamage()].getUnlocName();
    }

    @Override
    public int getHealAmount(ItemStack stack) {
        return dishInfos[stack.getItemDamage()].getHealAmount();
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        return dishInfos[stack.getItemDamage()].getSaturation();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            int meta = 0;
            for (DishInfo info : dishInfos) {
                items.add(new ItemStack(this, 1, meta));
                meta++;
            }
        }
    }
}
