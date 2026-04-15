package com.pryzmm.coreconfig.ui.option;

import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.config.Config;
import com.pryzmm.coreconfig.data.HoveredEntry;
import com.pryzmm.coreconfig.network.Server;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import com.pryzmm.coreconfig.ui.objects.CCContainer;
import com.pryzmm.coreconfigapi.data.ConfigType;
import com.pryzmm.coreconfigapi.entry.EnumEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class CCButtonEnum extends AbstractWidget {

    private final CCContainer container;
    private final String translation;
    private final Integer color;
    private final int hoverColor;
    private final EnumEntry entry;

    public CCButtonEnum(EnumEntry entry, int width, int height, String translation, CCContainer assignedContainer, int hoverColor, Integer color) {
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
            Component.translatable(this.translation).withStyle(style -> style.withItalic(entry.getClientValue() != entry.getUnsavedValue())),
            this.getX() + 5,
            this.getY() + (this.height / 2) - (Minecraft.getInstance().font.lineHeight / 2),
            0xFFFFFFFF,
            true
        );

        graphics.drawString(
            Minecraft.getInstance().font,
            Component.literal(entry.getUnsavedValue().name()),
            this.getX() + this.width - 5 - Minecraft.getInstance().font.width(Component.literal(entry.getUnsavedValue().name())),
            this.getY() + (this.height / 2) - (Minecraft.getInstance().font.lineHeight / 2),
            0xFFFFFFFF,
            true
        );

        if ((entry.type() == ConfigType.SERVER && !Server.isHostingServer()) || (entry.type() == ConfigType.COMMON && entry.getServerValue() != null && !Server.isHostingServer())) {
            graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, Config.lockedColor.getValue());
            graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/locked_option.png"),
                this.getX() + this.width - 20,
                this.getY(),
                0, 0,
                19, 19,
                19, 19
            );
        }

    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {}

    @Override
    public void onClick(@NotNull MouseButtonEvent event, boolean doubleClick) {
        super.onClick(event, doubleClick);
        if (entry.type() != ConfigType.SERVER || Server.isHostingServer()) {
            Enum<?> current = entry.getUnsavedValue();
            Enum<?>[] constants = current.getDeclaringClass().getEnumConstants();
            int nextIndex = (current.ordinal() + 1) % constants.length;
            entry.change(constants[nextIndex]);
        }
    }
}
