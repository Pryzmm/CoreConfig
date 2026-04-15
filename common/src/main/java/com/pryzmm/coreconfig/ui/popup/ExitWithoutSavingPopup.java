package com.pryzmm.coreconfig.ui.popup;

import com.pryzmm.coreconfig.data.EntryHolder;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import com.pryzmm.coreconfig.ui.objects.CCButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class ExitWithoutSavingPopup extends AbstractPopup {

    final Minecraft minecraft;
    final CCButton confirmButton;
    final CCButton cancelButton;
    final Screen screen;

    public ExitWithoutSavingPopup(Minecraft minecraft, int width, int height, Screen screen) {
        super(minecraft, width, height);
        this.minecraft = minecraft;
        this.screen = screen;
        confirmButton = new CCButton(this.getX() + 2, this.getY() + this.getHeight() - 22, (this.getWidth() / 2) - 3, 20, Component.translatable("ui.coreconfig.confirm"), null, 0x99663333, false, true, () -> {
            EntryHolder.refreshConfigs();
            if (screen instanceof CoreConfigScreen configScreen) configScreen.acceptClosedPopup();
            minecraft.setScreen(null);
        });
        cancelButton = new CCButton(this.getX() + 1 + (this.getWidth() / 2), this.getY() + this.getHeight() - 22, (this.getWidth() / 2) - 3, 20, Component.translatable("ui.coreconfig.cancel"), null, 0x99336633, false, true, () -> {
            if (screen instanceof CoreConfigScreen configScreen) configScreen.acceptClosedPopup();
        });
        if (screen instanceof CoreConfigScreen configScreen) this.visitWidgets(configScreen::addWidget);
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float a) {
        super.renderWidget(graphics, mouseX, mouseY, a);
        if (this.minecraft == null) return;
        Component title = Component.translatable("ui.coreconfig.exit_without_saving");

        graphics.pose().pushMatrix();
        graphics.nextStratum();

        graphics.drawString(
            minecraft.font,
            title,
            this.getX() + (this.width / 2) - (minecraft.font.width(title) / 2),
            this.getY() + 3,
            0xFFFFFFFF,
            true
        );
        Component desc = Component.translatable("ui.coreconfig.exit_without_saving_desc");
        List<FormattedCharSequence> lines = minecraft.font.split(desc, this.width - 10);
        int lineY = this.getY() + 20;
        for (FormattedCharSequence line : lines) {
            int lineWidth = minecraft.font.width(line);
            graphics.drawString(
                minecraft.font,
                line,
                this.getX() + (this.width / 2) - (lineWidth / 2),
                lineY,
                0xFFFFFFFF,
                true
            );
            lineY += minecraft.font.lineHeight;
        }
        confirmButton.renderWidget(graphics, mouseX, mouseY, a);
        cancelButton.renderWidget(graphics, mouseX, mouseY, a);
        graphics.pose().popMatrix();
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean doubleClick) {
        if (confirmButton.isMouseOver(event.x(), event.y())) confirmButton.mouseClicked(event, doubleClick);
        if (cancelButton.isMouseOver(event.x(), event.y())) cancelButton.mouseClicked(event, doubleClick);
        return true;
    }

}
