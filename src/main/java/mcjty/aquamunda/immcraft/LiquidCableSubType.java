package mcjty.aquamunda.immcraft;

import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.immcraft.api.cable.ICableSubType;
import net.minecraft.block.Block;

import java.util.Optional;

public class LiquidCableSubType implements ICableSubType {
    @Override
    public Optional<Block> getBlock() {
        return Optional.of(ModBlocks.hoseBlock);
    }

    @Override
    public String getTypeID() {
        return "liquid";
    }

    @Override
    public String getReadableName() {
        return "liquid";
    }

    @Override
    public String getTextureName() {
        return AquaMunda.MODID + ":blocks/hose";
    }
}
