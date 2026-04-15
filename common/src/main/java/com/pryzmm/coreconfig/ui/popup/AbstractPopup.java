package com.pryzmm.coreconfig.ui.popup;

import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import net.minecraft.client.Minecraft;

public class AbstractPopup extends AbstractWidget {

    Screen screen;

    public AbstractPopup(Minecraft minecraft, int width, int height) {
        //noinspection DataFlowIssue
        super((minecraft.screen.width / 2) - (width / 2), (minecraft.screen.height / 2) - (height / 2), width, height, Component.empty());
        this.screen = minecraft.screen;
    }

    @Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.pose().pushMatrix();
        graphics.nextStratum();
        graphics.fill(0, 0, screen.width, screen.height, 0x99443333);
        graphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0x88444444);
        graphics.fill(this.getX(), this.getY() + 14, this.getX() + this.getWidth(), this.getY() + 15, 0xFFFFFFFF);
        graphics.outline(this.getX(), this.getY(), this.width, this.height, 0xCC666666);
        graphics.pose().popMatrix();
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean doubleClick) {
        onClick(event, doubleClick);
        return true;
    }

    @Override
    public boolean mouseReleased(@NotNull MouseButtonEvent event) {
        onRelease(event);
        return true;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isEscape() && screen instanceof CoreConfigScreen configScreen) configScreen.acceptClosedPopup();
        return super.keyPressed(event);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {}

}
