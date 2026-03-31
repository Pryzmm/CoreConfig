package com.pryzmm.coreconfig.ui.objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class CCButton extends AbstractWidget implements CCElement {

    private final int x, y, width, height;

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public CCButton(int x, int y, int width, int height, Component text) {
        super(x, y, width, height, text);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0x551A1A1A);
        graphics.text(
            Minecraft.getInstance().font,
            this.getMessage(),
            this.getX() + (this.width / 2) - (Minecraft.getInstance().font.width(this.getMessage()) / 2),
            this.getY() + (this.height / 2) - (Minecraft.getInstance().font.lineHeight / 2),
            0xFFFFFFFF,
            true
        );
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {}

}
