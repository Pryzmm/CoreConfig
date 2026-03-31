package com.pryzmm.coreconfig.ui;

import com.pryzmm.coreconfig.Coreconfig;
import com.pryzmm.coreconfig.ui.objects.*;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class CoreConfigScreen extends Screen {

    private final List<CCElement> containers = new ArrayList<>();
    private String activeModID;

    /**
     * Opens the CoreConfig screen. If a ModID is provided, it will open the config screen for that specific mod.
     * @param ModID The mod ID of the mod to open the config screen for. If null or empty, the CoreConfig's config screen will be opened.
     */
    public CoreConfigScreen(String ModID) {
        super(Component.empty());
        activeModID = ModID;
    }

    @Override
    protected void init() {
        assert this.minecraft.screen != null;

        CCHeader header = new CCHeader(this.minecraft, 5, 5, 200, 19, Component.translatable("ui.coreconfig.list"));
        containers.add(header);
        header.visitWidgets(this::addRenderableWidget);

        CCContainer modListContainer = new CCContainer(this.minecraft, 5, 25, 200, minecraft.screen.height - 62);
        List<AbstractWidget> modListWidgets = new ArrayList<>(List.of(new CCListMod(200, 30, Coreconfig.MOD_ID, "Core Config", modListContainer)));
        modListContainer.populate(modListWidgets);
        containers.add(modListContainer);
        modListContainer.visitWidgets(this::addRenderableWidget);

        CCButton saveButton = new CCButton(5, minecraft.screen.height - 35, 99, 30, Component.translatable("ui.coreconfig.save"));
        saveButton.visitWidgets(this::addRenderableWidget);

        CCButton exitButton = new CCButton(106, minecraft.screen.height - 35, 99, 30, Component.translatable("ui.coreconfig.exit"));
        exitButton.visitWidgets(this::addRenderableWidget);
    }

    @Override
    public void extractBackground(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        containers.forEach(container -> graphics.fill(
            container.getX(),
            container.getY(),
            container.getX() + container.getWidth(),
            container.getY() + container.getHeight(),
            0x551A1A1A
        ));
    }

}
