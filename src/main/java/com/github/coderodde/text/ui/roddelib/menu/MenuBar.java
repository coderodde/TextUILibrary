package com.github.coderodde.text.ui.roddelib.menu;

import com.github.coderodde.text.ui.roddelib.AbstractWidget;
import com.github.coderodde.text.ui.roddelib.BorderThickness;
import static com.github.coderodde.text.ui.roddelib.BorderThickness.NONE;
import com.github.coderodde.text.ui.roddelib.Window;
import com.github.coderodde.text.ui.roddelib.impl.TextCanvas;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import com.github.coderodde.text.ui.roddelib.impl.TextCanvasMouseListener;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 27, 2022)
 * @since 1.6 (Jul 27, 2022)
 */
public class MenuBar extends AbstractWidget {
    
    private static final Color DEFAULT_FOREGROUND_COLOR = Color.WHITE;
    private static final Color DEFAULT_BACKGROUND_COLOR = 
            new Color(0.55, 0.55, 0.55, 1);
    
    private static final Color DEFAULT_FOREGROUND_COLOR_ON_HOVER = Color.ORANGE;

    private static final Color DEFAULT_BACKGROUND_COLOR_ON_HOVER = 
            DEFAULT_BACKGROUND_COLOR;
    
    protected Color onHoverForegroundColor = DEFAULT_FOREGROUND_COLOR_ON_HOVER;
    protected Color onHoverBackgroundColor = DEFAULT_BACKGROUND_COLOR_ON_HOVER;
    
    private char[][] charMatrix = new char[0][0];
    private Color[][] foregroundColorMatrix;
    private Color[][] backgroundColorMatrix;
    private boolean isDirty = true;
    private final MenuBarMouseListenerImpl mouseListenerImpl = 
            new MenuBarMouseListenerImpl();
    
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
    private int maximumScrollX;
    
    public MenuBar() {
        this.setForegroundColor(DEFAULT_FOREGROUND_COLOR);
        this.setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
        super.mouseListener = new MenuBarMouseListenerImpl();
    }
    
    public void addMenuBarBorder(MenuBarBorder menuBarBorder) {
        buildCharMatrixIfNeeded();
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
    
    public void paint(TextCanvas windowImpl) {
        if (!isDirty) {
            return;
        }
        
        List<Menu> menuItems = new ArrayList<>(children.size());
        
        for (AbstractWidget menuWidget : children) {
            menuItems.add((Menu) menuWidget);
        }
        
        int totalWidth = getTotalWidth();
        maximumScrollX = Math.max(0, totalWidth - windowImpl.getGridWidth());
        
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
    
    public MenuBarBorder getMenuBarBorder() {
        return menuBarBorder;
    }
    
    public boolean isEmpty() {
        return children.isEmpty();
    }
    
    private void paintMenuBar() {
        buildCharMatrixIfNeeded();
        paintMenuBarBorder();
    }
    
    private void buildCharMatrixIfNeeded() {
        int requestedHeight = 1;
        
        if (menuBarBorder != null) {
            if (!menuBarBorder.getTopHorizontalBorderThickness()
                              .equals(BorderThickness.NONE)){
                requestedHeight = 2;
            }
            
            if (!menuBarBorder.getBottomHorizontalBorderThickness()
                              .equals(BorderThickness.NONE)) {
                requestedHeight++;
            }
        }
        
        int requestedWidth = getMinimumFittingWidth();
        
        if (requestedHeight != charMatrix.length ||
            requestedWidth != charMatrix[0].length) {
            
            Window window = (Window) parentWidget;
            this.width = Math.min(window.getWidth(), requestedWidth);
            this.height = 
                    1 + (menuBarBorder == null ? 0 : menuBarBorder.getHeight());
            
            removeCurrentMenuBarFromDepthBuffer(window);
            
            this.charMatrix = new char[requestedHeight][requestedWidth];
            this.foregroundColorMatrix = new Color[requestedHeight]
                                                  [requestedWidth];
            
            this.backgroundColorMatrix = new Color[requestedHeight]
                                                  [requestedWidth];
            
            setForegroundColorMatrix();
            setBackgroundColorMatrix();
            setTextsCharMatrix();
            
            boolean topBorderPresent;
            
            if (menuBarBorder != null &&
                    !menuBarBorder.getTopHorizontalBorderThickness()
                              .equals(BorderThickness.NONE)) {
                buildTopBorder();
                topBorderPresent = true;
            } else {
                topBorderPresent = false;
            }
            
            if (menuBarBorder != null &&
                    !menuBarBorder.getBottomHorizontalBorderThickness()
                              .equals(BorderThickness.NONE)) {
                buildBottomBorder(topBorderPresent ? 2 : 1);
            }
           
            int endY = 1;
            
            if (menuBarBorder != null) {
                endY += menuBarBorder.getHeight();
            }
            
            int endX = Math.min(requestedWidth, parentWidget.getWidth());
            
            window.addMenuBarToDepthBuffer(this, endX, endY);
        }
    }
    
    private void removeCurrentMenuBarFromDepthBuffer(Window window) {
        int height = 
                1 + (menuBarBorder == null ? 0 : menuBarBorder.getHeight());
        
        window.removeMenuBarFromDepthBuffer(height, this);
    }
    
    private void setTextsCharMatrix() {
        boolean noSeparator =
                menuBarBorder == null || menuBarBorder.noActualBorder();
        
        int menuLineY;
        
        if (menuBarBorder == null 
                || menuBarBorder.getTopHorizontalBorderThickness()
                                .equals(BorderThickness.NONE)) {
            menuLineY = 0;
        } else {
            menuLineY = 1;
        }
        
        char separatorChar;
        BorderThickness separatorBorderThickness = null;
        
        if (menuBarBorder != null) {
            separatorBorderThickness =
                    menuBarBorder.getMenuSeparatorBorderThickness();
        }
        
        if (noSeparator 
                || separatorBorderThickness == null
                || separatorBorderThickness.equals(BorderThickness.NONE)) {
            separatorChar = ' ';
        } else if (separatorBorderThickness.equals(BorderThickness.SINGLE)) {
            separatorChar = '\u2502';
        } else if (separatorBorderThickness.equals(BorderThickness.DOUBLE)) {
            separatorChar = '\u2551';
        } else {
            throw new IllegalStateException(
                    "Unknown BorderThickness: " + separatorBorderThickness);
        }
        
        TextCanvas window = ((Window) parentWidget).getTextCanvas();
        int charsPrinted = 0;
        int iterations = 0;
        
        for (AbstractWidget menuWidget : children) {
            Menu menu = (Menu) menuWidget;
            Point mousePoint = mouseListenerImpl.getCurrentCursorPoint();
            boolean hasHover = menu.contanisPoint(mousePoint.x, mousePoint.y);
            boolean isLast = iterations == children.size() - 1;
            
            charsPrinted += printMenuToCharMatrix(
                                      window, 
                                      menu, 
                                      separatorChar, 
                                      charsPrinted, 
                                      menuLineY,
                                      isLast,
                                      hasHover);
            iterations++;
        }
    }
    
    private int printMenuToCharMatrix(TextCanvas window, 
                                      Menu menu, 
                                      char separatorChar, 
                                      int offset,
                                      int lineY,
                                      boolean isLast,
                                      boolean hasHover) {
        int x;
        int charIndex = 0;
        
        for (x = offset; x < offset + menu.getWidth(); x++) {
            charMatrix[lineY][x] = menu.getMenuText().charAt(charIndex++);
            
            if (hasHover) {
                foregroundColorMatrix[lineY][x] = onHoverForegroundColor;
                backgroundColorMatrix[lineY][x] = onHoverBackgroundColor;
            } else {
                foregroundColorMatrix[lineY][x] = foregroundColor;
                backgroundColorMatrix[lineY][x] = backgroundColor;
            }
        }
        
        if (!isLast) {
            window.setChar(x, lineY, separatorChar);
            return menu.getWidth() + 1;
        } else {
            return menu.getWidth();
        }
    }
    
    private void setForegroundColorMatrix() {
        for (int y = 0; y < foregroundColorMatrix.length; y++) {
            for (int x = 0; x < foregroundColorMatrix[0].length; x++) {
                foregroundColorMatrix[y][x] = foregroundColor;
            }
        }
    }
    
    private void setBackgroundColorMatrix() {
        for (int y = 0; y < backgroundColorMatrix.length; y++) {
            for (int x = 0; x < backgroundColorMatrix[0].length; x++) {
                backgroundColorMatrix[y][x] = backgroundColor;
            }
        }
    }
    
    private void printSimpleMenuBar() {
        TextCanvas canvasImpl = ((Window) parentWidget).getTextCanvas();
        
        int matrixX = scrollX;
        int windowX = 0;
        
        canvasImpl.setForegroundColor(foregroundColorMatrix[0][matrixX]);
        canvasImpl.setBackgroundColor(backgroundColorMatrix[0][matrixX]);
        
        // Clear the menu bar:
        for (int x = 0; x < getWidth(); x++) {
            canvasImpl.setChar(x, 0, ' ');
        }
        
        // Paint the menu bar: 
        for (int i = 0; i < getWidth(); i++, matrixX++, windowX++) {
            canvasImpl.setChar(windowX, 0, charMatrix[0][matrixX]);
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
                menuBarBorder.getTopHorizontalBorderThickness()
                             .equals(BorderThickness.DOUBLE);
        
        boolean leftVerticalDouble = 
                menuBarBorder.getLeftVerticalBorderThickness()
                             .equals(BorderThickness.DOUBLE);
        
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
                menuBarBorder.getRightVerticalBorderThickness()
                             .equals(BorderThickness.DOUBLE);
        
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
    
    private void setDepthBuffer() {
        Window window = (Window) parentWidget;
        window.addMenuBarToDepthBuffer(this, width, height);
    }
    
    private void buildBottomBorder(int y) {
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
            charMatrix[y][x] = borderChar;
        }
    }
    
    private void paintMenuBarBorder() {
        if (menuBarBorder == null || menuBarBorder.noActualBorder()) {
            paintSimpleMenuBar();
            return;
        }
        
        
    }
    
    private void paintSimpleMenuBar() {
        buildCharMatrixIfNeeded();
        
        int index = 0;
        int iterations = 0;
        boolean isLast = false;
        
        for (AbstractWidget menuWidget : children) {
            if (iterations == children.size() - 1) {
                isLast = true;
            }
            
            Menu menu = (Menu) menuWidget;
            paintSimpleMenuBarMenu(menu, index, isLast);
            index += menu.getWidth() + 1;
            iterations++;
        }
    }
    
    private void paintSimpleMenuBarMenu(Menu menu, int index, boolean isLast) {  
        TextCanvas window = ((Window) parentWidget).getTextCanvas();
        
        window.setForegroundColor(foregroundColor);
        window.setBackgroundColor(backgroundColor);
        
        int endIndex = Math.min(scrollX + index + menu.getWidth(),
                                window.getGridWidth());
        
        for (int x = scrollX + index; x < endIndex; x++) {
            window.setChar(x, 0, charMatrix[0][x]);
        }
        
        if (!isLast) {
            window.setChar(endIndex, 0, ' ');
        }
    }
    
    private void paintSimpleMenuBarOld() {
        int totalWidth = Math.min(getMinimumFittingWidth(), 
                                  parentWidget.getWidth());
        
        List<Menu> menus = getMenuList();
        
        int currentMenuIndex = 0;
        Menu currentMenu = menus.get(0);
        int currentMenuWidth = currentMenu.getWidth();
        int currentMenuTextIndex = 0;
        Window window = (Window) parentWidget;
        
        for (int x = 0;
                x < Math.min(totalWidth, parentWidget.getWidth()); 
                x++) {
            char ch;
            
            if (x == currentMenuWidth + 1) {
                ch = ' ';
                currentMenu = menus.get(++currentMenuIndex);
                currentMenuWidth = currentMenu.getWidth();
                currentMenuTextIndex = 0;
            }
            
            ch = currentMenu.getMenuText().charAt(currentMenuTextIndex++);
            
            if (currentMenu.hovered()) {
                window.setForegroundColor(onHoverForegroundColor);
                window.setBackgroundColor(onHoverBackgroundColor);
            } else {
                window.setForegroundColor(foregroundColor);
                window.setBackgroundColor(backgroundColor);
            }
            
            window.setChar(x, 0, ch);
        }
    }
    
    private int getMinimumFittingWidth() {
        int width = 0;
        
        for (AbstractWidget menuWidget : children) {
            Menu menu = (Menu) menuWidget;
            width += menu.getWidth();
        }
        
        width += children.size() - 1; // n - 1 menu separators.
        return width;
    }
    
    @Override
    public void addChildren(AbstractWidget firstWidget, 
                            AbstractWidget... otherWidgets) {
        throw new UnsupportedOperationException(
                "Use addMenu(Menu menu) instead.");
    }
    
    public void addMenu(Menu menu) {
        Objects.requireNonNull(menu, "The input Menu is null.");
        
        int startX;
        int startY;
         
        if (menuBarBorder == null || 
                menuBarBorder.getTopHorizontalBorderThickness().equals(NONE)) {
            startY = 0;
        } else {
            startY = 1;
        }
        
        if (menuBarBorder == null || menuBarBorder.noActualBorder()) {
            if (children.isEmpty()) {
                startX = 0;
            } else {
                startX = getTotalMenusLength() + 1;
            }
        } else {
            if (children.isEmpty()) {
                startX = 1;
            } else {
                startX = getTotalMenusLength() + 1;
            }
        }
        
        menu.setParentOffsetX(startX);
        menu.setParentOffsetY(startY);
        children.add(menu);
    }
    
    @Override
    public void setParent(AbstractWidget windowCandidate) {
        Objects.requireNonNull(windowCandidate);
        
        if (!(windowCandidate instanceof Window)) {
            throw new IllegalArgumentException(
                    "Only Window is allowed as a parent widget.");
        }
        
        super.setParent(windowCandidate);
        Window window = (Window) windowCandidate;
        window.addMenusToDepthBuffer();
    }
    
    private List<Menu> getMenuList() {
        List<Menu> menuList = new ArrayList<>(children.size());
        
        for (AbstractWidget menuWidget : children) {
            menuList.add((Menu) menuWidget);
        }
        
        return menuList;
    }
    
    public int getTotalMenusLength() {
        if (children.isEmpty()) {
            return 0;
        }
        
        int totalMenusLength = 0;
        
        for (AbstractWidget widget : children) {
            totalMenusLength += widget.getWidth() + 1;
        }
        
        return totalMenusLength - 1;
    }
    
    private final class MenuBarMouseListenerImpl 
            implements TextCanvasMouseListener {
        
        private static final int NONINIT_CURSOR_X = -1;
        private static final int NONINIT_CURSOR_Y = -1;
        
        private volatile int previousCursorX = NONINIT_CURSOR_X;
        private volatile int previousCursorY = NONINIT_CURSOR_Y;
        
        private volatile int currentCursorX;
        private volatile int currentCursorY;
        
        private Menu previouslyHoveredMenu;
        
        Point getCurrentCursorPoint() {
            return new Point(currentCursorX, currentCursorY);
        }
        
        @Override
        public void onMouseClick(MouseEvent mouseEvent, int charX, int charY) {
            
            previousCursorX = currentCursorX;
            previousCursorY = currentCursorY;
            
            Menu targetMenu = getMenuViaPoint(charX, charY);
            
            if (targetMenu != null) {
                System.out.println(targetMenu.getMenuText());
            }
            
            currentCursorX = charX;
            currentCursorY = charY;
        }
        
        private Menu getMenuViaPoint(int charX, int charY) {
            for (AbstractWidget widget : children) {
                Menu menu = (Menu) widget;
                
                if (menu.containsPoint(charX, charY)) {
                    return menu;
                }
            }
            
            return null;
        }
        
        private void handlePreviousCursor() {
            if (previousCursorX == NONINIT_CURSOR_X || 
                    previousCursorY == NONINIT_CURSOR_Y) {
                return;
            }
            
            Menu targetMenu = getMenuViaPoint(previousCursorX, 
                                              previousCursorY);
            
            if (targetMenu == null) {
                return;
            }
            
            
        }
        
        @Override
        public void onMouseExited(MouseEvent mouseEvent, int charX, int charY) {
            if (!MenuBar.this.containsPoint(charX, charY)) {
                return;
            }
            
            System.out.println("exited: x = " + charX + ", y = " + charY);
            Window window = (Window) parentWidget;

            window.getTextCanvas().setForegroundColor(foregroundColor);
            window.getTextCanvas().setBackgroundColor(backgroundColor);

            for (AbstractWidget menuWidget : children) {
                Menu menu = (Menu) menuWidget;
                menu.setHovered(false);
                paintMenuText(window, menu, 0);
            }

            window.paint();
        }
        
        @Override
        public void onMouseMoved(MouseEvent mouseEvent, int charX, int charY) {
            System.out.println("MenuBar.onMouseMoved");
//            handlePreviousCursor();
            
            if (!MenuBar.this.containsPoint(charX, charY)) {
                System.out.println("exitting method");
                return;
            }
            
            Menu targetMenu = getMenuViaPoint(charX + scrollX, charY);
            
            if (targetMenu == null) {
                return;
            }
            
            System.out.println("DEBUG: " + targetMenu.getMenuText());
            
            Window window = (Window) parentWidget;
            
            // Turn off the hover status from all the menus:
            for (AbstractWidget menuWidget : children) {
                Menu menu = (Menu) menuWidget;
                menu.setHovered(false);
            }
            
            // Hover the target menu:
            targetMenu.setHovered(true);
            
            window.getTextCanvas().setForegroundColor(onHoverForegroundColor);
            window.getTextCanvas().setBackgroundColor(onHoverBackgroundColor);
            
            paintMenuText(window, targetMenu, 0);
            window.paint();
            
            previousCursorX = charX;
            previousCursorY = charY;
        }
        
        private void paintMenuText(Window window, Menu menu, int y) {
            int windowWidth = window.getTextCanvas().getGridWidth();
            int xStart = menu.getStartX() + 
                        (children.get(0).equals(menu) ? 0 : 1);
            
            int xEnd = 
                    Math.min(
                            menu.getStartX() 
                                    + menu.getWidth() 
                                    + (children.get(0).equals(menu) ? 0 : 1),
                            windowWidth);
            
            TextCanvas canvas = window.getTextCanvas();
            
            for (int x = xStart, i = 0; x < xEnd; x++, i++) {
                canvas.setChar(x, y, menu.getMenuText().charAt(i));
            }
        }
        
        @Override
        public void onMouseScroll(ScrollEvent scrollEvent,
                                  int charX, 
                                  int charY) {
            
            if (MenuBar.this.containsPoint(charX, charY)) {
                Window window = (Window) parentWidget;
                
                int deltaX = 
                        (int)(scrollEvent.getDeltaX() * 
                              scrollEvent.getMultiplierX() /
                              window.getTextCanvas()
                                    .getFontCharWidth());
                
                int deltaY = 
                        (int) (scrollEvent.getDeltaY() * 
                               scrollEvent.getMultiplierY() / 
                               window.getTextCanvas()
                                     .getFontCharHeight());
                
                int fontCharWidth = window.getFontCharWidth();
                
                boolean doHorizontalScroll =
                        Math.abs(deltaX) >= Math.abs(deltaY);
                
                int charsToScroll;
                
                if (doHorizontalScroll) {
                    charsToScroll = deltaX / fontCharWidth;
                } else {
                    charsToScroll = deltaY / fontCharWidth;
                }
                
                scrollX += charsToScroll;
                normalizeScrollX();
                MenuBar.this.setDepthBuffer();
                MenuBar.this.printSimpleMenuBar();
                window.paint();
            }
        }
    }
    
    private void normalizeScrollX() {
        if (scrollX < 0) {
            scrollX = 0;
        } else if (scrollX > maximumScrollX) {
            scrollX = maximumScrollX;
        }
    }
}
