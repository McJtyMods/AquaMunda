package mcjty.aquamunda.blocks.tank;

import com.google.common.primitives.Ints;
import mcjty.aquamunda.AquaMunda;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TankISBM implements ISmartBlockModel {

    public static final float THICK = 1/16.0f;

    public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("aquamunda:tank");

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
        Boolean north = extendedBlockState.getValue(TankBlock.NORTH);
        Boolean south = extendedBlockState.getValue(TankBlock.SOUTH);
        Boolean west = extendedBlockState.getValue(TankBlock.WEST);
        Boolean east = extendedBlockState.getValue(TankBlock.EAST);
        return new TankBakedModel(north, south, west, east);
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing side) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return null;
    }

    public class TankBakedModel implements IBakedModel {
        private TextureAtlasSprite sprite;

        private final boolean north;
        private final boolean south;
        private final boolean west;
        private final boolean east;

        public TankBakedModel(boolean north, boolean south, boolean west, boolean east) {
            sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(AquaMunda.MODID + ":blocks/tank");
            this.north = north;
            this.south = south;
            this.west = west;
            this.east = east;
        }

        private int[] vertexToInts(float x, float y, float z, float u, float v) {
            return new int[] {
                    Float.floatToRawIntBits(x),
                    Float.floatToRawIntBits(y),
                    Float.floatToRawIntBits(z),
                    -1,
                    Float.floatToRawIntBits(sprite.getInterpolatedU(u)),
                    Float.floatToRawIntBits(sprite.getInterpolatedV(v)),
                    0
            };
        }

        private BakedQuad createQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4) {
            Vec3 normal = v1.subtract(v2).crossProduct(v3.subtract(v2));
            EnumFacing side = LightUtil.toSide((float) normal.xCoord, (float) normal.yCoord, (float) normal.zCoord);

            return new BakedQuad(Ints.concat(
                    vertexToInts((float) v1.xCoord, (float) v1.yCoord, (float) v1.zCoord, 0, 0),
                    vertexToInts((float) v2.xCoord, (float) v2.yCoord, (float) v2.zCoord, 0, 16),
                    vertexToInts((float) v3.xCoord, (float) v3.yCoord, (float) v3.zCoord, 16, 16),
                    vertexToInts((float) v4.xCoord, (float) v4.yCoord, (float) v4.zCoord, 16, 0)
            ), -1, side);
        }

        @Override
        public List<BakedQuad> getFaceQuads(EnumFacing side) {
            return Collections.emptyList();
        }

        @Override
        public List<BakedQuad> getGeneralQuads() {
            List<BakedQuad> quads = new ArrayList<>();
            Vec3 p000 = new Vec3(0, 0, 0);
            Vec3 p100 = new Vec3(1, 0, 0);
            Vec3 p001 = new Vec3(0, 0, 1);
            Vec3 p101 = new Vec3(1, 0, 1);
            Vec3 p010 = new Vec3(0, 1, 0);
            Vec3 p110 = new Vec3(1, 1, 0);
            Vec3 p011 = new Vec3(0, 1, 1);
            Vec3 p111 = new Vec3(1, 1, 1);

            if (!north) {
                quads.add(createQuad(p000, p010, p110, p100));
                quads.add(createQuad(p100.addVector(0, 0, THICK), p110.addVector(0, 0, THICK), p010.addVector(0, 0, THICK), p000.addVector(0, 0, THICK)));
                if (!west && !east) {
                    quads.add(createQuad(p010.addVector(THICK, 0, 0), p010.addVector(THICK, 0, THICK), p110.addVector(-THICK, 0, THICK), p110.addVector(-THICK, 0, 0)));
                } else if (!west) {
                    quads.add(createQuad(p010.addVector(THICK, 0, 0), p010.addVector(THICK, 0, THICK), p110.addVector(0, 0, THICK), p110));
                    quads.add(createQuad(p110, p110.addVector(0, 0, THICK), p100.addVector(0, 0, THICK), p100)); // EAST cover
                } else if (!east) {
                    quads.add(createQuad(p010, p010.addVector(0, 0, THICK), p110.addVector(-THICK, 0, THICK), p110.addVector(-THICK, 0, 0)));
                    quads.add(createQuad(p000, p000.addVector(0, 0, THICK), p010.addVector(0, 0, THICK), p010)); // WEST cover
                } else {
                    quads.add(createQuad(p010, p010.addVector(0, 0, THICK), p110.addVector(0, 0, THICK), p110));
                    quads.add(createQuad(p000, p000.addVector(0, 0, THICK), p010.addVector(0, 0, THICK), p010)); // WEST cover
                    quads.add(createQuad(p110, p110.addVector(0, 0, THICK), p100.addVector(0, 0, THICK), p100)); // EAST cover
                }
            }
            if (!south) {
                quads.add(createQuad(p101, p111, p011, p001));
                quads.add(createQuad(p001.addVector(0, 0, -THICK), p011.addVector(0, 0, -THICK), p111.addVector(0, 0, -THICK), p101.addVector(0, 0, -THICK)));
                if (!west && !east) {
                    quads.add(createQuad(p111.addVector(-THICK, 0, 0), p111.addVector(-THICK, 0, -THICK), p011.addVector(THICK, 0, -THICK), p011.addVector(THICK, 0, 0)));
                } else if (!west) {
                    quads.add(createQuad(p111, p111.addVector(0, 0, -THICK), p011.addVector(THICK, 0, -THICK), p011.addVector(THICK, 0, 0)));
                    quads.add(createQuad(p101, p101.addVector(0, 0, -THICK), p111.addVector(0, 0, -THICK), p111)); // EAST cover
                } else if (!east) {
                    quads.add(createQuad(p111.addVector(-THICK, 0, 0), p111.addVector(-THICK, 0, -THICK), p011.addVector(0, 0, -THICK), p011));
                    quads.add(createQuad(p011, p011.addVector(0, 0, -THICK), p001.addVector(0, 0, -THICK), p001)); // WEST cover
                } else {
                    quads.add(createQuad(p111, p111.addVector(0, 0, -THICK), p011.addVector(0, 0, -THICK), p011));
                    quads.add(createQuad(p011, p011.addVector(0, 0, -THICK), p001.addVector(0, 0, -THICK), p001)); // WEST cover
                    quads.add(createQuad(p101, p101.addVector(0, 0, -THICK), p111.addVector(0, 0, -THICK), p111)); // EAST cover
                }
            }
            if (!west) {
                quads.add(createQuad(p010, p000, p001, p011));
                quads.add(createQuad(p011.addVector(THICK, 0, 0), p001.addVector(THICK, 0, 0), p000.addVector(THICK, 0, 0), p010.addVector(THICK, 0, 0)));
                quads.add(createQuad(p011, p011.addVector(THICK, 0, 0), p010.addVector(THICK, 0, 0), p010));
                if (!north && !south) {
                } else if (!north) {
                    quads.add(createQuad(p001, p001.addVector(THICK, 0, 0), p011.addVector(THICK, 0, 0), p011)); // SOUTH cover
                } else if (!south) {
                    quads.add(createQuad(p010, p010.addVector(THICK, 0, 0), p000.addVector(THICK, 0, 0), p000)); // NORTH cover
                } else {
                    quads.add(createQuad(p010, p010.addVector(THICK, 0, 0), p000.addVector(THICK, 0, 0), p000)); // NORTH cover
                    quads.add(createQuad(p001, p001.addVector(THICK, 0, 0), p011.addVector(THICK, 0, 0), p011)); // SOUTH cover
                }
            }
            if (!east) {
                quads.add(createQuad(p100, p110, p111, p101));
                quads.add(createQuad(p101.addVector(-THICK, 0, 0), p111.addVector(-THICK, 0, 0), p110.addVector(-THICK, 0, 0), p100.addVector(-THICK, 0, 0)));
                quads.add(createQuad(p110, p110.addVector(-THICK, 0, 0), p111.addVector(-THICK, 0, 0), p111));
                if (!north && !south) {
                } else if (!north) {
                    quads.add(createQuad(p111, p111.addVector(-THICK, 0, 0), p101.addVector(-THICK, 0, 0), p101)); // SOUTH cover
                } else if (!south) {
                    quads.add(createQuad(p100, p100.addVector(-THICK, 0, 0), p110.addVector(-THICK, 0, 0), p110)); // NORTH cover
                } else {
                    quads.add(createQuad(p100, p100.addVector(-THICK, 0, 0), p110.addVector(-THICK, 0, 0), p110)); // NORTH cover
                    quads.add(createQuad(p111, p111.addVector(-THICK, 0, 0), p101.addVector(-THICK, 0, 0), p101)); // SOUTH cover
                }
            }

            quads.add(createQuad(p000, p100, p101, p001));
            quads.add(createQuad(p001.addVector(0, THICK/2, 0), p101.addVector(0, THICK/2, 0), p100.addVector(0, THICK/2, 0), p000.addVector(0, THICK/2, 0)));
            return quads;
        }

        @Override
        public boolean isAmbientOcclusion() {
            return true;
        }

        @Override
        public boolean isGui3d() {
            return true;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return sprite;
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return ItemCameraTransforms.DEFAULT;
        }
    }
}
