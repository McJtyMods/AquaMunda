package mcjty.aquamunda.cables;

import mcjty.aquamunda.blocks.bundle.BundleTE;
import mcjty.aquamunda.hosemultiblock.HoseNetwork;
import mcjty.aquamunda.hosemultiblock.IHoseConnector;
import mcjty.aquamunda.multiblock.MultiBlockNetwork;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class LiquidCableHandler implements ICableHandler {

    @Override
    public MultiBlockNetwork getNetwork(World world, CableSubType subType) {
        HoseNetwork hoseNetwork = HoseNetwork.get(world);
        return hoseNetwork.getNetwork();
    }

    @Override
    public MultiBlockNetwork getNetworkClient(CableSubType subType) {
        HoseNetwork hoseNetwork = HoseNetwork.getClientSide();
        return hoseNetwork.getNetwork();
    }

    @Override
    public void saveNetwork(World world) {
        HoseNetwork hoseNetwork = HoseNetwork.get(world);
        hoseNetwork.save(world);
    }

    @Override
    public ICable getCable(World world, CableSubType subType, int id) {
        HoseNetwork hoseNetwork = HoseNetwork.get(world);
        return hoseNetwork.getOrCreateHose(id);
    }

    @Override
    public void tick(BundleTE bundleTE, CableSection section) {
        // @todo take fluid pressure into account
        // @todo fix height difference calculation to be based on height of tank/connector and not on end points of hose.

        int id = section.getId();

        Cable hose = getHose(bundleTE, id);
        BlockPos first = hose.getPath().get(0);
        if (!first.equals(bundleTE.getPos())) {
            // We only let the first hosemultiblock TE in the path do the work.
            return;
        }

        BlockPos last = hose.getPath().get(hose.getBlockCount() - 1);
        World worldObj = bundleTE.getWorld();
        BundleTE otherBundleTE = (BundleTE) worldObj.getTileEntity(last);

        ICableConnector connector1 = getCableConnector(section, worldObj, null);
        if (connector1 == null) {
            return;
        }

        CableSection otherSection = otherBundleTE.findSection(section.getType(), section.getSubType(), id);
        if (otherSection == null) {
            return;
        }

        ICableConnector connector2 = getCableConnector(otherSection, worldObj, connector1);
        if (connector2 == null) {
            return;
        }


        IHoseConnector con1 = (IHoseConnector) connector1;
        IHoseConnector con2 = (IHoseConnector) connector2;

        if (con1.getSupportedFluid() != null && con2.getSupportedFluid() != null && con1.getSupportedFluid() != con2.getSupportedFluid()) {
            // Do nothing. The fluids are not equivalent
            return;
        }
        int y1 = first.getY();
        int y2 = last.getY();
        if (y1 == y2) {
            // Same height so try to balance the two containers.
            float pct1 = con1.getFilledPercentage();
            float pct2 = con2.getFilledPercentage();
            if (pct1 > pct2) {
                int available = con2.getEmptyLiquidLeft();
                int extracted = con1.extract(min3(con1.getMaxExtractPerTick(), con2.getMaxInsertPerTick(), available));
                con2.insert(con1.getSupportedFluid(), extracted);
            } else if (pct2 > pct1) {
                int available = con1.getEmptyLiquidLeft();
                int extracted = con2.extract(min3(con2.getMaxExtractPerTick(), con1.getMaxInsertPerTick(), available));
                con1.insert(con2.getSupportedFluid(), extracted);
            }
        } else if (y1 > y2) {
            int available = con2.getEmptyLiquidLeft();
            int extracted = con1.extract(min3(con1.getMaxExtractPerTick(), con2.getMaxInsertPerTick(), available));
            con2.insert(con1.getSupportedFluid(), extracted);
        } else {
            int available = con1.getEmptyLiquidLeft();
            int extracted = con2.extract(min3(con2.getMaxExtractPerTick(), con1.getMaxInsertPerTick(), available));
            con1.insert(con2.getSupportedFluid(), extracted);
        }
    }

    private ICableConnector getCableConnector(CableSection section, World worldObj, ICableConnector prevCable) {
        ICableConnector cable = section.getConnector(worldObj, 0);
        if (cable == null || cable == prevCable) {
            cable = section.getConnector(worldObj, 1);
        }
        return cable;
    }

    public Cable getHose(BundleTE bundleTE, int id) {
        if (id == -1) {
            return null;
        }
        HoseNetwork hoseNetwork = HoseNetwork.get(bundleTE.getWorld());
        return hoseNetwork.getOrCreateHose(id);
    }

    private static int min3(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

}
