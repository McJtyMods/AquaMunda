package mcjty.aquamunda.blocks.bundle;

import mcjty.aquamunda.cables.CableSubType;
import mcjty.aquamunda.cables.CableType;
import net.minecraft.block.Block;

public class HoseItemBlock extends CableItemBlock {

    public HoseItemBlock(Block block) {
        super(block, CableType.LIQUID, CableSubType.LIQUID);
    }
}
