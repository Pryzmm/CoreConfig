package com.pryzmm.coreconfig.ui.objects;

import com.pryzmm.coreconfig.Coreconfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
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

    List<String> resourceLocations = List.of(
        "icon.png",
        "textures/icon.png"
    );


    public CCListMod(int width, int height, String modID, CCContainer assignedContainer) {
        this(width, height, modID, modID, assignedContainer);
    }

    public CCListMod(int width, int height, String modID, String modDisplayName, CCContainer assignedContainer) {
        this.modID = modID;
        this.container = assignedContainer;
        this.modDisplayName = modDisplayName;
        super(assignedContainer.getX(), 0, width, height, Component.empty());

        for (String location : resourceLocations) {
            if (Minecraft.getInstance().getResourceManager().getResource(Identifier.fromNamespaceAndPath(modID, location)).isPresent()) {
                this.image = Identifier.fromNamespaceAndPath(modID, location);
                return;
            }
        }
        this.image = Identifier.fromNamespaceAndPath(Coreconfig.MOD_ID, "icon.png");
    }

    @Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {

        int width = this.width;
        if (container.scrollable()) width = this.width - 6;

        graphics.fill(this.getX() + 14, this.getY(), this.getX() + width + 10, this.getY() + this.height, 0x55000000);
        graphics.text(
            Minecraft.getInstance().font,
            modDisplayName,
            this.getX() + 50,
            this.getY() + (this.height / 2) - (Minecraft.getInstance().font.lineHeight / 2),
            0xFFFFFFFF,
            true
        );
        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            image,
            this.getX() + 16,
            this.getY() + 1,
            0, 0,
            28, 28,
            28, 28
        );
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {}

}
