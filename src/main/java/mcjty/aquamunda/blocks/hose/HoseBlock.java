package mcjty.aquamunda.blocks.hose;

import mcjty.aquamunda.blocks.generic.GenericAMBlock;
import net.minecraft.block.material.Material;

public class HoseBlock extends GenericAMBlock {

    public HoseBlock() {
        super(Material.CLOTH, "hose", null, HoseItemBlock.class);
    }

    @Override
    public MetaUsage getMetaUsage() {
        return MetaUsage.NONE;
    }

}
