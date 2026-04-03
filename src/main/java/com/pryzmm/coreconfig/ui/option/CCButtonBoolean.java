package com.pryzmm.coreconfig.ui.option;

import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.client.CoreconfigClient;
import com.pryzmm.coreconfig.config.Config;
import com.pryzmm.coreconfig.entry.BooleanEntry;
import com.pryzmm.coreconfig.ui.objects.CCContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class CCButtonBoolean extends AbstractWidget {

    private final CCContainer container;
    private final Identifier valuePath;
    private final Integer color;
    private final int hoverColor;
    private final BooleanEntry entry;

    Identifier boolTrueImage = Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/boolean_true.png");
    Identifier boolFalseImage = Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/boolean_false.png");
    Identifier boolTrueImageSwitch = Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/boolean_true_switch.png");
    Identifier boolFalseImageSwitch = Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/boolean_false_switch.png");

    public CCButtonBoolean(BooleanEntry entry, int width, int height, Identifier valuePath, CCContainer assignedContainer, int hoverColor, Integer color) {
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
            graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, hoverColor);
            if (CoreconfigClient.hoveredEntry != entry) CoreconfigClient.hoveredEntry = entry;
        } else {
            if (color == null) graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, 0x55000000);
            if (CoreconfigClient.hoveredEntry == entry) CoreconfigClient.hoveredEntry = null;
        }

        graphics.text(
            Minecraft.getInstance().font,
            Component.translatable(this.valuePath.getPath()).withStyle(style -> style.withItalic(entry.getValue() != entry.getUnsavedValue())),
            this.getX() + 5,
            this.getY() + (this.height / 2) - (Minecraft.getInstance().font.lineHeight / 2),
            0xFFFFFFFF,
            true
        );
        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            !Config.booleanUseSwitchTexture.getValue() ? (entry.getUnsavedValue() ? boolTrueImage : boolFalseImage) : (entry.getUnsavedValue() ? boolTrueImageSwitch : boolFalseImageSwitch),
            this.getX() + this.width - 20 - (container.scrollable() ? 6 : 0),
            this.getY(),
            0, 0,
            19, 19,
            19, 19
        );

    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {}

    @Override
    public void onClick(@NotNull MouseButtonEvent event, boolean doubleClick) {
        super.onClick(event, doubleClick);
        entry.change(!entry.getUnsavedValue());
    }
}
