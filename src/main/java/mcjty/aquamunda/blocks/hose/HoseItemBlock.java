package mcjty.aquamunda.blocks.hose;

import mcjty.aquamunda.compat.immcraft.ImmersiveCraftHandler;
import net.minecraft.block.Block;

public class HoseItemBlock extends CableItemBlock {

    public HoseItemBlock(Block block) {
        super(block, ImmersiveCraftHandler.liquidType, ImmersiveCraftHandler.liquidSubtype);
    }
}
