package com.pryzmm.coreconfig.ui.option;

import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.config.Config;
import com.pryzmm.coreconfig.data.HoveredEntry;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import com.pryzmm.coreconfig.ui.objects.CCContainer;
import net.minecraft.resources.Identifier;
import com.pryzmm.coreconfig.network.Server;
import com.pryzmm.coreconfigapi.data.ConfigType;
import com.pryzmm.coreconfigapi.entry.ColorEntry;
import com.pryzmm.coreconfigapi.screen.IConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class CCButtonColor extends AbstractWidget {

    private final CCContainer container;
    private final String translation;
    private final Integer color;
    private final int hoverColor;
    private final ColorEntry entry;

    public CCButtonColor(ColorEntry entry, int width, int height, String translation, CCContainer assignedContainer, int hoverColor, Integer color) {
        this.container = assignedContainer;
        this.translation = translation;
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
            if (CoreConfigScreen.activePopup == null) graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, hoverColor);
            if (HoveredEntry.value != entry) HoveredEntry.value = entry;
        } else {
            if (color == null) graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, 0x55000000);
            if (HoveredEntry.value == entry) HoveredEntry.value = null;
        }

        graphics.text(
            Minecraft.getInstance().font,
            Component.translatable(this.translation).withStyle(style -> style.withItalic(!equals(entry.getClientValue(), entry.getUnsavedValue()))),
            this.getX() + 5,
            this.getY() + (this.height / 2) - (Minecraft.getInstance().font.lineHeight / 2),
            0xFFFFFFFF,
            true
        );

        graphics.fill(this.getX() + width - 19, this.getY() + 1, this.getX() + width - 1, this.getY() + this.height - 1, 0xFF000000);

        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/color_background.png"),
            this.getX() + width - 18,
            this.getY() + 2,
            0, 0,
            16, 16,
            16, 16
        );
        graphics.fill(this.getX() + width - 18, this.getY() + 2, this.getX() + width - 2, this.getY() + 18,
            entry.allowAlpha() ? (int) entry.getUnsavedValue() : (int) entry.getUnsavedValue() | 0xFF000000
        );

        if ((entry.type() == ConfigType.SERVER && !Server.isHostingServer()) || (entry.type() == ConfigType.COMMON && entry.getServerValue() != null && !Server.isHostingServer())) {
            graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, Config.lockedColor.getValue());
            graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/locked_option.png"),
                this.getX() + this.width - 20 - (container.scrollable() ? 6 : 0),
                this.getY(),
                0, 0,
                19, 19,
                19, 19
            );
        }

    }

    private static boolean equals(Integer value, Object unsavedValue) {
        try {
            int finalValue = Integer.parseInt(unsavedValue.toString());
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
        if (Minecraft.getInstance().screen instanceof IConfigScreen screen && (entry.type() != ConfigType.SERVER || Server.isHostingServer())) screen.sendColorPopup(entry);
    }

    @Override
    public boolean keyPressed(@NotNull KeyEvent event) {
        return super.keyPressed(event);
    }

    @Override
    public boolean keyReleased(@NotNull KeyEvent event) {
        return super.keyReleased(event);
    }

    @Override
    public boolean charTyped(@NotNull CharacterEvent event) {
        return super.charTyped(event);
    }
}
