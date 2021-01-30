package net.kunmc.lab.kmp.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderUtil {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void guiBindAndBlit(ResourceLocation location, int x, int y, int textureStartX, int textureStartY, int textureFinishWidth, int textureFinishHeight, int textureSizeX, int textureSizeY) {
        RenderSystem.pushMatrix();
        mc.getTextureManager().bindTexture(location);
        guiBlit(x, y, textureStartX, textureStartY, textureFinishWidth, textureFinishHeight, textureSizeX, textureSizeY);
        RenderSystem.popMatrix();
    }

    public static void guiBlit(int x, int y, int textureStartX, int textureStartY, int textureFinishWidth, int textureFinishHeight, int textureSizeX, int textureSizeY) {
        AbstractGui.blit(x, y, textureStartX, textureStartY, textureFinishWidth, textureFinishHeight, textureSizeX, textureSizeY);
    }

    public static void drawString(FontRenderer fr, String text, int x, int y, int color) {
        fr.drawString(text, x, y, color);
    }

    public static void drawCenterString(FontRenderer fr, String text, int x, int y, int color) {
        int size = fr.getStringWidth(text);
        drawString(fr, text, x - size / 2, y, color);
    }
}