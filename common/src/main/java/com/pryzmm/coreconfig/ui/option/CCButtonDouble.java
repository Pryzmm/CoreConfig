package com.pryzmm.coreconfig.ui.option;

import com.pryzmm.coreconfig.data.HoveredEntry;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import com.pryzmm.coreconfig.ui.objects.CCContainer;
import com.pryzmm.coreconfigapi.entry.DoubleEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class CCButtonDouble extends AbstractWidget {

    private final CCContainer container;
    private final Identifier valuePath;
    private final Integer color;
    private final int hoverColor;
    private final DoubleEntry entry;
    private final EditBox editBox;

    public CCButtonDouble(DoubleEntry entry, int width, int height, Identifier valuePath, CCContainer assignedContainer, int hoverColor, Integer color) {
        this.container = assignedContainer;
        this.valuePath = valuePath;
        this.hoverColor = hoverColor;
        this.entry = entry;
        this.color = color;
        super(0, 0, width - 4, height, Component.empty());

        editBox = new EditBox(Minecraft.getInstance().font, 0, 0, 200, 20, Component.empty());
        editBox.insertText(entry.getUnsavedValue().toString());
        editBox.setMaxLength(256);
    }

    @Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int width = this.width;
        if (container.scrollable()) width = this.width - 6;

        editBox.setX(this.getX() + width - 151);
        editBox.setY(this.getY() + (this.height / 2) - 9);
        editBox.setWidth(150);
        editBox.setHeight(18);


        boolean canParse = false;
        double value = 0;
        try { value = Double.parseDouble(editBox.getValue()); canParse = true; } catch (Exception ignored) {}

        if (color != null) graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, color);
        if (this.isHovered) {
            if (CoreConfigScreen.activePopup == null) graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, hoverColor);
            if (HoveredEntry.value != entry) HoveredEntry.value = entry;
        } else {
            if (color == null) graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, 0x55000000);
            if (HoveredEntry.value == entry) HoveredEntry.value = null;
        }

        graphics.text(
            Minecraft.getInstance().font,
            Component.translatable(this.valuePath.getPath()).withStyle(style -> style.withItalic(!equals(entry.getValue(), entry.getUnsavedValue()))),
            this.getX() + 5,
            this.getY() + (this.height / 2) - (Minecraft.getInstance().font.lineHeight / 2),
            (!canParse || value < entry.minimum() || value > entry.maximum()) ? 0xFFFF0044 : 0xFFFFFFFF,
            true
        );

        editBox.extractRenderState(graphics, mouseX, mouseY, a);

    }

    private static boolean equals(Double value, Object unsavedValue) {
        try {
            double finalValue = Double.parseDouble(unsavedValue.toString());
            return value == finalValue;
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {}

    @Override
    public void onClick(@NotNull MouseButtonEvent event, boolean doubleClick) {
        super.onClick(event, doubleClick);
        entry.change(entry.getUnsavedValue());
    }

    public void updateFocus(MouseButtonEvent event) {
        editBox.setFocused(editBox.areCoordinatesInRectangle(event.x(), event.y()));
    }

    @Override
    public boolean keyPressed(@NotNull KeyEvent event) {
        editBox.keyPressed(event);
        entry.change(editBox.getValue());
        if (event.isEscape() || event.input() == GLFW.GLFW_KEY_ENTER) editBox.setFocused(false);
        return super.keyPressed(event);
    }

    @Override
    public boolean keyReleased(@NotNull KeyEvent event) {
        editBox.keyReleased(event);
        entry.change(editBox.getValue());
        return super.keyReleased(event);
    }

    @Override
    public boolean charTyped(@NotNull CharacterEvent event) {
        editBox.charTyped(event);
        entry.change(editBox.getValue());
        return super.charTyped(event);
    }
}
