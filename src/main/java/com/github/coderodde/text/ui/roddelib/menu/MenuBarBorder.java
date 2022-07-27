package com.github.coderodde.text.ui.roddelib.menu;

import com.github.coderodde.text.ui.roddelib.BorderThickness;
import java.util.Objects;
import javafx.scene.paint.Color;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public class MenuBarBorder {

    private static final Color DEFAULT_FOREGROUND_COLOR = Color.RED;
    private static final Color DEFAULT_BACKGROUND_COLOR = 
            new Color(100, 100, 100, 1);
    
    private BorderThickness leftVerticalBorderThickness =
            BorderThickness.SINGLE;
    
    private BorderThickness rightVerticalBorderThickness = 
            BorderThickness.SINGLE;
    
    private BorderThickness topUpperHorizontalBorderThickness = 
            BorderThickness.DOUBLE;
    
    private BorderThickness topLowerHorizontalBorderThickness =
            BorderThickness.SINGLE;
    
    private BorderThickness bottomHorizontalBorderThickness = 
            BorderThickness.DOUBLE;
    
    private Color foregroundColor = DEFAULT_FOREGROUND_COLOR;
    
    private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
    
    public Color getForegroundColor() {
        return foregroundColor;
    }
    
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    
    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = 
                Objects.requireNonNull(
                        foregroundColor, 
                        "The input foreground color is null.");
    }
    
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = 
                Objects.requireNonNull(
                        backgroundColor, 
                        "The input background color is null.");
    }
}
