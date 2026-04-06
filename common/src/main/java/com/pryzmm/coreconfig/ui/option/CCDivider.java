package com.pryzmm.coreconfig.ui.option;

import com.pryzmm.coreconfig.data.HoveredEntry;
import com.pryzmm.coreconfigapi.entry.DividerEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class CCDivider extends AbstractWidget {

    private final Identifier valuePath;
    private final Integer color;
    private final DividerEntry entry;

    public CCDivider(DividerEntry entry, int width, int height, Identifier valuePath, Integer color) {
        this.valuePath = valuePath;
        this.entry = entry;
        this.color = color;
        super(0, 0, width - 4, height, Component.empty());
    }

    @Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {

        if (this.isHovered && HoveredEntry.value != entry) HoveredEntry.value = entry;
        else if (HoveredEntry.value == entry) HoveredEntry.value = null;

        int textWidth = Minecraft.getInstance().font.width(Component.translatable(this.valuePath.getPath()));
        int bottomPadding = (this.height - 20) / 2;

        graphics.text(
            Minecraft.getInstance().font,
            Component.translatable(this.valuePath.getPath()),
            this.getX() + (this.width / 2) - (textWidth / 2),
            this.getY() + (this.height / 2) - (Minecraft.getInstance().font.lineHeight / 2) + bottomPadding,
            color,
            true
        );

        // Divider lines
        if (textWidth == 0) {
            graphics.fill(this.getX(), this.getY() + (this.height / 2) + bottomPadding, this.getX() + this.width, this.getY() + (this.height / 2) + bottomPadding + 1, color);
        } else {
            graphics.fill(this.getX(), this.getY() + (this.height / 2) + bottomPadding, this.getX() + (this.width / 2) - (textWidth / 2) - 5, this.getY() + (this.height / 2) + bottomPadding + 1, color);
            graphics.fill(this.getX() + (this.width / 2) + (textWidth / 2) + 5, this.getY() + (this.height / 2) + bottomPadding, this.getX() + this.width, this.getY() + (this.height / 2) + bottomPadding + 1, color);
        }

    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {}

}
