package com.github.coderodde.text.ui.roddelib.menu;

import com.github.coderodde.text.ui.roddelib.AbstractWidget;
import com.github.coderodde.text.ui.roddelib.BorderThickness;
import static com.github.coderodde.text.ui.roddelib.BorderThickness.DOUBLE;
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
        buildCharMatrixIfNeeded();
        int parentWindowWidth = parentWidget.getWidth();
        paintMenuBarBorder();
    }
    
    private void buildCharMatrixIfNeeded() {
        int requestedHeight =
                1 + 
                (!menuBarBorder.getTopHorizontalBorderThickness()
                               .equals(BorderThickness.NONE) ? 1 : 0)
                + 
                (!menuBarBorder.getBottomHorizontalBorderThickness()
                               .equals(BorderThickness.NONE) ? 1 : 0);
        
        int requestedWidth = parentWidget.getWidth();
        
        if (requestedHeight != charMatrix.length ||
            requestedWidth != charMatrix[0].length) {
            this.charMatrix = new char[requestedHeight][requestedWidth];
            
            if (!menuBarBorder.getTopHorizontalBorderThickness()
                              .equals(BorderThickness.NONE)) {
                buildTopBorder();
            }
            
            if (!menuBarBorder.getBottomHorizontalBorderThickness()
                              .equals(BorderThickness.NONE)) {
                buildBottomBorder();
            }
        }
    }
    
    private void buildTopBorder() {
        char borderChar;
        char separatorIntersectionChar;
        BorderThickness separatorThickness = 
                menuBarBorder.getMenuSeparatorBorderThickness();
        
        switch (menuBarBorder.getTopHorizontalBorderThickness()) {
            case DOUBLE:
                borderChar = '\u2550';
                
                switch (separatorThickness) {
                    case DOUBLE:
                        separatorIntersectionChar = '\u2566';
                        break;
                        
                    case SINGLE:
                        separatorIntersectionChar = '\u2564';
                        break;
                        
                    default:
                        throw new IllegalStateException("Not allowed here: " + 
                            menuBarBorder.getMenuSeparatorBorderThickness());
                }
                
                break;
                
            case SINGLE:
                borderChar = '\u2500';
                
                switch (separatorThickness) {
                    case DOUBLE:
                        separatorIntersectionChar = '\u2565';
                        break;
                        
                    case SINGLE:
                        separatorIntersectionChar = '\u252c';
                        break;
                        
                    default:
                        throw new IllegalStateException("Not allowed here: " + 
                            menuBarBorder.getMenuSeparatorBorderThickness());
                }
                
                break;
                
            default:
                throw new IllegalStateException(
                    "Not allowed here: " + 
                            menuBarBorder.getTopHorizontalBorderThickness());
        }
        
        int currentMenuIndex = 0;
        int currentPositionInCurrentMenuText = 0;
        Menu currentMenu = (Menu) children.get(0);
        
        // Print the actual bar:
        for (int x = 1; x < parentWidget.getWidth() - 1; x++) {
            if (currentPositionInCurrentMenuText++ == currentMenu.getWidth()) {
                currentPositionInCurrentMenuText = 0;
                currentMenuIndex++;
                currentMenu = (Menu) children.get(currentMenuIndex);
                charMatrix[0][x] = separatorIntersectionChar;
            } else {
                charMatrix[0][x] = borderChar;
            }
        }
        
        // Print the top left corner:
        boolean topHorizontalDouble = 
                menuBarBorder.getTopHorizontalBorderThickness().equals(DOUBLE);
        
        boolean leftVerticalDouble = 
                menuBarBorder.getLeftVerticalBorderThickness().equals(DOUBLE);
        
        if (topHorizontalDouble) {
            if (leftVerticalDouble) {
                borderChar = '\u2554';
            } else {
                borderChar = '\u2552';
            }
        } else {
            if (leftVerticalDouble) {
                borderChar = '\u2556';
            }  else {
                borderChar = '\u250c';
            }
        }
        
        charMatrix[0][0] = borderChar;
        
        // Print the top right corner:
        boolean rightVerticalDouble = 
                menuBarBorder.getRightVerticalBorderThickness().equals(DOUBLE);
        
        if (topHorizontalDouble) {
            if (rightVerticalDouble) {
                
            } else {
                
            }
        } else {
            if (rightVerticalDouble) {
                
            } else {
                
            }
        }
        
        charMatrix[0][width - 1] = borderChar;
    }
    
    private void buildBottomBorder() {
        char borderChar;
        
        switch (menuBarBorder.getTopHorizontalBorderThickness()) {
            case DOUBLE:
                borderChar = '\u00cd';
                break;
                
            case SINGLE:
                borderChar = '\u00c4';
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
