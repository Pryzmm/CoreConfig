package com.pryzmm.coreconfig.ui.objects;

import net.minecraft.client.gui.components.AbstractWidget;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface CCElement {
    int getX();
    int getY();
    int getWidth();
    int getHeight();
    @Nullable Integer getColor();
    void visitWidgets(Consumer<AbstractWidget> consumer);

    // Neoforge 1.20.1 (for whatever reason) has a very hard time with getting the proper methods for this class, so remapped methods are here as a fallback.
    default int m_252754_() { return getX(); }
    default int m_252907_() { return getY(); }
    default int m_5711_()   { return getWidth(); }
    default int m_93694_()  { return getHeight(); }
}