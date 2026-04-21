package com.pryzmm.coreconfig.ui.option;

import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.config.Config;
import com.pryzmm.coreconfig.data.HoveredEntry;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import com.pryzmm.coreconfig.ui.objects.CCContainer;
import com.pryzmm.coreconfig.network.Server;
import com.pryzmm.coreconfigapi.data.ConfigType;
import com.pryzmm.coreconfigapi.entry.DoubleEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class CCButtonDouble extends AbstractWidget {

    private final CCContainer container;
    private final String translation;
    private final Integer color;
    private final int hoverColor;
    private final DoubleEntry entry;
    private final EditBox editBox;

    public CCButtonDouble(DoubleEntry entry, int width, int height, String translation, CCContainer assignedContainer, int hoverColor, Integer color) {
        super(0, 0, width - 4, height, Component.empty());
        this.container = assignedContainer;
        this.translation = translation;
        this.hoverColor = hoverColor;
        this.entry = entry;
        this.color = color;

        editBox = new EditBox(Minecraft.getInstance().font, 0, 0, 200, 16, Component.empty());
        editBox.insertText(entry.getUnsavedValue().toString());
        editBox.setMaxLength(256);
    }

    private boolean isHovered(double pMouseX, double pMouseY) {
        int width = container.scrollable() ? this.width - 7 : this.width;
        return pMouseX >= this.getX() && pMouseY >= this.getY() && pMouseX < this.getX() + width && pMouseY < this.getY() + this.height;
    }

    public boolean isEditBoxFocused() {
        return editBox.isFocused();
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float a) {
        int width = this.width;
        if (container.scrollable()) width = this.width - 7;

        editBox.setX(this.getX() + width - 152 - (container.scrollable() ? 0 : 1));
        editBox.setY(this.getY() + (this.height / 2) - 8);
        editBox.setWidth(150);
        //editBox.setHeight(18);


        boolean canParse = false;
        double value = 0;
        try { value = Double.parseDouble(editBox.getValue()); canParse = true; } catch (Exception ignored) {}

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
            Component.translatable(this.translation).withStyle(style -> style.withItalic(!equals(entry.getClientValue(), entry.getUnsavedValue()))),
            this.getX() + 5,
            this.getY() + (this.height / 2) - (Minecraft.getInstance().font.lineHeight / 2),
            (!canParse || value < entry.minimum() || value > entry.maximum()) ? 0xFFFF0044 : 0xFFFFFFFF,
            true
        );

        editBox.renderWidget(graphics, mouseX, mouseY, a);

        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 400);
        if ((entry.type() == ConfigType.SERVER && !Server.isHostingServer()) || (entry.type() == ConfigType.COMMON && entry.getServerValue() != null && !Server.isHostingServer())) {
            graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, Config.lockedColor.getValue());
            graphics.blit(
                new ResourceLocation(CoreConfigConstants.MOD_ID, "textures/ui/locked_option.png"),
                this.getX() + this.width - 20 - (container.scrollable() ? 6 : 0),
                this.getY(),
                0, 0,
                19, 19,
                19, 19
            );
        }
        graphics.pose().popPose();

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
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
        if (entry.type() != ConfigType.SERVER || Server.isHostingServer()) entry.change(entry.getUnsavedValue());
    }

    public void updateFocus(double pMouseX, double pMouseY) {
        if (entry.type() != ConfigType.SERVER || Server.isHostingServer()) editBox.setFocused(editBox.isMouseOver(pMouseX, pMouseY));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        editBox.keyPressed(keyCode, scanCode, modifiers);
        entry.change(editBox.getValue());
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_ENTER) editBox.setFocused(false);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        editBox.keyReleased(keyCode, scanCode, modifiers);
        entry.change(editBox.getValue());
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        editBox.charTyped(codePoint, modifiers);
        entry.change(editBox.getValue());
        return super.charTyped(codePoint, modifiers);
    }

}
