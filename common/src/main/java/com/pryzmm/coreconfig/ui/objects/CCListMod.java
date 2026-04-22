package com.pryzmm.coreconfig.ui.objects;

import com.pryzmm.coreconfig.util.ModHolderUtil;
import com.pryzmm.coreconfigapi.data.ModData;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class CCListMod extends AbstractWidget {

    private final String modID;
    private final String imagePath;
    private final CCContainer container;
    private final String nameTranslation;
    private final int hoverColor;

    final CoreConfigScreen screen;

    List<String> resourceLocations = List.of(
        "icon.png",
        "textures/icon.png"
    );

    /**
     * @param width The width of the widget. This is decreased by 6 automatically if the container is scrollable. This is also decreased further automatically for ease of use.
     */
    public CCListMod(CoreConfigScreen screen, int width, int height, String modID, String nameTranslation, CCContainer assignedContainer, int hoverColor) {
        super(0, 0, width - 4, height, Component.empty());
        this.modID = modID;
        this.container = assignedContainer;
        this.nameTranslation = nameTranslation;
        this.hoverColor = hoverColor;
        this.screen = screen;

        ModData data = ModHolderUtil.getModData(modID);
        if (data.overrideIconPath() == null) {
            for (String location : resourceLocations) {
                if (Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(modID, location)).isPresent()) {
                    this.imagePath = location;
                    return;
                }
            }
            this.imagePath = "textures/ui/icon/missing.png";
            return;
        } else if (Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(modID, data.overrideIconPath())).isPresent()) {
            this.imagePath = data.overrideIconPath();
            return;
        }
        this.imagePath = "textures/ui/icon/missing.png";
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float a) {

        int width = this.width;
        if (container.scrollable()) width = this.width - 6;

        ModData data = ModHolderUtil.getModData(modID);
        if (data.bannerPath() != null) graphics.blit(new ResourceLocation(data.modID(), data.bannerPath()), this.getX(), this.getY(), 0, 0, width, height, width, height);
        if (this.isHovered && CoreConfigScreen.activePopup == null) graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, hoverColor);
        else if (data.bannerPath() == null) graphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.height, 0x55000000);

        graphics.drawString(
            Minecraft.getInstance().font,
            Component.translatable(nameTranslation),
            this.getX() + 36,
            this.getY() + (this.height / 2) - (Minecraft.getInstance().font.lineHeight / 2),
            0xFFFFFFFF,
            true
        );
        graphics.blit(
            new ResourceLocation(modID, imagePath),
            this.getX() + 1,
            this.getY() + 1,
            0, 0,
            28, 28,
            28, 28
        );
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        screen.updateConfigScreen(modID);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {}

}
