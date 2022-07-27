package com.github.coderodde.text.ui.roddelib.listeners;

import javafx.scene.input.MouseEvent;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public interface WidgetMouseClickListener {

    default void onClick(MouseEvent mouseEvent, int charX, int charY) {
        
    }
}
