package com.pryzmm.coreconfig.ui.popup;

import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import com.pryzmm.coreconfig.ui.objects.CCButton;
import net.minecraft.resources.Identifier;
import com.pryzmm.coreconfigapi.entry.ColorEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ColorPopup extends AbstractPopup {

    final Minecraft minecraft;
    final CCButton button;
    final Screen screen;
    final ColorEntry entry;

    private static Integer alphaColor = 255;
    private static Integer redColor = 255;
    private static Integer blueColor = 255;
    private static Integer greenColor = 255;

    public ColorPopup(Minecraft minecraft, int width, int height, Screen screen, ColorEntry entry) {
        super(minecraft, width, height);
        this.minecraft = minecraft;
        this.screen = screen;
        this.entry = entry;
        alphaColor = (Integer.parseInt(entry.getUnsavedValue().toString()) >> 24) & 0xFF;
        redColor = (Integer.parseInt(entry.getUnsavedValue().toString()) >> 16) & 0xFF;
        greenColor = (Integer.parseInt(entry.getUnsavedValue().toString()) >> 8) & 0xFF;
        blueColor = Integer.parseInt(entry.getUnsavedValue().toString()) & 0xFF;
        button = new CCButton(this.getX() + 2, this.getY() + this.getHeight() - 22, this.getWidth() - 4, 20, Component.translatable("ui.coreconfig.confirm"), null, 0x99336633, false, true, () -> {
            entry.change(entry.allowAlpha() ? (alphaColor << 24) | (redColor << 16) | (greenColor << 8) | blueColor : (redColor << 16) | (greenColor << 8) | blueColor);
            if (screen instanceof CoreConfigScreen configScreen) configScreen.acceptClosedPopup();
        });
        this.visitWidgets(screen::addRenderableWidget);
    }

    @Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
        if (this.minecraft == null) return;

        int multiThing = entry.allowAlpha() ? 11 : 9;
        int offsetThing = entry.allowAlpha() ? 2 : 0;

        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/color_background.png"),
            this.getX() + 1,
            this.getY() + 1,
            0, 0,
            this.width - 2, 13,
            13, 13
        );
        graphics.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + 14, entry.allowAlpha() ? (alphaColor << 24) | (redColor << 16) | (greenColor << 8) | blueColor : 0xFF000000 | (redColor << 16) | (greenColor << 8) | blueColor);

        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/color_selector.png"),
            this.getX() + (this.width / multiThing * 2) - offsetThing, this.getY() + 17,
            1, 1,
            24, 110,
            128, 110
        );
        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/color_selector.png"),
            this.getX() + (this.width / multiThing * 2) - offsetThing,
            this.getY() + 18 + (int) Math.round((1 - (redColor / 255.0)) * 107),
            104, 1,
            23, 1,
            128, 128
        );

        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/color_selector.png"),
            this.getX() + (this.width / multiThing * 4) - offsetThing, this.getY() + 17,
            27, 1,
            24, 110,
            128, 110
        );
        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/color_selector.png"),
            this.getX() + (this.width / multiThing * 4) - offsetThing,
            this.getY() + 18 + (int) Math.round((1 - (greenColor / 255.0)) * 107),
            104, 1,
            23, 1,
            128, 128
        );

        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/color_selector.png"),
            this.getX() + (this.width / multiThing * 6) - offsetThing, this.getY() + 17,
            53, 1,
            24, 110,
            128, 110
        );
        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/color_selector.png"),
            this.getX() + (this.width / multiThing * 6) - offsetThing,
            this.getY() + 18 + (int) Math.round((1 - (blueColor / 255.0)) * 107),
            104, 1,
            23, 1,
            128, 128
        );

        if (entry.allowAlpha()) {
            graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/color_selector.png"),
                this.getX() + (this.width / multiThing * 8) - offsetThing, this.getY() + 17,
                79, 1,
                24, 110,
                128, 110
            );
            graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/color_selector.png"),
                this.getX() + (this.width / multiThing * 8) - offsetThing,
                this.getY() + 18 + (int) Math.round((1 - (alphaColor / 255.0)) * 107),
                104, 1,
                23, 1,
                128, 128
            );
        }

        if (grabbedComponent == getComponentFromCoords(mouseX, mouseY)) setColorOfComponentFromCoords(mouseX, mouseY);

        button.extractRenderState(graphics, mouseX, mouseY, a);
    }

    private enum ColorComponent {
        ALPHA, RED, GREEN, BLUE
    }
    private ColorComponent getComponentFromCoords(double x, double y) {
        double relX = x - this.getX();
        double relY = y - this.getY();
        if (relY < 17 || relY > 125) return null;
        int multiThing = entry.allowAlpha() ? 11 : 9;
        int offsetThing = entry.allowAlpha() ? 2 : 0;
        int[] slotIndices = entry.allowAlpha() ? new int[]{2, 4, 6, 8} : new int[]{2, 4, 6};
        ColorComponent[] components = entry.allowAlpha() ? new ColorComponent[]{ColorComponent.RED, ColorComponent.GREEN, ColorComponent.BLUE, ColorComponent.ALPHA} : new ColorComponent[]{ColorComponent.RED, ColorComponent.GREEN, ColorComponent.BLUE};
        for (int i = 0; i < slotIndices.length; i++) {
            double slotX = (double) this.getWidth() / multiThing * slotIndices[i] - offsetThing;
            if (relX >= slotX && relX <= slotX + 24) return components[i];
        }
        return null;
    }
    private void setColorOfComponentFromCoords(double x, double y) {
        ColorComponent component = getComponentFromCoords(x, y);
        if (component == null) return;
        double relY = y - this.getY();
        double percentage = Math.max(0, Math.min(1, (relY - 17) / 110.0));
        int value = (int) Math.abs(Math.round(percentage * 255) - 255);
        switch (component) {
            case RED -> redColor = value;
            case GREEN -> greenColor = value;
            case BLUE -> blueColor = value;
            case ALPHA -> alphaColor = value;
        }
    }

    private ColorComponent grabbedComponent = null;

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean doubleClick) {
        if (button.isMouseOver(event.x(), event.y())) button.mouseClicked(event, doubleClick);
        grabbedComponent = getComponentFromCoords(event.x(), event.y());
        return true;
    }

    @Override
    public boolean mouseReleased(@NotNull MouseButtonEvent event) {
        if (button.isMouseOver(event.x(), event.y())) button.mouseReleased(event);
        grabbedComponent = null;
        return true;
    }
}
