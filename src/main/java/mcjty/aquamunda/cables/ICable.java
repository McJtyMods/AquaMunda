package mcjty.aquamunda.cables;

import net.minecraft.util.BlockPos;

import java.util.List;

/**
 * Representation of a cable.
 */
public interface ICable {
    List<BlockPos> getPath();
}
