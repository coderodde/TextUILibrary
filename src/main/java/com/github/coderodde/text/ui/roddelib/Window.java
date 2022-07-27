package com.github.coderodde.text.ui.roddelib;

import com.github.coderodde.text.ui.roddelib.menu.MenuBar;
import com.github.coderodde.text.ui.roddelib.impl.TextUIWindowMouseListener;
import com.github.coderodde.text.ui.roddelib.impl.TextUIWindowTouchListener;
import com.github.coderodde.text.ui.roddelib.impl.TextUIWindow;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TouchEvent;

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
    
    private TextUIWindow windowImpl;
    
    private List<AbstractWidget>[][] depthBuffer;
    
    public Window(int width, int height, int fontSize) {
        this.windowImpl = new TextUIWindow(width, height, fontSize);
        this.windowImpl.addTextUIWindowMouseListener(
                new GlobalWindowMouseListener());
        
        initializeDepthBuffer();
    }
    
    public Window(int width, int height) {
        this(width, height, DEFAULT_FONT_SIZE);
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
        this.menuBar =
                Objects.requireNonNull(menuBar, "The input MenuBar is null.");
        
        // Let the MenuBar know its window:
        menuBar.setParent(this);
    }
    
    public void setChar(int charX, int charY, char ch) {
        
        windowImpl.setChar(charX, charY, ch);
    }
    
    private final class GlobalWindowMouseListener 
            implements TextUIWindowMouseListener,
                       TextUIWindowTouchListener {
        
        private volatile int previousCharX;
        private volatile int previousCharY;
        
        @Override
        public void onMouseClick(MouseEvent mouseEvent, int charX, int charY) {
            AbstractWidget eventTargetComponent = getTopmostWidgetAtPos(charX, 
                                                                        charY);
            
            if (eventTargetComponent.mouseClickListener != null) {
                eventTargetComponent.mouseClickListener.onClick(mouseEvent, 
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
            
            if (eventTargetWidget.mouseScrollListener != null) {
                eventTargetWidget.mouseScrollListener.onScroll(scrollEvent, 
                                                               charX, 
                                                               charY);
            }
        }
        
        @Override
        public void onTouchMove(TouchEvent touchEvent, int charX, int charY) {
            AbstractWidget eventTargetWidget = getTopmostWidgetAtPos(charX, 
                                                                     charY);
            
            if (eventTargetWidget.touchMoveListener != null) {
                eventTargetWidget.touchMoveListener.onTouchMove(touchEvent, 
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
