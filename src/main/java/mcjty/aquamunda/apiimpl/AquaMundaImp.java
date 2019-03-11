package mcjty.aquamunda.apiimpl;

import mcjty.aquamunda.api.IAquaMunda;
import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.aquamunda.compat.immcraft.ImmersiveCraftHandler;
import mcjty.immcraft.api.cable.ICableType;
import net.minecraftforge.fluids.Fluid;

public class AquaMundaImp implements IAquaMunda {
    @Override
    public ICableType getAquaMundaCableType() {
        return ImmersiveCraftHandler.liquidType;
    }

    @Override
    public Fluid getFreshWater() {
        return FluidSetup.freshWater;
    }
}
