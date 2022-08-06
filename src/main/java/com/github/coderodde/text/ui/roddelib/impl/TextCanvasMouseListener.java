package com.github.coderodde.text.ui.roddelib.impl;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public interface TextCanvasMouseListener {

    default void onMouseClick(MouseEvent e, int charX, int charY) {
        
    }
    
    default void onMouseEntered(MouseEvent e, int charX, int charY) {
    
    }
    
    default void onMouseExited(MouseEvent e, int charX, int charY) {
        
    }
    
    default void onMousePressed(MouseEvent e, int charX, int charY) {
        
    }
    
    default void onMouseReleased(MouseEvent e, int charX, int charY) {
        
    }
    
    default void onMouseMoved(MouseEvent e, int charX, int charY) {
        
    }
    
    default void onMouseDragged(MouseEvent e, int charX, int charY) {
        
    }
    
    default void onMouseScroll(ScrollEvent e, int charX, int charY) {
        
    }
}
