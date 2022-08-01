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
    
    /**
     * The border thickness of the left vertical border of the entire menu bar.
     */
    private BorderThickness leftVerticalBorderThickness =
            BorderThickness.DOUBLE;
    
    /**
     * The border thickness of the right vertical border of the entire menu bar.
     */
    private BorderThickness rightVerticalBorderThickness = 
            BorderThickness.DOUBLE;
    
    /**
     * The border thickness of the top horizontal border of the entire menu bar.
     */
    private BorderThickness topHorizontalBorderThickness = 
            BorderThickness.DOUBLE;
    
    /**
     * The border thickness of the bottom horizontal border of the entire menu
     * bar.
     */
    private BorderThickness bottomHorizontalBorderThickness = 
            BorderThickness.DOUBLE;
    
    /**
     * The border thickness of the menu separator between two adjacent menus in
     * the menu bar.
     */
    private BorderThickness menuSeparatorBorderThickness =
            BorderThickness.SINGLE;
    
    /**
     * The foreground color of the menu bar. Used to draw the characters 
     * comprising the border.
     */
    private Color foregroundColor = DEFAULT_FOREGROUND_COLOR;
    
    /**
     * The background color of the menu text. Used to draw the character
     * background in the border.
     */
    private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
    
    public boolean noActualBorder() {
        return topHorizontalBorderThickness.equals(BorderThickness.NONE) &&
               bottomHorizontalBorderThickness.equals(BorderThickness.NONE) &&
               leftVerticalBorderThickness.equals(BorderThickness.NONE) &&
               rightVerticalBorderThickness.equals(BorderThickness.NONE) &&
               menuSeparatorBorderThickness.equals(BorderThickness.NONE);
    }
    
    public Color getForegroundColor() {
        return foregroundColor;
    }
    
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    
    public int getHeight() {
        int height = 0;
        
        if (!topHorizontalBorderThickness.equals(BorderThickness.NONE)) {
            height = 1;
        }
        
        if (!bottomHorizontalBorderThickness.equals(BorderThickness.NONE)) {
            height++;
        }
        
        return height;
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

    public BorderThickness getLeftVerticalBorderThickness() {
        return leftVerticalBorderThickness;
    }

    public void setLeftVerticalBorderThickness(
            BorderThickness leftVerticalBorderThickness) {
        this.leftVerticalBorderThickness = 
                Objects.requireNonNull(
                        leftVerticalBorderThickness, 
                        "The input leftVerticalBorderThickness is null.");
    }

    public BorderThickness getRightVerticalBorderThickness() {
        return rightVerticalBorderThickness;
    }

    public void setRightVerticalBorderThickness(
            BorderThickness rightVerticalBorderThickness) {
        this.rightVerticalBorderThickness =
                Objects.requireNonNull(
                        rightVerticalBorderThickness, 
                        "The input rightVerticalBorderThickness is null.");
    }

    public BorderThickness getTopHorizontalBorderThickness() {
        return topHorizontalBorderThickness;
    }

    public void setTopHorizontalBorderThickness(
            BorderThickness topHorizontalBorderThickness) {
        this.topHorizontalBorderThickness = 
                Objects.requireNonNull(
                        topHorizontalBorderThickness, 
                        "The iniput topHorizontalBorderThickness is null.");
    }

    public BorderThickness getBottomHorizontalBorderThickness() {
        return bottomHorizontalBorderThickness;
    }

    public void setBottomHorizontalBorderThickness(
            BorderThickness bottomHorizontalBorderThickness) {
        this.bottomHorizontalBorderThickness = 
                Objects.requireNonNull(
                        bottomHorizontalBorderThickness, 
                        "The input bottomHorizontalBorderThickness is null.");
    }

    public BorderThickness getMenuSeparatorBorderThickness() {
        return menuSeparatorBorderThickness;
    }

    public void setMenuSeparatorBorderThickness(
            BorderThickness menuSeparatorBorderThickness) {
        this.menuSeparatorBorderThickness = 
                Objects.requireNonNull(
                        menuSeparatorBorderThickness, 
                        "The input menuSeparatorBorderThickness is null.");
    }
}
