package com.pryzmm.coreconfig.ui.option;

import com.pryzmm.coreconfig.data.HoveredEntry;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import com.pryzmm.coreconfig.ui.objects.CCContainer;
import com.pryzmm.coreconfigapi.entry.CustomEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class CCButtonCustom extends AbstractWidget {

    private final CCContainer container;
    private final Identifier valuePath;
    private final Integer color;
    private final int hoverColor;
    private final CustomEntry entry;

    public CCButtonCustom(CustomEntry entry, int width, int height, Identifier valuePath, CCContainer assignedContainer, int hoverColor, Integer color) {
        this.container = assignedContainer;
        this.valuePath = valuePath;
        this.hoverColor = hoverColor;
        this.entry = entry;
        this.color = color;
        super(0, 0, width - 4, height, Component.empty());
    }

    @Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int width = this.width;
        if (container.scrollable()) width = this.width - 6;

        if (color != null) graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, color);
        if (this.isHovered) {
            if (CoreConfigScreen.activePopup == null) graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, hoverColor);
            if (HoveredEntry.value != entry) HoveredEntry.value = entry;
        } else {
            if (color == null) graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, 0x55000000);
            if (HoveredEntry.value == entry) HoveredEntry.value = null;
        }

        int textWidth = Minecraft.getInstance().font.width(Component.translatable(this.valuePath.getPath()));

        graphics.text(
            Minecraft.getInstance().font,
            Component.translatable(this.valuePath.getPath()),
            this.getX() + (this.width / 2) - (textWidth / 2),
            this.getY() + (this.height / 2) - (Minecraft.getInstance().font.lineHeight / 2),
            0xFFFFFFFF,
            true
        );

    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {}

    @Override
    public void onClick(@NotNull MouseButtonEvent event, boolean doubleClick) {
        super.onClick(event, doubleClick);
        entry.getValue().run();
    }
}
