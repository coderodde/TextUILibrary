package com.github.coderodde.text.ui.roddelib.listeners;

import javafx.scene.input.TouchEvent;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 27, 2022)
 * @since 1.6 (Jul 27, 2022)
 */
public interface WidgetTouchMoveListener {

    default void onTouchMove(TouchEvent touchEvent, int charX, int charY) {
        
    }
}
