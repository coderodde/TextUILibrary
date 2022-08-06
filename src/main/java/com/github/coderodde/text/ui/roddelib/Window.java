package com.github.coderodde.text.ui.roddelib;

import static com.github.coderodde.text.ui.roddelib.BorderThickness.NONE;
import com.github.coderodde.text.ui.roddelib.menu.MenuBar;
import com.github.coderodde.text.ui.roddelib.impl.TextCanvas;
import com.github.coderodde.text.ui.roddelib.menu.Menu;
import com.github.coderodde.text.ui.roddelib.menu.MenuBarBorder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import com.github.coderodde.text.ui.roddelib.impl.TextCanvasMouseListener;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public class Window extends AbstractWidget {
    
    private static final int DEFAULT_FONT_SIZE = 17;
    private static final int DEFAULT_BUFFER_POINT_CAPACITY = 5;
        
    // The menu bar of this window:
    private MenuBar menuBar;
    
    // The bottom tabifier of this window:
    private BottomTabifierWidget bottomTabifierWidget;
    
    private TextCanvas windowImpl;
    
    private TextCanvasMouseListener mouseListener;
    
    private List<AbstractWidget>[][] depthBuffer;
    
    public Window(int width, int height, int fontSize) {
        this.windowImpl = new TextCanvas(width, height, fontSize);
        this.windowImpl.addTextUIWindowMouseListener(
                new GlobalWindowMouseListener());
        
        this.width = windowImpl.getGridWidth();
        this.height = windowImpl.getGridHeight();
        
        initializeDepthBuffer();
    }
    
    public Window(int width, int height) {
        this(width, height, DEFAULT_FONT_SIZE);
    }
    
    public TextCanvas getTextCanvas() {
        return windowImpl;
    }
    
    public void setMouseListener(TextCanvasMouseListener mouseListener) {
        this.mouseListener = mouseListener;
    }
    
    public int getFontCharWidth() {
        return this.windowImpl.getFontCharWidth();
    }
    
    public void paint() {
        if (menuBar != null) {
            menuBar.paint(windowImpl);
        }
        
        Platform.runLater(() -> {
            windowImpl.repaint();
        });
    }
    
    private void initializeDepthBuffer() {
        depthBuffer = new ArrayList[height][width];
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                depthBuffer[y][x] =
                        new ArrayList<>(DEFAULT_BUFFER_POINT_CAPACITY);
            }
        }
    }

    public void addMenuBar(MenuBar menuBar) {
        if (this.menuBar != null) {
            throw new IllegalStateException("Cannot add a MenuBar twice.");
        }
        
        this.menuBar =
                Objects.requireNonNull(menuBar, "The input MenuBar is null.");
        
        // Let the MenuBar know its window:
        menuBar.setParent(this);
        setMenusParents();
        addMenusToDepthBuffer();
    }
    
    public void addMenusToDepthBuffer() {
        int offsetX;
        int offsetY;
        MenuBarBorder menuBarBorder = menuBar.getMenuBarBorder();
        
        if (menuBarBorder != null 
                && !menuBarBorder.getLeftVerticalBorderThickness()
                                 .equals(NONE)) {
            offsetX = 1;
        } else {
            offsetX = 0;
        }
        
        if (menuBarBorder != null 
                && !menuBarBorder.getTopHorizontalBorderThickness()
                                 .equals(NONE)) {
            offsetY = 1;
        } else {
            offsetY = 0;
        }
        
        int totalMenusWidth = getMenusTotalWidth();
        int actualWidth = Math.min(width, totalMenusWidth);
        int charsProcessed = 0;
        
        for (AbstractWidget menuWidget : menuBar.children) {
            charsProcessed += 
                    addMenuToDepthBuffer(
                            (Menu) menuWidget, 
                            offsetX + charsProcessed, 
                            offsetY, 
                            charsProcessed,
                            actualWidth);
            
            charsProcessed++; // Omit the separaotr.
            
            if (charsProcessed >= actualWidth) {
                break;
            }
        }
    }
    
    private void setMenusParents() {
        for (AbstractWidget menuWidget : menuBar.children) {
            menuWidget.setParent(menuBar);
        }
    }
    
    private int addMenuToDepthBuffer(Menu menu, 
                                     int x, 
                                     int y, 
                                     int charsProcessed, 
                                     int width) {
        int menuWidth = menu.getWidth();
        int endX = Math.min(x + menuWidth, width);
        
        for (int xx = charsProcessed; xx < endX; xx++) {
            depthBuffer[y][xx].add(menu);
        }
        
        return endX - x;
    }
    
    private int getMenusTotalWidth() {
        if (menuBar == null) {
            throw new IllegalStateException("Window has no MenuBar.");
        }
        
        if (menuBar.isEmpty()) {
            return 0;
        }
        
        int totalMenusWidth = 0;
        
        for (AbstractWidget menuWidget : menuBar.children) {
            totalMenusWidth += menuWidget.getWidth() + 1;
        }
        
        return totalMenusWidth - 1;
    }
    
    public void setChar(int charX, int charY, char ch) {
        
        windowImpl.setChar(charX, charY, ch);
    }
    
    public void addMenuBarToDepthBuffer(MenuBar menuBar, 
                                        int width, 
                                        int height) {
        for (int y = 0; y < height; y++) {
            List<AbstractWidget>[] row = depthBuffer[y];
            
            for (int x = 0; x < width; x++) {
                row[x].add(menuBar);
            }
        }
    }
    
    public MenuBar getMenuBar() {
        return menuBar;
    }
    
    public void addMenuToDepthBuffer(Menu menu) {
        int xEnd = Math.min(menu.getStartX() + menu.getWidth(), width);
        int startX = 0;
        int y;
        
        MenuBarBorder menuBarBorder = menuBar.getMenuBarBorder();
        
        if (menuBarBorder != null && 
                !menuBarBorder.getTopHorizontalBorderThickness().equals(NONE)) {
            y = 1;
        } else {
            y = 0;
        }
        
        for (int x = startX; x < xEnd; x++) {
            depthBuffer[y][x].add(menu);
        }
    }
    
    public void removeMenuBarFromDepthBuffer(int menuBarHeight,
                                             MenuBar menuBar) {
        
        for (int y = 0; y < menuBarHeight; y++) {
            for (int x = 0; x < width; x++) {
                depthBuffer[y][x].remove(menuBar);
            }
        }
    }
    
    private final class GlobalWindowMouseListener 
            implements TextCanvasMouseListener {
        
        private static final int ILLEGAL_CHAR_COORDINATE = -1;
        
        private volatile int previousCharX = ILLEGAL_CHAR_COORDINATE;
        private volatile int previousCharY = ILLEGAL_CHAR_COORDINATE;
        
        @Override
        public void onMouseClick(MouseEvent mouseEvent, int charX, int charY) {
            AbstractWidget eventTargetComponent = getTopmostWidgetAtPos(charX, 
                                                                        charY);
            
            if (eventTargetComponent == null) {
                return;
            }
            
            if (eventTargetComponent.mouseListener != null) {
                eventTargetComponent.mouseListener.onMouseClick(mouseEvent, 
                                                                charX, 
                                                                charY);
            }
        }
        
        @Override
        public void onMouseEntered(MouseEvent mouseEvent, 
                                   int charX, 
                                   int charY) {
            AbstractWidget eventTargetComponent = getTopmostWidgetAtPos(charX, 
                                                                        charY);
            
            if (eventTargetComponent == null) {
                return;
            }
            
            if (eventTargetComponent.mouseListener != null) {
                eventTargetComponent.mouseListener.onMouseEntered(mouseEvent, 
                                                                  charX, 
                                                                  charY);
            }
        }
        
        @Override
        public void onMouseExited(MouseEvent mouseEvent, int charX, int charY) {
            if (previousCharX == ILLEGAL_CHAR_COORDINATE) {
                return;
            }
            
            AbstractWidget eventTargetComponent = 
                    getTopmostWidgetAtPos(previousCharX, 
                                          previousCharY);
            
            if (eventTargetComponent == null) {
                return;
            }
            
            if (eventTargetComponent.mouseListener != null) {
                eventTargetComponent.mouseListener.onMouseExited(mouseEvent, 
                                                                 previousCharX, 
                                                                 previousCharY);
            }
        }
        
        @Override
        public void onMousePressed(MouseEvent mouseEvent, 
                                   int charX,
                                   int charY) {
            AbstractWidget eventTargetComponent = getTopmostWidgetAtPos(charX, 
                                                                        charY);
            
            if (eventTargetComponent == null) {
                return;
            }
            
            if (eventTargetComponent.mouseListener != null) {
                eventTargetComponent.mouseListener.onMouseClick(mouseEvent, 
                                                                charX, 
                                                                charY);
            }
        }
        
        @Override
        public void onMouseReleased(MouseEvent mouseEvent, 
                                   int charX,
                                   int charY) {
            AbstractWidget eventTargetComponent = getTopmostWidgetAtPos(charX, 
                                                                        charY);
            
            if (eventTargetComponent == null) {
                return;
            }
            
            if (eventTargetComponent.mouseListener != null) {
                eventTargetComponent.mouseListener.onMouseReleased(mouseEvent, 
                                                                   charX, 
                                                                   charY);
            }
        }
        
        @Override
        public void onMouseMoved(MouseEvent mouseEvent, int charX, int charY) {
            if (previousCharX != ILLEGAL_CHAR_COORDINATE &&
                previousCharY != ILLEGAL_CHAR_COORDINATE) {
                
                AbstractWidget oldTargetComponent = 
                        getTopmostWidgetAtPos(previousCharX,
                                              previousCharY);
                
                if (oldTargetComponent != null && 
                    oldTargetComponent.mouseListener != null) {
                    oldTargetComponent.mouseListener
                                      .onMouseExited(mouseEvent,
                                                     previousCharX, 
                                                     previousCharY);
                }
            }
            
            previousCharX = charX;
            previousCharY = charY;
            
            AbstractWidget eventTargetComponent = getTopmostWidgetAtPos(charX, 
                                                                        charY);
            
            if (eventTargetComponent == null) {
                return;
            }
            
            if (eventTargetComponent.mouseListener != null) {
                eventTargetComponent.mouseListener.onMouseMoved(mouseEvent, 
                                                                charX, 
                                                                charY);
            }
        }
        
        @Override
        public void onMouseDragged(MouseEvent mouseEvent, 
                                   int charX,
                                   int charY) {
            AbstractWidget eventTargetComponent = getTopmostWidgetAtPos(charX, 
                                                                        charY);
            
            if (eventTargetComponent == null) {
                return;
            }
            
            if (eventTargetComponent.mouseListener != null) {
                eventTargetComponent.mouseListener.onMouseMoved(mouseEvent, 
                                                                charX, 
                                                                charY);
            }
        }
        
        @Override
        public void onMouseScroll(ScrollEvent scrollEvent,
                                  int charX, 
                                  int charY) {
            AbstractWidget eventTargetWidget = getTopmostWidgetAtPos(charX, 
                                                                     charY);
            
            if (eventTargetWidget == null) {
                return;
            }
            
            if (eventTargetWidget.mouseListener != null) {
                eventTargetWidget.mouseListener.onMouseScroll(scrollEvent, 
                                                              charX, 
                                                              charY);
            }
        }
        
        private AbstractWidget getTopmostWidgetAtPos(int charX, int charY) {
            List<AbstractWidget> widgetStack = depthBuffer[charY][charX];
            
            if (widgetStack.isEmpty()) {
                return null;
            }
            
            return widgetStack.get(widgetStack.size() - 1);
        }
    }
}
