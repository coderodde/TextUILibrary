package com.github.coderodde.text.ui.roddelib;

import java.util.Objects;
import javafx.scene.paint.Color;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public class MenuBar extends AbstractWidget {
    
    private static final Color DEFAULT_FOREGROUND_COLOR = Color.RED;
    private static final Color DEFAULT_BACKGROUND_COLOR = 
            new Color(100, 100, 100, 1);
    
    private static final Color DEFAULT_FOREGROUND_COLOR_ON_HOVER =
            DEFAULT_BACKGROUND_COLOR;

    private static final Color DEFAULT_BACKGROUND_COLOR_ON_HOVER = 
            DEFAULT_FOREGROUND_COLOR;
    
    protected Color onHoverForegroundColor = DEFAULT_FOREGROUND_COLOR_ON_HOVER;
    protected Color onHoverBackgroundColor = DEFAULT_BACKGROUND_COLOR_ON_HOVER;
    
    public MenuBar() {
        this.setForegroundColor(DEFAULT_FOREGROUND_COLOR);
        this.setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
    }
    
    public void setOnHoverForegroundColor(Color onHoverForegroundColor) {
        this.onHoverForegroundColor =
                Objects.requireNonNull(
                        onHoverForegroundColor, 
                        "The input onHoverForegroundColor is null.");
    }
    
    public void setOnHoverBackgroundColor(Color onHoverBackgroundColor) {
        this.onHoverBackgroundColor =
                Objects.requireNonNull(
                        onHoverBackgroundColor, 
                        "The input onHoverBackgroundColor is null.");
    }
    
    public Color getOnHoverForegroundColor() {
        return onHoverForegroundColor;
    }
    
    public Color getOnHoverBackgroundColor() {
        return onHoverBackgroundColor;
    }
    
    private int scrollX;
    
    public void paint() {
        paintMenu();
    }
    
    private void paintMenu() {
        int parentWindowWidth = parentWidget.getWidth();
        
        
    }
    
    private int getMinimumFittingWidth() {
        int width = 0;
        
        
        
        return width;
    }
    
    @Override
    public void addChildren(AbstractWidget firstWidget, 
                            AbstractWidget... otherWidgets) {
        throw new UnsupportedOperationException(
                "Use addMenu(Menu menu) instead.");
    }
    
    public void addMenu(Menu menu) {
        addChildren(Objects.requireNonNull(menu, "The input Menu is null."));
    }
    
    void setParentWindow(Window parentWindow) {
        this.parentWidget = parentWindow;
    }
}
