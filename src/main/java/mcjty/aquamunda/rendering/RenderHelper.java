package mcjty.aquamunda.rendering;

import mcjty.aquamunda.varia.Vector;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderHelper {
    public static float rot = 0.0f;

    public static void renderEntity(Entity entity, int xPos, int yPos) {
        GL11.glPushMatrix();
        GL11.glColor3f(1F, 1F, 1F);
        GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
        GL11.glEnable(2903 /* GL_COLOR_MATERIAL */);
        GL11.glPushMatrix();
        GL11.glTranslatef(xPos + 8, yPos + 16, 50F);
        float f1 = 10F;
        GL11.glScalef(-f1, f1, f1);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(rot, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(0.0F, 1.0F, 0.0F, 0.0F);
//        entity.renderYawOffset = entity.rotationYaw = entity.prevRotationYaw = entity.prevRotationYawHead = entity.rotationYawHead = 0;//this.rotateTurret;
        entity.rotationPitch = 0.0F;
        GL11.glTranslatef(0.0F, (float) entity.getYOffset(), 0.0F);
        // @todo
//        RenderManager.instance.playerViewY = 180F;
//        RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        GL11.glPopMatrix();
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

        GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
        GL11.glTranslatef(0F, 0F, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
        int i1 = 240;
        int k1 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,
                                              i1 / 1.0F, k1 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GL11.glDisable(2896 /* GL_LIGHTING */);
        GL11.glDisable(2929 /* GL_DEPTH_TEST */);
        GL11.glPopMatrix();
    }

    public static boolean renderObject(Minecraft mc, int x, int y, Object itm, boolean highlight) {
        if (itm instanceof Entity) {
            renderEntity((Entity) itm, x, y);
            return true;
        }
        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
        return renderObject(mc, itemRender, x, y, itm, highlight, 200);
    }

    public static boolean renderObject(Minecraft mc, RenderItem itemRender, int x, int y, Object itm, boolean highlight, float lvl) {
        itemRender.zLevel = lvl;

        if (itm==null) {
            return renderItemStack(mc, itemRender, null, x, y, "", highlight);
        }
        if (itm instanceof Item) {
            return renderItemStack(mc, itemRender, new ItemStack((Item) itm, 1), x, y, "", highlight);
        }
        if (itm instanceof Block) {
            return renderItemStack(mc, itemRender, new ItemStack((Block) itm, 1), x, y, "", highlight);
        }
        if (itm instanceof ItemStack) {
            return renderItemStackWithCount(mc, itemRender, (ItemStack) itm, x, y, highlight);
        }
//        if (itm instanceof IIcon) {
//            return renderIcon(mc, itemRender, (IIcon) itm, x, y, highlight);
//        }
        return renderItemStack(mc, itemRender, null, x, y, "", highlight);
    }

//    public static boolean renderIcon(Minecraft mc, RenderItem itemRender, IIcon itm, int xo, int yo, boolean highlight) {
//        itemRender.renderIcon(xo, yo, itm, 16, 16);
//        return true;
//    }

    public static boolean renderItemStackWithCount(Minecraft mc, RenderItem itemRender, ItemStack itm, int xo, int yo, boolean highlight) {
        if (itm.stackSize==1 || itm.stackSize==0) {
            return renderItemStack(mc, itemRender, itm, xo, yo, "", highlight);
        } else {
            return renderItemStack(mc, itemRender, itm, xo, yo, "" + itm.stackSize, highlight);
        }
    }

    public static boolean renderItemStack(Minecraft mc, RenderItem itemRender, ItemStack itm, int x, int y, String txt, boolean highlight){
        GL11.glColor3f(1F, 1F, 1F);

        boolean isLightingEnabled = GL11.glIsEnabled(GL11.GL_LIGHTING);

        boolean rc = false;
        if (highlight){
            GL11.glDisable(GL11.GL_LIGHTING);
            drawVerticalGradientRect(x, y, x+16, y+16, 0x80ffffff, 0xffffffff);
        }
        if (itm != null && itm.getItem() != null) {
            rc = true;
            boolean isRescaleNormalEnabled = GL11.glIsEnabled(GL12.GL_RESCALE_NORMAL);
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 32.0F);
            GL11.glColor4f(1F, 1F, 1F, 1F);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_LIGHTING);
            short short1 = 240;
            short short2 = 240;
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, short1 / 1.0F, short2 / 1.0F);
            itemRender.renderItemAndEffectIntoGUI(itm, x, y);
            itemRender.renderItemOverlayIntoGUI(mc.fontRendererObj, itm, x, y, txt);
            GL11.glPopMatrix();
            if (isRescaleNormalEnabled) {
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            } else {
                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            }
        }

        if (isLightingEnabled) {
            GL11.glEnable(GL11.GL_LIGHTING);
        } else {
            GL11.glDisable(GL11.GL_LIGHTING);
        }

        return rc;
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors.
     * x2 and y2 are not included.
     */
    public static void drawVerticalGradientRect(int x1, int y1, int x2, int y2, int color1, int color2) {
//        this.zLevel = 300.0F;
        float zLevel = 0.0f;

        float f = (color1 >> 24 & 255) / 255.0F;
        float f1 = (color1 >> 16 & 255) / 255.0F;
        float f2 = (color1 >> 8 & 255) / 255.0F;
        float f3 = (color1 & 255) / 255.0F;
        float f4 = (color2 >> 24 & 255) / 255.0F;
        float f5 = (color2 >> 16 & 255) / 255.0F;
        float f6 = (color2 >> 8 & 255) / 255.0F;
        float f7 = (color2 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        // @todo
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.startDrawingQuads();
//        tessellator.setColorRGBA_F(f1, f2, f3, f);
//        tessellator.addVertex(x2, y1, zLevel);
//        tessellator.addVertex(x1, y1, zLevel);
//        tessellator.setColorRGBA_F(f5, f6, f7, f4);
//        tessellator.addVertex(x1, y2, zLevel);
//        tessellator.addVertex(x2, y2, zLevel);
//        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Draws a rectangle with a horizontal gradient between the specified colors.
     * x2 and y2 are not included.
     */
    public static void drawHorizontalGradientRect(int x1, int y1, int x2, int y2, int color1, int color2) {
//        this.zLevel = 300.0F;
        float zLevel = 0.0f;

        float f = (color1 >> 24 & 255) / 255.0F;
        float f1 = (color1 >> 16 & 255) / 255.0F;
        float f2 = (color1 >> 8 & 255) / 255.0F;
        float f3 = (color1 & 255) / 255.0F;
        float f4 = (color2 >> 24 & 255) / 255.0F;
        float f5 = (color2 >> 16 & 255) / 255.0F;
        float f6 = (color2 >> 8 & 255) / 255.0F;
        float f7 = (color2 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        // @todo

//        Tessellator tessellator = Tessellator.instance;
//        tessellator.startDrawingQuads();
//        tessellator.setColorRGBA_F(f1, f2, f3, f);
//        tessellator.addVertex(x1, y1, zLevel);
//        tessellator.addVertex(x1, y2, zLevel);
//        tessellator.setColorRGBA_F(f5, f6, f7, f4);
//        tessellator.addVertex(x2, y2, zLevel);
//        tessellator.addVertex(x2, y1, zLevel);
//        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawHorizontalLine(int x1, int y1, int x2, int color) {
        Gui.drawRect(x1, y1, x2, y1 + 1, color);
    }

    public static void drawVerticalLine(int x1, int y1, int y2, int color) {
        Gui.drawRect(x1, y1, x1 + 1, y2, color);
    }

    // Draw a small triangle. x,y is the coordinate of the left point
    public static void drawLeftTriangle(int x, int y, int color) {
        drawVerticalLine(x, y, y, color);
        drawVerticalLine(x + 1, y - 1, y + 1, color);
        drawVerticalLine(x + 2, y - 2, y + 2, color);
    }

    // Draw a small triangle. x,y is the coordinate of the right point
    public static void drawRightTriangle(int x, int y, int color) {
        drawVerticalLine(x, y, y, color);
        drawVerticalLine(x - 1, y - 1, y + 1, color);
        drawVerticalLine(x - 2, y - 2, y + 2, color);
    }

    // Draw a small triangle. x,y is the coordinate of the top point
    public static void drawUpTriangle(int x, int y, int color) {
        drawHorizontalLine(x, y, x, color);
        drawHorizontalLine(x - 1, y + 1, x + 1, color);
        drawHorizontalLine(x - 2, y + 2, x + 2, color);
    }

    // Draw a small triangle. x,y is the coordinate of the bottom point
    public static void drawDownTriangle(int x, int y, int color) {
        drawHorizontalLine(x, y, x, color);
        drawHorizontalLine(x - 1, y - 1, x + 1, color);
        drawHorizontalLine(x - 2, y - 2, x + 2, color);
    }



    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
        float zLevel = 0.01f;
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        // @todo
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.startDrawingQuads();
//        tessellator.addVertexWithUV((double)(x + 0), (double)(y + height), (double)zLevel, (double)((float)(u + 0) * f), (double)((float)(v + height) * f1));
//        tessellator.addVertexWithUV((double) (x + width), (double) (y + height), (double) zLevel, (double) ((float) (u + width) * f), (double) ((float) (v + height) * f1));
//        tessellator.addVertexWithUV((double) (x + width), (double) (y + 0), (double) zLevel, (double) ((float) (u + width) * f), (double) ((float) (v + 0) * f1));
//        tessellator.addVertexWithUV((double) (x + 0), (double) (y + 0), (double) zLevel, (double) ((float) (u + 0) * f), (double) ((float) (v + 0) * f1));
//        tessellator.draw();
    }

    public static void renderBillboardQuad(double scale) {
        GL11.glPushMatrix();

        rotateToPlayer();

        // @todo
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.startDrawingQuads();
//        tessellator.addVertexWithUV(-scale, -scale, 0, 0, 0);
//        tessellator.addVertexWithUV(-scale, +scale, 0, 0, 1);
//        tessellator.addVertexWithUV(+scale, +scale, 0, 1, 1);
//        tessellator.addVertexWithUV(+scale, -scale, 0, 1, 0);
//        tessellator.draw();
        GL11.glPopMatrix();
    }

    public static void renderBillboardQuadWithRotation(float rot, double scale) {
        GL11.glPushMatrix();

        rotateToPlayer();

        GL11.glRotatef(rot, 0, 0, 1);

        // @todo
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.startDrawingQuads();
//        tessellator.addVertexWithUV(-scale, -scale, 0, 0, 0);
//        tessellator.addVertexWithUV(-scale, +scale, 0, 0, 1);
//        tessellator.addVertexWithUV(+scale, +scale, 0, 1, 1);
//        tessellator.addVertexWithUV(+scale, -scale, 0, 1, 0);
//        tessellator.draw();
        GL11.glPopMatrix();
    }

    public static void rotateToPlayer() {
//        GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
//        GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
    }

    /**
     * Draw a beam (in lines version) with some thickness.
     * @param S
     * @param E
     * @param P
     * @param width
     */
    public static void drawBeamLines(Vector S, Vector E, Vector P, float width) {
        Vector PS = Vector.subtract(S, P);
        Vector SE = Vector.subtract(E, S);

        Vector normal = Vector.cross(PS, SE);
        normal = normal.normalize();

        Vector half = Vector.mul(normal, width);
        Vector p1 = Vector.add(S, half);
        Vector p2 = Vector.subtract(S, half);
        Vector p3 = Vector.add(E, half);
        Vector p4 = Vector.subtract(E, half);

        drawQuadLines(Tessellator.getInstance(), p1, p3, p4, p2);
    }

    public static void drawQuadLines(Tessellator tessellator, Vector p1, Vector p2, Vector p3, Vector p4) {
//        tessellator.addVertex(p1.getX(), p1.getY(), p1.getZ());
//        tessellator.addVertex(p2.getX(), p2.getY(), p2.getZ());
//        tessellator.addVertex(p3.getX(), p3.getY(), p3.getZ());
//        tessellator.addVertex(p4.getX(), p4.getY(), p4.getZ());
    }

    /**
     * Draw a beam with some thickness.
     * @param S
     * @param E
     * @param P
     * @param width
     */
    public static void drawBeam(Vector S, Vector E, Vector P, float width) {
        Vector PS = Vector.subtract(S, P);
        Vector SE = Vector.subtract(E, S);

        Vector normal = Vector.cross(PS, SE);
        normal = normal.normalize();

        Vector half = Vector.mul(normal, width);
        Vector p1 = Vector.add(S, half);
        Vector p2 = Vector.subtract(S, half);
        Vector p3 = Vector.add(E, half);
        Vector p4 = Vector.subtract(E, half);

        drawQuad(p1, p3, p4, p2);
    }

    public static void drawQuad(Vector p1, Vector p2, Vector p3, Vector p4) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer renderer = tessellator.getWorldRenderer();
        renderer.pos(p1.getX(), p1.getY(), p1.getZ()).tex(0, 0).endVertex();
        renderer.pos(p2.getX(), p2.getY(), p2.getZ()).tex(1, 0).endVertex();
        renderer.pos(p3.getX(), p3.getY(), p3.getZ()).tex(1, 1).endVertex();
        renderer.pos(p4.getX(), p4.getY(), p4.getZ()).tex(0, 1).endVertex();
    }

//    public static IIcon checkIcon(IIcon icon) {
//        if (icon == null)
//            return getMissingTextureIcon();
//        return icon;
//    }
//
//    public static IIcon getFluidTexture(Fluid fluid, boolean flowing) {
//        if (fluid == null)
//            return getMissingTextureIcon();
//        return checkIcon(flowing ? fluid.getFlowingIcon() : fluid.getStillIcon());
//    }
//
//    public static IIcon getMissingTextureIcon(){
//        return ((TextureMap) Minecraft.getMinecraft().getTextureManager().getTexture(getBlocksResourceLocation())).getAtlasSprite("missingno");
//    }
//
//    public static ResourceLocation getBlocksResourceLocation(){
//        return TextureMap.locationBlocksTexture;
//    }
}
