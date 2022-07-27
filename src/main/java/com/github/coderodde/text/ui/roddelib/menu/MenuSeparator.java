package com.github.coderodde.text.ui.roddelib.menu;

import com.github.coderodde.text.ui.roddelib.AbstractWidget;
import java.util.Objects;
import javafx.scene.paint.Color;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public class MenuSeparator extends AbstractWidget {
    
    private static final Color DEFAULT_FOREGROUND_COLOR = Color.RED;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.GREY;
    private static final int POSITION_INDEX_INVALID = -1;
    
    protected Color foregroundColor = DEFAULT_FOREGROUND_COLOR;
    protected Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
    protected int positionIndexInParentMenu = POSITION_INDEX_INVALID;
    
    
    
    MenuSeparator(MenuSeparator other) {
        this.foregroundColor = other.foregroundColor;
        this.backgroundColor = other.backgroundColor;
    }
    
    public void setForegroundColor(Color color) {
        this.foregroundColor = 
                Objects.requireNonNull(color, "The foreground color is null.");
    }
    
    public void setBackgroundColor(Color color) {
        this.backgroundColor = 
                Objects.requireNonNull(color, "The foreground color is null.");
    }
    
    public void paint() {
        Objects.requireNonNull(parentWidget, "The parent widget is null.");
        
        int startX = parentWidget.getStartX();
        int startY = parentWidget.getStartY() + positionIndexInParentMenu;
        
        
    }
    
    @Override
    public int getWidth() {
        return parentWidget.getWidth();
    }
    
    public int getHeight() {
        return 1; // MenuSeparator takes only one text row.
    }
    
    void setPositionIndexInMenu(int posIndex) {
        this.positionIndexInParentMenu = posIndex;
    }
}
