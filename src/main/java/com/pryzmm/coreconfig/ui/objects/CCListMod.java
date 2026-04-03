package com.pryzmm.coreconfig.ui.objects;

import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.data.ModData;
import com.pryzmm.coreconfig.data.ModHolder;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class CCListMod extends AbstractWidget {

    private final String modID;
    private final Identifier image;
    private final CCContainer container;
    private final String modDisplayName;
    private final int hoverColor;

    final CoreConfigScreen screen;

    List<String> resourceLocations = List.of(
        "missing.png",
        "textures/missing.png"
    );

    /**
     * @param width The width of the widget. This is decreased by 6 automatically if the container is scrollable. This is also decreased further automatically for ease of use.
     */
    public CCListMod(CoreConfigScreen screen, int width, int height, Identifier identifier, CCContainer assignedContainer, int hoverColor) {
        this.modID = identifier.getNamespace();
        this.container = assignedContainer;
        this.modDisplayName = identifier.getPath();
        this.hoverColor = hoverColor;
        this.screen = screen;
        super(0, 0, width - 4, height, Component.empty());

        for (String location : resourceLocations) {
            if (Minecraft.getInstance().getResourceManager().getResource(Identifier.fromNamespaceAndPath(modID, location)).isPresent()) {
                this.image = Identifier.fromNamespaceAndPath(modID, location);
                return;
            }
        }
        this.image = Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/ui/icon/missing.png");
    }

    @Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {

        int width = this.width;
        if (container.scrollable()) width = this.width - 6;

        ModData data = ModHolder.getModData(modID);
        if (data.modBanner() != null) graphics.blit(RenderPipelines.GUI_TEXTURED, data.modBanner(), this.getX(), this.getY(), 0, 0, width, this.height, 200, 30, 200, 30);
        if (this.isHovered) graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, hoverColor);
        else if (data.modBanner() == null) graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, 0x55000000);

        graphics.text(
            Minecraft.getInstance().font,
            Component.translatable(modDisplayName),
            this.getX() + 36,
            this.getY() + (this.height / 2) - (Minecraft.getInstance().font.lineHeight / 2),
            0xFFFFFFFF,
            true
        );
        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            image,
            this.getX() + 1,
            this.getY() + 1,
            0, 0,
            28, 28,
            28, 28
        );
    }

    @Override
    public void onClick(@NotNull MouseButtonEvent event, boolean doubleClick) {
        super.onClick(event, doubleClick);
        screen.updateConfigScreen(modID);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {}

}
