package com.github.coderodde.text.ui.roddelib.menu;

import com.github.coderodde.text.ui.roddelib.AbstractWidget;
import com.github.coderodde.text.ui.roddelib.BorderThickness;
import com.github.coderodde.text.ui.roddelib.Window;
import com.github.coderodde.text.ui.roddelib.impl.TextUIWindow;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.scene.paint.Color;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 27, 2022)
 * @since 1.6 (Jul 27, 2022)
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
    
    private char[][] charMatrix = new char[0][0];
    private boolean isDirty = true;
    
    /**
     * The menu bar border.
     */
    private MenuBarBorder menuBarBorder;
    
    /**
     * The scroll value. The menu bar omits {@code scrollX} character positions
     * from the left of the menu bar.
     */
    private int scrollX;
    
    /**
     * The maximum value for {@code scrollX}. At this value, the menu bar is 
     * aligned to the right end.
     */
    private int maximumScrollY;
    
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
    
    public void paint(TextUIWindow windowImpl) {
        if (!isDirty) {
            return;
        }
        
        List<Menu> menuItems = new ArrayList<>(children.size());
        
        for (AbstractWidget menuWidget : children) {
            menuItems.add((Menu) menuWidget);
        }
        
        int totalWidth = getTotalWidth();
        maximumScrollY = Math.max(0, totalWidth - windowImpl.getGridWidth());
        
        paintMenuBar();
        isDirty = false;
    }
    
    private int getTotalWidth() {
        int width = 0;
        
        for (AbstractWidget menu : children) {
            width += menu.getWidth();
        }
        
        // Add menu separators, 1 char in width each.
        width += children.size() - 1;
        
        if (menuBarBorder != null) {
            if (!menuBarBorder.getLeftVerticalBorderThickness()
                              .equals(BorderThickness.NONE)) {
                width++;
            }
            
            if (!menuBarBorder.getRightVerticalBorderThickness()
                              .equals(BorderThickness.NONE)) {
                width++;
            }
        }
        
        return width;
    }
    
    private void paintMenuBar() {
        if (charMatrix == null || charMatrixDimensionMismatch()) {
//            createCharMatrix();
        }
        
        
        
        int parentWindowWidth = parentWidget.getWidth();
        paintMenuBarBorder();
    }
    
    private boolean charMatrixDimensionMismatch() {
        int requestedHeight =
                1 + 
                (!menuBarBorder.getTopHorizontalBorderThickness()
                               .equals(BorderThickness.NONE) ? 1 : 0)
                + 
                (!menuBarBorder.getBottomHorizontalBorderThickness()
                               .equals(BorderThickness.NONE) ? 1 : 0);
        
        int requestedWidth = parentWidget.getWidth();
        
        return requestedHeight != charMatrix.length ||
               requestedWidth != charMatrix[0].length;
    }
    
    private void buildCharMatrix(int width, int height) {
        this.charMatrix = new char[height][width];
        
        if (!menuBarBorder.getTopHorizontalBorderThickness()
                          .equals(BorderThickness.NONE)) {
            buildTopBorder();
        }
    }
    
    private void buildTopBorder() {
        char borderChar;
        
        switch (menuBarBorder.getTopHorizontalBorderThickness()) {
            case DOUBLE:
                borderChar = 'd';
                break;
                
            case SINGLE:
                borderChar = 'd';
                break;
                
            default:
                throw new IllegalStateException(
                    "Not allowed here: " + 
                            menuBarBorder.getTopHorizontalBorderThickness());
        }
        
        for (int x = 1; x < parentWidget.getWidth() - 1; x++) {
            charMatrix[0][x] = borderChar;
        }
    }
    
    private void paintMenuBarBorder() {
        
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
        isDirty = true;
    }
    
    
    
    @Override
    public void setParent(AbstractWidget windowCandidate) {
        Objects.requireNonNull(windowCandidate);
        
        if (!(windowCandidate instanceof Window)) {
            throw new IllegalArgumentException(
                    "Only Window is allowed as a parent widget.");
        }
        
        super.setParent(windowCandidate);
    }
}
