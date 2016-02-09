package mcjty.aquamunda.hosemultiblock;

import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import mcjty.immcraft.api.cable.ICableConnector;
import mcjty.immcraft.api.cable.ICableType;
import net.minecraftforge.fluids.Fluid;

public interface IHoseConnector extends ICableConnector {

    @Override
    default ICableType getType() { return ImmersiveCraftHandler.liquidType; }

    // Get the liquid that is supported by this connection or null if everything is supported.
    Fluid getSupportedFluid();

    // Get the maximum amount of mb that can be extracted out of this connection per tick.
    int getMaxExtractPerTick();

    // Extract an amount of liquid. Return what could be extracted.
    int extract(int amount);

    // Get the maximum amount of mb that can be inserted into this connection per tick.
    int getMaxInsertPerTick();

    // Get how much liquid can still be inserted into this connection before it is full.
    int getEmptyLiquidLeft();

    // Insert an amount of liquid. Return what could be inserted.
    int insert(Fluid fluid, int amount);

    // Return how full this connection is as represented by a percentage.
    float getFilledPercentage();
}
