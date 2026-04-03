package com.pryzmm.coreconfig.ui.objects;

import com.pryzmm.coreconfigapi.data.EntryHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class CCButton extends AbstractWidget implements CCElement {

    private final int x, y, width, height, hoverColor;
    private final Integer color;
    private final Runnable runnable;
    private final boolean isSaveButton;

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Integer getColor() { return color; }

    public CCButton(int x, int y, int width, int height, Component text, int hoverColor, boolean isSaveButton, Runnable runnable) {
        this(x, y, width, height, text, null, hoverColor, isSaveButton, runnable);
    }
    public CCButton(int x, int y, int width, int height, Component text, Integer color, int hoverColor, boolean isSaveButton, Runnable runnable) {
        super(x, y, width, height, text);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.hoverColor = hoverColor;
        this.runnable = runnable;
        this.isSaveButton = isSaveButton;
    }

    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        if (this.isHovered) {
            graphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, hoverColor);
            if (EntryHolder.containsAnyInvalidConfigs() && isSaveButton) setTooltip(Tooltip.create(Component.translatable("ui.coreconfig.cant_save")));
            else setTooltip(null);
        }
        else graphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0x551A1A1A);
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
    public void onClick(@NotNull MouseButtonEvent event, boolean doubleClick) {
        super.onClick(event, doubleClick);
        if (isSaveButton && EntryHolder.containsAnyInvalidConfigs()) return;
        runnable.run();
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {}

}
