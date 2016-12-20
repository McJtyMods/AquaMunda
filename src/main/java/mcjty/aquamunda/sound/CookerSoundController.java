package mcjty.aquamunda.sound;

import com.google.common.collect.Maps;
import mcjty.aquamunda.AquaMunda;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

@SideOnly(Side.CLIENT)
public final class CookerSoundController {

    public static void init() {
        boilingwater = registerSound(new ResourceLocation(AquaMunda.MODID, "boilingwater"));
    }

    private static final Map<Pair<Integer, BlockPos>, CookerSound> sounds = Maps.newHashMap();
    protected static SoundEvent boilingwater;

    private static SoundEvent registerSound(ResourceLocation rl){
        SoundEvent ret = new SoundEvent(rl).setRegistryName(rl);
        ((FMLControlledNamespacedRegistry)SoundEvent.REGISTRY).register(ret);
        return ret;
    }

    public static void stopSound(World worldObj, BlockPos pos) {
        Pair<Integer, BlockPos> g = fromPosition(worldObj, pos);
        if (sounds.containsKey(g)) {
            MovingSound movingSound = sounds.get(g);
            Minecraft.getMinecraft().getSoundHandler().stopSound(movingSound);
            sounds.remove(g);
        }
    }

    private static void playSound(World worldObj, BlockPos pos, SoundEvent soundType) {
        CookerSound sound = new CookerSound(soundType, worldObj, pos);
        stopSound(worldObj, pos);
        Minecraft.getMinecraft().getSoundHandler().playSound(sound);
        Pair<Integer, BlockPos> g = Pair.of(worldObj.provider.getDimension(), pos);
        sounds.put(g, sound);
    }


    public static void playCooking(World worldObj, BlockPos pos) {
        playSound(worldObj, pos, boilingwater);
    }

    public static boolean isCookingPlaying(World worldObj, BlockPos pos) {
        return isSoundTypePlayingAt(boilingwater, worldObj, pos);
    }


    private static boolean isSoundTypePlayingAt(SoundEvent event, World world, BlockPos pos){
        CookerSound s = getSoundAt(world, pos);
        return s != null && s.isSoundType(event);
    }

    private static CookerSound getSoundAt(World world, BlockPos pos){
        return sounds.get(fromPosition(world, pos));
    }

    private static Pair<Integer, BlockPos> fromPosition(World world, BlockPos pos){
        return Pair.of(world.provider.getDimension(), pos);
    }

}
