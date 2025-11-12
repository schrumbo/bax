package schrumbo.bax.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import static schrumbo.bax.BaxClient.config;
import static schrumbo.bax.BaxClient.highlightConfig;

public class RenderUtils {

    /**
     * renders the hitbox of an entity
     * @param entity
     * @param camera
     * @param matrices
     */
    public static void renderHitbox(Entity entity, Camera camera, MatrixStack matrices, int color) {
        Vec3d cameraPos = camera.getPos();
        Box box = entity.getBoundingBox().offset(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        preRender();

        VertexConsumerProvider.Immediate vcp = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();

        RenderLayer layer = RenderLayer.getLines();
        VertexConsumer buffer = vcp.getBuffer(layer);

        drawOutlinedBox(matrices, buffer, box, color);

        vcp.draw(layer);

        postRender();
    }

    /**
     * disables DepthTest before rendering
     */
    public static void preRender(){
        RenderSystem.lineWidth(4.0f);
        if (!highlightConfig.depthCheck){
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
    }

    /**
     * reenables DepthTest after rendering
     */
    public static void postRender(){
        RenderSystem.lineWidth(1.0f);
        if (!highlightConfig.depthCheck){
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    /**
     * the hitbox of mobs
     * @param matrices
     * @param buffer
     * @param box
     * @param color
     */
    private static void drawOutlinedBox(MatrixStack matrices, VertexConsumer buffer, Box box, int color) {
        MatrixStack.Entry entry = matrices.peek();
        float x1 = (float)box.minX;
        float y1 = (float)box.minY;
        float z1 = (float)box.minZ;
        float x2 = (float)box.maxX;
        float y2 = (float)box.maxY;
        float z2 = (float)box.maxZ;

        //bot
        buffer.vertex(entry, x1, y1, z1).color(color).normal(entry, 1, 0, 0);
        buffer.vertex(entry, x2, y1, z1).color(color).normal(entry, 1, 0, 0);

        buffer.vertex(entry, x2, y1, z1).color(color).normal(entry, 0, 0, 1);
        buffer.vertex(entry, x2, y1, z2).color(color).normal(entry, 0, 0, 1);

        buffer.vertex(entry, x2, y1, z2).color(color).normal(entry, -1, 0, 0);
        buffer.vertex(entry, x1, y1, z2).color(color).normal(entry, -1, 0, 0);

        buffer.vertex(entry, x1, y1, z2).color(color).normal(entry, 0, 0, -1);
        buffer.vertex(entry, x1, y1, z1).color(color).normal(entry, 0, 0, -1);

        //top
        buffer.vertex(entry, x1, y2, z1).color(color).normal(entry, 1, 0, 0);
        buffer.vertex(entry, x2, y2, z1).color(color).normal(entry, 1, 0, 0);

        buffer.vertex(entry, x2, y2, z1).color(color).normal(entry, 0, 0, 1);
        buffer.vertex(entry, x2, y2, z2).color(color).normal(entry, 0, 0, 1);

        buffer.vertex(entry, x2, y2, z2).color(color).normal(entry, -1, 0, 0);
        buffer.vertex(entry, x1, y2, z2).color(color).normal(entry, -1, 0, 0);

        buffer.vertex(entry, x1, y2, z2).color(color).normal(entry, 0, 0, -1);
        buffer.vertex(entry, x1, y2, z1).color(color).normal(entry, 0, 0, -1);

        //vert
        buffer.vertex(entry, x1, y1, z1).color(color).normal(entry, 0, 1, 0);
        buffer.vertex(entry, x1, y2, z1).color(color).normal(entry, 0, 1, 0);

        buffer.vertex(entry, x2, y1, z1).color(color).normal(entry, 0, 1, 0);
        buffer.vertex(entry, x2, y2, z1).color(color).normal(entry, 0, 1, 0);

        buffer.vertex(entry, x2, y1, z2).color(color).normal(entry, 0, 1, 0);
        buffer.vertex(entry, x2, y2, z2).color(color).normal(entry, 0, 1, 0);

        buffer.vertex(entry, x1, y1, z2).color(color).normal(entry, 0, 1, 0);
        buffer.vertex(entry, x1, y2, z2).color(color).normal(entry, 0, 1, 0);
    }




    public static void fillRoundedRect(DrawContext context, int x, int y, int width, int height, float radius, int color) {
        if (radius <= 0) {
            context.fill(x, y, x + width, y + height, color);
            return;
        }

        int minDimension = Math.min(width, height);
        int cornerRadius = (int) (minDimension * radius * 0.5f);
        cornerRadius = Math.min(cornerRadius, minDimension / 2);

        context.fill(x + cornerRadius, y + cornerRadius, x + width - cornerRadius, y + height - cornerRadius, color);

        context.fill(x + cornerRadius, y, x + width - cornerRadius, y + cornerRadius, color);

        context.fill(x + cornerRadius, y + height - cornerRadius, x + width - cornerRadius, y + height, color);

        context.fill(x, y + cornerRadius, x + cornerRadius, y + height - cornerRadius, color);

        context.fill(x + width - cornerRadius, y + cornerRadius, x + width, y + height - cornerRadius, color);

        fillRoundedCorner(context, x, y, cornerRadius, color, 1);
        fillRoundedCorner(context, x + width - cornerRadius, y, cornerRadius, color, 2);
        fillRoundedCorner(context, x, y + height - cornerRadius, cornerRadius, color, 4);
        fillRoundedCorner(context, x + width - cornerRadius, y + height - cornerRadius, cornerRadius, color, 3);
    }


    public static void drawRoundedRectWithOutline(DrawContext context, int x, int y, int width, int height, float radius, int thickness, int color) {
        if (radius <= 0) {
            drawBorder(context, x, y, width, height, color);
            return;
        }

        int minDimension = Math.min(width, height);
        int cornerRadius = (int) (minDimension * radius * 0.5f);
        cornerRadius = Math.min(cornerRadius, minDimension / 2);

        context.fill(x + cornerRadius, y, x + width - cornerRadius, y + thickness, color);

        context.fill(x + cornerRadius, y + height - thickness, x + width - cornerRadius, y + height, color);

        context.fill(x, y + cornerRadius, x + thickness, y + height - cornerRadius, color);

        context.fill(x + width - thickness, y + cornerRadius, x + width, y + height - cornerRadius, color);

        drawRoundedCornerOutline(context, x, y, cornerRadius, thickness, color, 1);
        drawRoundedCornerOutline(context, x + width - cornerRadius, y, cornerRadius, thickness, color, 2);
        drawRoundedCornerOutline(context, x, y + height - cornerRadius, cornerRadius, thickness, color, 4);
        drawRoundedCornerOutline(context, x + width - cornerRadius, y + height - cornerRadius, cornerRadius, thickness, color, 3);
    }

    /**
     * Fills a rounded corner
     * @param corner 1=topleft 2=topright 3=bottomright 4=bottomleft
     */
    private static void fillRoundedCorner(DrawContext context, int x, int y, int radius, int color, int corner) {
        for (int dx = 0; dx < radius; dx++) {
            for (int dy = 0; dy < radius; dy++) {
                double  distance = calcCorner(radius, corner, dx, dy);
                if (distance <= radius) {
                    context.fill(x + dx, y + dy, x + dx + 1, y + dy + 1, color);
                }
            }
        }
    }

    /**
     * Draws a rounded corner outline
     * @param corner 1=topleft 2=topright 3=bottomright 4=bottomleft
     */
    private static void drawRoundedCornerOutline(DrawContext context, int x, int y, int radius, int thickness, int color, int corner) {
        for (int dx = 0; dx < radius; dx++) {
            for (int dy = 0; dy < radius; dy++) {
                double distance = calcCorner(radius, corner, dx, dy);
                if (distance >= radius - thickness && distance <= radius) {
                    context.fill(x + dx, y + dy, x + dx + 1, y + dy + 1, color);
                }
            }
        }
    }

    private static double calcCorner(int radius, int corner, int dx, int dy) {
        int checkX = 0, checkY = 0;

        checkY = switch (corner) {
            case 1 -> {
                checkX = radius - 1 - dx;
                yield radius - 1 - dy;
            }
            case 2 -> {
                checkX = dx;
                yield radius - 1 - dy;
            }
            case 3 -> {
                checkX = dx;
                yield dy;
            }
            case 4 -> {
                checkX = radius - 1 - dx;
                yield dy;
            }
            default -> checkY;
        };

        return Math.sqrt(checkX * checkX + checkY * checkY);
    }

    public static void drawRectWithCutCorners(DrawContext context, int x, int y, int width, int height, int thickness, int color) {
        drawOutlineWithCutCorners(context, x, y, width, height, thickness, color);

        context.fill(x + thickness, y + thickness, x + width - thickness, y + height - thickness, color);
    }

    public static void drawOutlineWithCutCorners(DrawContext context, int x, int y, int width, int height, int thickness, int color){
        //oben
        context.fill(x + 1, y, x + width - 1, y + thickness, color);

        //unten
        context.fill(x + 1, y + height - thickness, x + width - 1, y + height, color);

        //links
        context.fill(x, y + 1, x + thickness, y + height - 1, color);

        //rechts
        context.fill(x + width - thickness, y + 1, x + width, y + height - 1, color);

    }

    public static void drawBorder(DrawContext context, int x, int y, int width, int height, int color){
        context.fill(x, y, x + width, y + 1, color);
        context.fill(x, y + height - 1, x + width, y + height, color);
        context.fill(x, y, x + 1, y + height, color);
        context.fill(x + width - 1, y, x + width, y + height, color);
    }



}
