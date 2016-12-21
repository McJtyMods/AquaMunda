package mcjty.aquamunda.blocks.cuttingboard;

import mcjty.aquamunda.blocks.generic.GenericInventoryTE;
import mcjty.immcraft.api.handles.InputInterfaceHandle;
import mcjty.immcraft.api.handles.OutputInterfaceHandle;
import mcjty.immcraft.api.helpers.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class CuttingBoardTE extends GenericInventoryTE implements ITickable {

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 3;

    public CuttingBoardTE() {
        super(4);

        float boundsdx = .25f;
        float boundsdy = .33f;
        double renderdx = 0.19;
        double renderdz = 0.29;

        int x = 0;
        int y = 1;
        addInputHandle(boundsdx, boundsdy, renderdx, renderdz, 0, 1, SLOT_INPUT);
        addInputHandle(boundsdx, boundsdy, renderdx, renderdz, 2, 1, SLOT_INPUT+1);
        addInputHandle(boundsdx, boundsdy, renderdx, renderdz, 1, 2, SLOT_INPUT+2);

        addInterfaceHandle(new OutputInterfaceHandle().slot(SLOT_OUTPUT).side(EnumFacing.UP)
                .bounds(boundsdx * 4, boundsdy * y, boundsdx * (4 + 1), boundsdy * (y + 1))
                .renderOffset(new Vec3d(renderdx * (4 - 1) - renderdx / 2.0, 0.3, renderdz * (y - 1) - .02))
                .scale(.80f));
    }

    private void addInputHandle(float boundsdx, float boundsdy, double renderdx, double renderdz, int x, int y, int slot) {
        addInterfaceHandle(new InputInterfaceHandle().slot(slot).side(EnumFacing.UP)
                .bounds(boundsdx * x, boundsdy * y, boundsdx * (x + 1), boundsdy * (y + 1))
                .renderOffset(new Vec3d(renderdx * (x - 1) - renderdx / 2.0, 0.3, renderdz * (y - 1) - .02))
                .scale(.60f));
    }


    @Override
    public void update() {
        if (!getWorld().isRemote) {
        } else {
//            if (GeneralConfiguration.baseCookerVolume > 0.01f) {
//                int boilingState = getBoilingState();
//                if (boilingState >= 1) {
//                    float vol = (boilingState-1.0f)/9.0f;
//                    if (!CookerSoundController.isBoilingPlaying(getWorld(), pos)) {
//                        CookerSoundController.playBoiling(getWorld(), getPos(), vol);
//                    } else {
//                        CookerSoundController.updateVolume(getWorld(), getPos(), vol);
//                    }
//                } else {
//                    CookerSoundController.stopSound(getWorld(), getPos());
//                }
//            }
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (getWorld().isRemote) {
//            CookerSoundController.stopSound(getWorld(), getPos());
        }
    }

    @Override
    public boolean isUsable(EntityPlayer player) {
        return true;
    }

    private static Random random = new Random();

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTHelper helper) {
        super.writeToNBT(helper);
    }

}
