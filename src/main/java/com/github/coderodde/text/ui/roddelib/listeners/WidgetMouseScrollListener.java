package com.github.coderodde.text.ui.roddelib.listeners;

import javafx.scene.input.ScrollEvent;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 27, 2022)
 * @since 1.6 (Jul 27, 2022)
 */
public interface WidgetMouseScrollListener {

    default void onScroll(ScrollEvent scrollEvent, int charX, int charY) {
        
    }
}
