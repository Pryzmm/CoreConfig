package com.pryzmm.coreconfig.ui.option;

import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.data.HoveredEntry;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import com.pryzmm.coreconfig.ui.objects.CCContainer;
import com.pryzmm.coreconfig.util.Identifier;
import com.pryzmm.coreconfig.util.Server;
import com.pryzmm.coreconfigapi.data.ConfigType;
import com.pryzmm.coreconfigapi.entry.CustomEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class CCButtonCustom extends AbstractWidget {

    private final CCContainer container;
    private final String translation;
    private final Integer color;
    private final int hoverColor;
    private final CustomEntry entry;

    public CCButtonCustom(CustomEntry entry, int width, int height, String translation, CCContainer assignedContainer, int hoverColor, Integer color) {
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

        int textWidth = Minecraft.getInstance().font.width(Component.translatable(this.translation));

        graphics.text(
            Minecraft.getInstance().font,
            Component.translatable(this.translation),
            this.getX() + (this.width / 2) - (textWidth / 2),
            this.getY() + (this.height / 2) - (Minecraft.getInstance().font.lineHeight / 2),
            0xFFFFFFFF,
            true
        );

        if ((entry.type() == ConfigType.SERVER && !Server.isHostingServer()) || (entry.type() == ConfigType.COMMON && entry.getServerValue() != null && !Server.isHostingServer())) {
            graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, 0x44FF0011);
            graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                Identifier.get(CoreConfigConstants.MOD_ID, "textures/ui/locked_option.png"),
                this.getX() + this.width - 20 - (container.scrollable() ? 6 : 0),
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
        if (entry.type() != ConfigType.SERVER || Server.isHostingServer()) entry.getClientValue().run();
    }
}
