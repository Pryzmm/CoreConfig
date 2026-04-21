package com.pryzmm.coreconfig.ui.option;

import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.data.HoveredEntry;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import com.pryzmm.coreconfig.ui.objects.CCContainer;
import com.pryzmm.coreconfigapi.entry.DividerEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CCDivider extends AbstractWidget {

    private final String translation;
    private Integer color;
    private final DividerEntry entry;
    private final CCContainer container;

    public CCDivider(DividerEntry entry, int width, int height, String translation, CCContainer container, Integer color) {
        super(0, 0, width - 4, height, Component.empty());
        this.translation = translation;
        this.entry = entry;
        this.color = color;
        this.container = container;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float a) {

        int width = this.width;
        if (container.scrollable()) width = this.width - 7;

        if (color == null) color = 0xFFFFFFFF;

        if (this.isHovered && HoveredEntry.value != entry) HoveredEntry.value = entry;
        else if (HoveredEntry.value == entry) HoveredEntry.value = null;

        int textWidth = Minecraft.getInstance().font.width(Component.translatable(this.translation));
        int bottomPadding = (this.height - 20) / 2;

        graphics.drawString(
            Minecraft.getInstance().font,
            Component.translatable(this.translation),
            container.getX() + (width / 2) - (textWidth / 2),
            this.getY() + (this.height / 2) - (Minecraft.getInstance().font.lineHeight / 2) + bottomPadding,
            color,
            true
        );

        // Divider lines
        if (textWidth == 0) {
            graphics.fill(container.getX(), this.getY() + (this.height / 2) + bottomPadding, container.getX() + width - 7, this.getY() + (this.height / 2) + bottomPadding + 1, color);
        } else {
            graphics.fill(container.getX(), this.getY() + (this.height / 2) + bottomPadding, container.getX() + (width / 2) - (textWidth / 2) - 5, this.getY() + (this.height / 2) + bottomPadding + 1, color);
            graphics.fill(container.getX() + (width / 2) + (textWidth / 2) + 5, this.getY() + (this.height / 2) + bottomPadding, container.getX() + width - 11, this.getY() + (this.height / 2) + bottomPadding + 1, color);
        }

        graphics.blit(
            new ResourceLocation(CoreConfigConstants.MOD_ID, "textures/ui/divider_arrows.png"),
            this.getX() + width - 10,
            this.getY() + (this.height / 2) + bottomPadding - 4,
            entry.getFoldedState() ? 0 : 8, 0,
            8, 8,
            16, 8
        );


    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {}

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
        entry.setFoldedState(!entry.getFoldedState());
        CoreConfigScreen.screen.init(Minecraft.getInstance(), CoreConfigScreen.screen.width, CoreConfigScreen.screen.height);
    }

}
