package mcjty.aquamunda.blocks.tank;

import com.google.common.base.Function;
import mcjty.aquamunda.AquaMunda;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TankBakedModel implements IBakedModel {

    public static final float THICK = 1/16.0f;

    public static final ModelResourceLocation BAKED_TANK = new ModelResourceLocation("aquamunda:bakedtank");

    private TextureAtlasSprite sprite;
    private VertexFormat format;

    public TankBakedModel(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        this.format = format;
        sprite = bakedTextureGetter.apply(new ResourceLocation(AquaMunda.MODID, "blocks/tank"));
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (state == null) {
            return Collections.emptyList();
        }
        if (side != null) {
            return Collections.emptyList();
        }
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
        Boolean north = extendedBlockState.getValue(TankBlock.NORTH);
        Boolean south = extendedBlockState.getValue(TankBlock.SOUTH);
        Boolean west = extendedBlockState.getValue(TankBlock.WEST);
        Boolean east = extendedBlockState.getValue(TankBlock.EAST);

        List<BakedQuad> quads = new ArrayList<>();
        Vec3d p000 = new Vec3d(0, 0, 0);
        Vec3d p100 = new Vec3d(1, 0, 0);
        Vec3d p001 = new Vec3d(0, 0, 1);
        Vec3d p101 = new Vec3d(1, 0, 1);
        Vec3d p010 = new Vec3d(0, 1, 0);
        Vec3d p110 = new Vec3d(1, 1, 0);
        Vec3d p011 = new Vec3d(0, 1, 1);
        Vec3d p111 = new Vec3d(1, 1, 1);

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

    private void putVertex(UnpackedBakedQuad.Builder builder, Vec3d normal, double x, double y, double z, float u, float v) {
        for (int e = 0; e < format.getElementCount(); e++) {
            switch (format.getElement(e).getUsage()) {
                case POSITION:
                    builder.put(e, (float)x, (float)y, (float)z, 1.0f);
                    break;
                case COLOR:
                    builder.put(e, 1.0f, 1.0f, 1.0f, 1.0f);
                    break;
                case UV:
                    if (format.getElement(e).getIndex() == 0) {
                        u = sprite.getInterpolatedU(u);
                        v = sprite.getInterpolatedV(v);
                        builder.put(e, u, v, 0f, 1f);
                        break;
                    }
                case NORMAL:
                    builder.put(e, (float) normal.xCoord, (float) normal.yCoord, (float) normal.zCoord, 0f);
                    break;
                default:
                    builder.put(e);
                    break;
            }
        }
    }

    private BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4) {
        Vec3d normal = v2.subtract(v1).crossProduct(v3.subtract(v1)).normalize();

        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(sprite);
        putVertex(builder, normal, v1.xCoord, v1.yCoord, v1.zCoord, 0, 0);
        putVertex(builder, normal, v2.xCoord, v2.yCoord, v2.zCoord, 0, 16);
        putVertex(builder, normal, v3.xCoord, v3.yCoord, v3.zCoord, 16, 16);
        putVertex(builder, normal, v4.xCoord, v4.yCoord, v4.zCoord, 16, 0);
        return builder.build();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
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
        return sprite;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

}
