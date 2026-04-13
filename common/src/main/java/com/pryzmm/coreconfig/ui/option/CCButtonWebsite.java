package com.pryzmm.coreconfig.ui.option;

import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.data.HoveredEntry;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import com.pryzmm.coreconfig.ui.objects.CCContainer;
import com.pryzmm.coreconfigapi.entry.WebsiteEntry;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CCButtonWebsite extends AbstractWidget {

    private final CCContainer container;
    private final String translation;
    private final Integer color;
    private final int hoverColor;
    private final WebsiteEntry entry;

    public CCButtonWebsite(WebsiteEntry entry, int width, int height, String translation, CCContainer assignedContainer, int hoverColor, Integer color) {
        super(0, 0, width - 4, height, Component.empty());
        this.container = assignedContainer;
        this.translation = translation;
        this.hoverColor = hoverColor;
        this.entry = entry;
        this.color = color;
    }

    private boolean isHovered(double pMouseX, double pMouseY) {
        int width = container.scrollable() ? this.width - 6 : this.width;
        return pMouseX >= this.getX() && pMouseY >= this.getY() && pMouseX < this.getX() + width && pMouseY < this.getY() + this.height;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float a) {
        int width = this.width;
        if (container.scrollable()) width = this.width - 6;

        if (color != null) graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, color);
        if (isHovered(mouseX, mouseY)) {
            if (CoreConfigScreen.activePopup == null) graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, hoverColor);
            if (HoveredEntry.value != entry) HoveredEntry.value = entry;
        } else {
            if (color == null) graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, 0x55000000);
            if (HoveredEntry.value == entry) HoveredEntry.value = null;
        }

        graphics.drawString(
            Minecraft.getInstance().font,
            Component.translatable(this.translation),
            this.getX() + 5,
            this.getY() + (this.height / 2) - (Minecraft.getInstance().font.lineHeight / 2),
            0xFFFFFFFF,
            true
        );
        graphics.blit(
            RenderType::guiTextured,
            ResourceLocation.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/goto_website.png"),
            this.getX() + width - 16,
            this.getY() + 4,
            0, 0,
            11, 11,
            11, 11
        );

    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {}

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
        if (entry.getClientValue() != null && !entry.getClientValue().isEmpty()) {
            Util.getPlatform().openUri(entry.getClientValue());
        }
    }
}
