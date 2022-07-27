package com.github.coderodde.text.ui.roddelib.impl;

import javafx.scene.input.TouchEvent;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 27, 2022)
 * @since 1.6 (Jul 17, 2022)
 */
public interface TextUIWindowTouchListener {

    default void onTouchMove(TouchEvent touchEvent, int charX, int charY) {
        
    }
}
