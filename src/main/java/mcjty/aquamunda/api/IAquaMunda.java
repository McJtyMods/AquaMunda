package mcjty.aquamunda.api;

import mcjty.immcraft.api.cable.ICableType;
import net.minecraftforge.fluids.Fluid;

/**
 * Global API for Aqua Munda
 * Get a reference to an implementation of this interface by calling:
 *         FMLInterModComms.sendFunctionMessage("aquamunda", "getApi", "<whatever>.YourClass$GetApi");
 */
public interface IAquaMunda {

    /// Get the Aqua Munda cable type
    ICableType getAquaMundaCableType();

    /// Get the Aqua Munda fresh water liquid
    Fluid getFreshWater();
}
