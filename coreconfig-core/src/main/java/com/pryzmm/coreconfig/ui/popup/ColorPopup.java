package com.pryzmm.coreconfig.ui.popup;

import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import com.pryzmm.coreconfig.ui.objects.CCButton;
import com.pryzmm.coreconfigapi.entry.ColorEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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
        alphaColor = entry.allowAlpha() ? Integer.parseInt(entry.getUnsavedValue().toString()) >> 24 : 255;
        redColor = (Integer.parseInt(entry.getUnsavedValue().toString()) >> 16) & 0xFF;
        greenColor = (Integer.parseInt(entry.getUnsavedValue().toString()) >> 8) & 0xFF;
        blueColor = Integer.parseInt(entry.getUnsavedValue().toString()) & 0xFF;
        button = new CCButton(this.getX() + 2, this.getY() + this.getHeight() - 22, this.getWidth() - 4, 20, Component.translatable("ui.coreconfig.confirm"), null, 0x99336633, false, true, () -> {
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
        graphics.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + 14, (alphaColor << 24) | (redColor << 16) | (greenColor << 8) | blueColor);

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
            this.getX() + (this.width / multiThing * 4) - offsetThing, this.getY() + 17,
            27, 1,
            24, 110,
            128, 110
        );
        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/color_selector.png"),
            this.getX() + (this.width / multiThing * 6) - offsetThing, this.getY() + 17,
            53, 1,
            24, 110,
            128, 110
        );
        if (entry.allowAlpha()) graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/color_selector.png"),
            this.getX() + (this.width / multiThing * 8) - offsetThing, this.getY() + 17,
            79, 1,
            24, 110,
            128, 110
        );


        button.extractRenderState(graphics, mouseX, mouseY, a);
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean doubleClick) {
        if (button.isMouseOver(event.x(), event.y())) button.mouseClicked(event, doubleClick);
        return true;
    }

}
