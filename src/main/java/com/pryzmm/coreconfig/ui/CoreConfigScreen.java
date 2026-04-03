package com.pryzmm.coreconfig.ui;

import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.client.CoreconfigClient;
import com.pryzmm.coreconfig.data.CCFileHandler;
import com.pryzmm.coreconfig.data.EntryHolder;
import com.pryzmm.coreconfig.data.ModData;
import com.pryzmm.coreconfig.data.ModHolder;
import com.pryzmm.coreconfig.entry.BooleanEntry;
import com.pryzmm.coreconfig.entry.CCEntry;
import com.pryzmm.coreconfig.entry.IntegerEntry;
import com.pryzmm.coreconfig.entry.StringEntry;
import com.pryzmm.coreconfig.ui.objects.*;
import com.pryzmm.coreconfig.ui.option.CCButtonBoolean;
import com.pryzmm.coreconfig.ui.option.CCButtonInteger;
import com.pryzmm.coreconfig.ui.option.CCButtonString;
import com.pryzmm.coreconfig.ui.popup.AbstractPopup;
import com.pryzmm.coreconfig.ui.popup.ExitWithoutSavingPopup;
import com.pryzmm.coreconfig.ui.popup.RestartPopup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class CoreConfigScreen extends Screen {

    private final List<CCElement> containers = new ArrayList<>();
    private String activeModID;
    private static AbstractPopup activePopup = null;
    private final List<AbstractWidget> configWidgets = new ArrayList<>();
    private static CCContainer configContainer;

    /**
     * Opens the CoreConfig screen. If a ModID is provided, it will open the config screen for that specific mod.
     * @param ModID The mod ID of the mod to open the config screen for. If null or empty, the CoreConfig's config screen will be opened.
     */
    public CoreConfigScreen(@NotNull String ModID) {
        super(Component.empty());
        activeModID = ModID;
    }

    @Override
    protected void init() {
        assert this.minecraft.screen != null;

        this.containers.clear();
        this.clearWidgets();

        ModData data = ModHolder.getModData(activeModID);

        CCButton saveButton = new CCButton(5, minecraft.screen.height - 35, 99, 30, Component.translatable("ui.coreconfig.save"), 0x551A3A1A, true, () -> ModHolder.getRegisteredMods(true).forEach(CCFileHandler::updateConfigFile));
        CCButton exitButton = new CCButton(106, minecraft.screen.height - 35, 99, 30, Component.translatable("ui.coreconfig.exit"), 0x553A1A1A, false, () -> {
            if (EntryHolder.containsAnyUpdatedConfigs()) sendExitWithoutSavingPopup();
            else minecraft.setScreen(null);
        });

        CCHeader modListHeader = new CCHeader(this.minecraft, 5, 5, 200, 19, Component.translatable("ui.coreconfig.list"));
        containers.add(modListHeader);

        CCContainer modListContainer = new CCContainer(this.minecraft, 5, 25, 200, minecraft.screen.height - 62);
        List<AbstractWidget> modListWidgets = new ArrayList<>();
        ModHolder.getRegisteredMods(true).forEach((identifier) -> modListWidgets.add(new CCListMod(this, 200, 30, Identifier.fromNamespaceAndPath(identifier.getNamespace(), identifier.getPath()), modListContainer, 0x551A1A1A)));
        modListContainer.populate(modListWidgets);
        containers.add(modListContainer);

        Identifier identifier = ModHolder.getModByNamespace(activeModID);
        CCHeader configHeader = new CCHeader(this.minecraft, 210, 5, minecraft.screen.width - 215, 19, Component.translatable("ui.coreconfig.config_list", Component.translatable(identifier.getPath())), data.backgroundColor());
        containers.add(configHeader);

        configContainer = new CCContainer(this.minecraft, 210, 25, minecraft.screen.width - 215, minecraft.screen.height - 30, data.backgroundColor());
        configWidgets.clear();
        try {
            for (CCEntry entry : EntryHolder.get(activeModID)) {
                switch (entry) {
                    case BooleanEntry booleanEntry -> configWidgets.add(new CCButtonBoolean(booleanEntry, configContainer.getWidth(), 20, booleanEntry.identifier(), configContainer, booleanEntry.hoverColor() != null ? booleanEntry.hoverColor() : 0x55000000, data.buttonColor()));
                    case StringEntry stringEntry -> configWidgets.add(new CCButtonString(stringEntry, configContainer.getWidth(), 20, stringEntry.identifier(), configContainer, stringEntry.hoverColor() != null ? stringEntry.hoverColor() : 0x55000000, data.buttonColor()));
                    case IntegerEntry integerEntry -> configWidgets.add(new CCButtonInteger(integerEntry, configContainer.getWidth(), 20, integerEntry.identifier(), configContainer, integerEntry.hoverColor() != null ? integerEntry.hoverColor() : 0x55000000, data.buttonColor()));
                    case null, default -> CoreConfigConstants.LOGGER.warning("Unsupported config entry type: " + (entry == null ? "null" : entry.getClass()));
                }
            }
        } catch (Exception ignored) {}
        configContainer.populate(configWidgets);
        containers.add(configContainer);

        saveButton.visitWidgets(this::addRenderableWidget);
        exitButton.visitWidgets(this::addRenderableWidget);
        modListHeader.visitWidgets(this::addRenderableWidget);
        modListContainer.visitWidgets(this::addRenderableWidget);
        configHeader.visitWidgets(this::addRenderableWidget);
        configContainer.visitWidgets(this::addRenderableWidget);

    }

    public static void sendRestartPopup() {
        Screen screen = Minecraft.getInstance().screen;
        assert screen != null;
        activePopup = new RestartPopup(Minecraft.getInstance(), 200, 100, screen);
    }

    public static void sendExitWithoutSavingPopup() {
        Screen screen = Minecraft.getInstance().screen;
        assert screen != null;
        activePopup = new ExitWithoutSavingPopup(Minecraft.getInstance(), 200, 100, screen);
    }

    public void updateConfigScreen(String modID) {
        activeModID = modID;
        this.init();
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean doubleClick) {
        if (activePopup != null) {
            if (activePopup.isMouseOver(event.x(), event.y())) activePopup.mouseClicked(event, doubleClick);
            return false;
        }
        for (AbstractWidget widget : configWidgets) {
            if (widget instanceof CCButtonString stringWidget) stringWidget.updateFocus(event);
            else if (widget instanceof CCButtonInteger integerWidget) integerWidget.updateFocus(event);
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean keyPressed(@NotNull KeyEvent event) {
        if (activePopup != null) {
            activePopup.keyPressed(event);
            return false;
        }
        return super.keyPressed(event);
    }

    public void acceptClosedPopup() {
        this.removeWidget(activePopup);
        activePopup = null;
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

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        containers.forEach(container -> {
            if (container.getColor() != null) graphics.fill(
                container.getX(),
                container.getY(),
                container.getX() + container.getWidth(),
                container.getY() + container.getHeight(),
                container.getColor()
            );
        });
        super.extractRenderState(graphics, mouseX, mouseY, a);
        if (CoreconfigClient.hoveredEntry != null) {
            graphics.nextStratum();
            CCTooltip.render(graphics, mouseX, mouseY, CoreconfigClient.hoveredEntry, configContainer);
        }
    }

}
