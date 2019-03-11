package mcjty.aquamunda.blocks.generic;


import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.compat.immcraft.ImmersiveCraftHandler;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.generic.GenericBlock;
import mcjty.lib.McJtyRegister;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

import java.util.function.Function;

public class GenericAMBlock extends GenericBlock {

    public GenericAMBlock(Material material, String name, Class<? extends GenericAMTE> clazz) {
        this(material, name, clazz, ItemBlock::new);
    }

    public GenericAMBlock(Material material, String name, Class<? extends GenericAMTE> clazz, Function<Block, ItemBlock> itemBlockFactory) {
        super(material, AquaMunda.instance, name, itemBlockFactory);
        if (clazz != null) {
            McJtyRegister.registerLater(this, clazz);
        }
        this.setCreativeTab(AquaMunda.setup.getTab());
    }

    @Override
    protected IImmersiveCraft getApi() {
        return ImmersiveCraftHandler.immersiveCraft;
    }
}
