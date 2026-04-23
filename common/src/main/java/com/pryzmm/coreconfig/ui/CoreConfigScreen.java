package com.pryzmm.coreconfig.ui;

import com.mojang.blaze3d.platform.InputConstants;
import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.data.CCFileHandler;
import com.pryzmm.coreconfig.data.EntryHolder;
import com.pryzmm.coreconfig.data.HoveredEntry;
import com.pryzmm.coreconfig.ui.option.*;
import com.pryzmm.coreconfig.ui.popup.ColorPopup;
import com.pryzmm.coreconfig.util.ModHolderUtil;
import com.pryzmm.coreconfigapi.entry.*;
import com.pryzmm.coreconfigapi.data.ModData;
import com.pryzmm.coreconfig.ui.objects.*;
import com.pryzmm.coreconfig.ui.popup.AbstractPopup;
import com.pryzmm.coreconfig.ui.popup.ExitWithoutSavingPopup;
import com.pryzmm.coreconfig.ui.popup.RestartPopup;
import com.pryzmm.coreconfigapi.screen.IConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class CoreConfigScreen extends Screen implements IConfigScreen {

    private final List<CCElement> containers = new ArrayList<>();
    public static CoreConfigScreen screen;
    private String activeModID;
    public static AbstractPopup activePopup = null;
    private final List<AbstractWidget> configWidgets = new ArrayList<>();
    private final List<AbstractWidget> modListWidgets = new ArrayList<>();
    private static CCContainer configContainer;
    private static CCContainer modListContainer;
    private static CCButton exitButton;

    /**
     * Opens the CoreConfig screen. If a ModID is provided, it will open the config screen for that specific mod.
     * @param ModID The mod ID of the mod to open the config screen for. If null or empty, the CoreConfig's config screen will be opened.
     */
    public CoreConfigScreen(@NotNull String ModID) {
        super(Component.empty());
        activeModID = ModID;
        screen = this;
    }

    @Override
    protected void init() {
        assert this.minecraft != null;
        assert this.minecraft.screen != null;

        this.containers.clear();
        this.clearWidgets();

        ModData data;
        if (activeModID == null || activeModID.isEmpty()) activeModID = CoreConfigConstants.MOD_ID;
        else if (!ModHolderUtil.getRegisteredMods(false).contains(activeModID)) {
            CoreConfigConstants.LOGGER.warn("Tried to open config screen for mod ID '{}' but it was not found in the registered mods list. Defaulting to CoreConfig config screen.", activeModID);
            activeModID = CoreConfigConstants.MOD_ID;
        }
        data = ModHolderUtil.getModData(activeModID);

        CCButton saveButton = new CCButton(5, minecraft.screen.height - 35, 99, 30, Component.translatable("ui.coreconfig.save"), 0x551A3A1A, true, () -> ModHolderUtil.getRegisteredMods(true).forEach(CCFileHandler::updateConfigFile));
        exitButton = new CCButton(106, minecraft.screen.height - 35, 99, 30, Component.translatable("ui.coreconfig.exit"), 0x553A1A1A, false, () -> {
            if (EntryHolder.containsAnyUpdatedConfigs()) sendExitWithoutSavingPopup();
            else minecraft.setScreen(null);
        });

        CCHeader modListHeader = new CCHeader(this.minecraft, 5, 5, 200, 19, Component.translatable("ui.coreconfig.list"));
        containers.add(modListHeader);

        modListContainer = new CCContainer(5, 25, 200, minecraft.screen.height - 62);
        modListWidgets.clear();
        ModHolderUtil.getRegisteredMods(true).forEach((modID) -> {
            ModData modData = ModHolderUtil.getModData(modID);
            modListWidgets.add(new CCListMod(this, 200, 30, modID, modData.nameTranslation(), modListContainer, 0x551A1A1A));
        });
        modListContainer.populate(modListWidgets);
        containers.add(modListContainer);

        CCHeader configHeader = new CCHeader(this.minecraft, 210, 5, minecraft.screen.width - 215, 19, Component.translatable("ui.coreconfig.config_list", Component.translatable(data.nameTranslation())), data.backgroundColor());
        containers.add(configHeader);

        configContainer = new CCContainer(210, 25, minecraft.screen.width - 215, minecraft.screen.height - 30, data.backgroundColor());
        configWidgets.clear();
        try {
            for (CCEntry entry : EntryHolder.get(activeModID)) {

                     if (entry instanceof MainEntry mainEntry && mainEntry.divider() != null && mainEntry.divider().getFoldedState()) continue;

                     if (entry instanceof BooleanEntry booleanEntry) configWidgets.add(new CCButtonBoolean(booleanEntry, configContainer.ccGetWidth(), 20, booleanEntry.translation(),  configContainer, booleanEntry.hoverColor() != null ? booleanEntry.hoverColor() : 0x55000000, data.buttonColor()));
                else if (entry instanceof StringEntry   stringEntry) configWidgets.add(new CCButtonString(stringEntry,   configContainer.ccGetWidth(), 20, stringEntry.translation(),   configContainer, stringEntry.hoverColor()  != null ? stringEntry.hoverColor()  : 0x55000000, data.buttonColor()));
                else if (entry instanceof IntegerEntry integerEntry) configWidgets.add(new CCButtonInteger(integerEntry, configContainer.ccGetWidth(), 20, integerEntry.translation(),  configContainer, integerEntry.hoverColor() != null ? integerEntry.hoverColor() : 0x55000000, data.buttonColor()));
                else if (entry instanceof FloatEntry     floatEntry) configWidgets.add(new CCButtonFloat(floatEntry,     configContainer.ccGetWidth(), 20, floatEntry.translation(),    configContainer, floatEntry.hoverColor()   != null ? floatEntry.hoverColor()   : 0x55000000, data.buttonColor()));
                else if (entry instanceof DoubleEntry   doubleEntry) configWidgets.add(new CCButtonDouble(doubleEntry,   configContainer.ccGetWidth(), 20, doubleEntry.translation(),   configContainer, doubleEntry.hoverColor()  != null ? doubleEntry.hoverColor()  : 0x55000000, data.buttonColor()));
                else if (entry instanceof LongEntry       longEntry) configWidgets.add(new CCButtonLong(longEntry,       configContainer.ccGetWidth(), 20, longEntry.translation(),     configContainer, longEntry.hoverColor()    != null ? longEntry.hoverColor()    : 0x55000000, data.buttonColor()));
                else if (entry instanceof ColorEntry     colorEntry) configWidgets.add(new CCButtonColor(colorEntry,     configContainer.ccGetWidth(), 20, colorEntry.translation(),    configContainer, colorEntry.hoverColor()   != null ? colorEntry.hoverColor()   : 0x55000000, data.buttonColor()));
                else if (entry instanceof WebsiteEntry websiteEntry) configWidgets.add(new CCButtonWebsite(websiteEntry, configContainer.ccGetWidth(), 20, websiteEntry.translation(),  configContainer, websiteEntry.hoverColor() != null ? websiteEntry.hoverColor() : 0x55000000, data.buttonColor()));
                else if (entry instanceof EnumEntry       enumEntry) configWidgets.add(new CCButtonEnum(enumEntry,       configContainer.ccGetWidth(), 20, enumEntry.translation(),     configContainer, enumEntry.hoverColor()    != null ? enumEntry.hoverColor()    : 0x55000000, data.buttonColor()));
                else if (entry instanceof CustomEntry   customEntry) configWidgets.add(new CCButtonCustom(customEntry,   configContainer.ccGetWidth(), 20, customEntry.translation(),   configContainer, customEntry.hoverColor()  != null ? customEntry.hoverColor()  : 0x55000000, data.buttonColor()));
                else if (entry instanceof DividerEntry dividerEntry) configWidgets.add(new CCDivider(dividerEntry, configContainer.ccGetWidth(), configWidgets.isEmpty() ? 20 : 30, dividerEntry.translation(), configContainer, dividerEntry.textColor()));
                else CoreConfigConstants.LOGGER.warn("Unsupported config entry type: {}", entry == null ? "null" : entry.getClass());
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

    public void sendRestartPopup() {
        Screen screen = Minecraft.getInstance().screen;
        assert screen != null;
        activePopup = new RestartPopup(Minecraft.getInstance(), 200, 100, screen);
    }

    public static void sendExitWithoutSavingPopup() {
        Screen screen = Minecraft.getInstance().screen;
        assert screen != null;
        activePopup = new ExitWithoutSavingPopup(Minecraft.getInstance(), 200, 100, screen);
    }

    public void sendColorPopup(ColorEntry entry) {
        Screen screen = Minecraft.getInstance().screen;
        assert screen != null;
        activePopup = new ColorPopup(Minecraft.getInstance(), 200, 150, screen, entry);
    }

    public void updateConfigScreen(String modID) {
        activeModID = modID;
        this.init();
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int button) {
        if (activePopup instanceof ColorPopup popup) popup.mouseReleased(pMouseX, pMouseY, button);
        return super.mouseReleased(pMouseX, pMouseY, button);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int button) {
        if (activePopup != null) {
            if (activePopup.isMouseOver(pMouseX, pMouseY)) activePopup.mouseClicked(pMouseX, pMouseY, button);
            return false;
        }
        double mouseYModList = pMouseY + modListContainer.getLayout().getScrollAmount();
        double mouseYOptions = pMouseY + configContainer.getLayout().getScrollAmount();
        try {
            for (AbstractWidget widget : configWidgets) {
                if (widget instanceof CCButtonString stringWidget) stringWidget.updateFocus(pMouseX, mouseYOptions);
                else if (widget instanceof CCButtonInteger integerWidget) integerWidget.updateFocus(pMouseX, mouseYOptions);
                else if (widget instanceof CCButtonFloat floatWidget) floatWidget.updateFocus(pMouseX, mouseYOptions);
                else if (widget instanceof CCButtonDouble doubleWidget) doubleWidget.updateFocus(pMouseX, mouseYOptions);
                else if (widget instanceof CCButtonLong longWidget) longWidget.updateFocus(pMouseX, mouseYOptions);
                widget.mouseClicked(pMouseX, mouseYOptions, button);
            }
        } catch (ConcurrentModificationException ignored) {}
        for (AbstractWidget widget : modListWidgets) widget.mouseClicked(pMouseX, mouseYModList, button);
        return super.mouseClicked(pMouseX, pMouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (activePopup != null) {
            activePopup.keyPressed(keyCode, scanCode, modifiers);
            return false;
        }
        if (keyCode == InputConstants.KEY_ESCAPE) {
            boolean anyFocused = configWidgets.stream().anyMatch(w ->
                (w instanceof CCButtonFloat f && f.isEditBoxFocused()) ||
                (w instanceof CCButtonString s && s.isEditBoxFocused()) ||
                (w instanceof CCButtonInteger i && i.isEditBoxFocused()) ||
                (w instanceof CCButtonDouble d && d.isEditBoxFocused()) ||
                (w instanceof CCButtonLong l && l.isEditBoxFocused())
            );
            if (!anyFocused) return exitButton.mouseClicked(exitButton.ccGetX() + 1, exitButton.ccGetY() + 1, 0);
        }
        for (AbstractWidget widget : configWidgets) widget.keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (activePopup != null) return false;
        for (AbstractWidget widget : configWidgets) widget.charTyped(codePoint, modifiers);
        return super.charTyped(codePoint, modifiers);
    }

    public void acceptClosedPopup() {
        this.removeWidget(activePopup);
        activePopup = null;
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        containers.forEach(container -> graphics.fill(
            container.ccGetX(),
            container.ccGetY(),
            container.ccGetX() + container.ccGetWidth(),
            container.ccGetY() + container.ccGetHeight(),
            container.getColor() != null ? container.getColor() : 0x551A1A1A
        ));
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float a) {
        renderBackground(graphics, mouseX, mouseY, a);
        super.render(graphics, mouseX, mouseY, a);
        CCTooltip.render(graphics, mouseX, mouseY, HoveredEntry.value, configContainer);
    }

    public <T extends AbstractWidget> void addWidget(T widget) {
        addRenderableWidget(widget);
    }

}