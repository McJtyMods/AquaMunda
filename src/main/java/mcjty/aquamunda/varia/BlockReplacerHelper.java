package mcjty.aquamunda.varia;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.lang.reflect.Field;

public class BlockReplacerHelper {

    public static void replaceBlock(Block toReplace, Block replacement) {
        try {
            for (Field blockField : Blocks.class.getFields()) {
                if (Block.class.isAssignableFrom(blockField.getType())) {
                    Block block = (Block) blockField.get(null);
                    if (block == toReplace) {
                        System.out.println("BlockReplacerHelper.replaceBlock");
                        blockField.setAccessible(true);
                        ReflectionTools.unfinalizeField(blockField);
                        blockField.set(null, replacement);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

//    public static void replaceBlock(Block toReplace, Block replacement) {
//
//        Class<?>[] classTest = new Class<?>[4];
//        Exception exception = null;
//
//        try {
//            for (Field blockField : Blocks.class.getFields()) {
//                if (Block.class.isAssignableFrom(blockField.getType())) {
//                    Block block = (Block) blockField.get(null);
//
//                    if (block == toReplace) {
//                        ResourceLocation nameForObject = Block.blockRegistry.getNameForObject(block);
//                        int id = Block.getIdFromBlock(block);
//
//                        ItemBlock itemBlock = ((ItemBlock) Item.getItemFromBlock(block));
//                        itemBlock.block = replacement;
//
//                        FMLControlledNamespacedRegistry<Block> registryBlocks = GameData.getBlockRegistry();
//                        registryBlocks.registryObjects.put(nameForObject, replacement);
//                        registryBlocks.underlyingIntegerMap.put(replacement, id); // OBFUSCATED put object
//                        getItemBlockMap().put(replacement, itemBlock);
//
//                        blockField.setAccessible(true);
//                        ReflectionTools.unfinalizeField(blockField);
//                        blockField.set(null, replacement);
//
//                        Method delegateNameMethod = replacement.delegate.getClass().getDeclaredMethod("setResourceName", ResourceLocation.class);
//                        delegateNameMethod.setAccessible(true);
//                        delegateNameMethod.invoke(replacement.delegate, toReplace.delegate.getResourceName());
//
//                        classTest[0] = blockField.get(null).getClass();
//                        classTest[1] = GameData.getBlockRegistry().getObjectById(id).getClass();
//                        classTest[2] = ((ItemBlock) GameData.getBlockItemMap().get(replacement)).block.getClass();
//                        classTest[3] = replacement.getClass();
//                        break;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            exception = e;
//        }
//
//        if (classTest[0] != classTest[1] || classTest[0] != classTest[2] || classTest[0] != classTest[3] || classTest[0] == null) {
//            throw new RuntimeException("Unable to replace block " + toReplace.getUnlocalizedName() + "! Debug info to report: " + classTest[0] + "," + classTest[1] + "," + classTest[2], exception);
//        }
//
//        Gaia.logger.info("Replaced block " + toReplace.getLocalizedName() + " with " + replacement.getLocalizedName());
//
//    }
//
//    public static void replaceItem(Item toReplace, Item replacement) {
//        Class<?>[] classTest = new Class<?>[2];
//        Exception exception = null;
//
//        try {
//            for (Field itemField : Item.class.getFields()) {
//                if (Item.class.isAssignableFrom(itemField.getType())) {
//                    Item item = (Item) itemField.get(null);
//
//                    if (item == toReplace) {
//                        ResourceLocation registryName = Item.itemRegistry.getNameForObject(item);
//                        int id = Item.getIdFromItem(item);
//
//                        FMLControlledNamespacedRegistry<Item> registryItems = GameData.getItemRegistry();
//                        registryItems.registryObjects.put(registryName, replacement);
//                        registryItems.underlyingIntegerMap.put(replacement, id); // OBFUSCATED put object
//
//                        itemField.setAccessible(true);
//                        ReflectionTools.unfinalizeField(itemField);
//                        itemField.set(null, replacement);
//
//                        Method delegateNameMethod = replacement.delegate.getClass().getDeclaredMethod("setName", String.class);
//                        delegateNameMethod.setAccessible(true);
//                        delegateNameMethod.invoke(replacement.delegate, toReplace.delegate.name());
//
//                        classTest[0] = itemField.get(null).getClass();
//                        classTest[1] = Block.blockRegistry.getObjectById(id).getClass();
//                        break;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            exception = e;
//        }
//
//        if (classTest[0] != classTest[1] || classTest[0] == null) {
//            throw new RuntimeException("Unable to replace item " + toReplace.getUnlocalizedName() + "! Debug info to report: " + classTest[0] + "," + classTest[1], exception);
//        }
//
//        Gaia.logger.info("Replaced item " + toReplace.getUnlocalizedName() + " with " + replacement.getUnlocalizedName());
//
//    }
//
//    @SuppressWarnings("unchecked")
//    private static Map<Block, Item> getItemBlockMap(){
//        return GameData.getBlockItemMap();
//    }

}