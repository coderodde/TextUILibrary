package com.github.coderodde.text.ui.roddelib;

import com.github.coderodde.ui.TextUIWindow;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.scene.input.MouseEvent;

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
    }
    
    public Window(int width, int height) {
        this(width, height, DEFAULT_FONT_SIZE);
        this.depthBuffer = initializeDepthBuffer();
        this.windowImpl.addTextUIWindowMouseListener(new GlobalWindowMouseListener());
    }
    
    private List<AbstractWidget>[][] initializeDepthBuffer() {
        List<AbstractWidget>[][] depthBuffer = new ArrayList[height][width];
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                depthBuffer[y][x] =
                        new ArrayList<>(DEFAULT_BUFFER_POINT_CAPACITY);
            }
        }
        
        return depthBuffer;
    }

    public void addMenuBar(MenuBar menuBar) {
        this.menuBar =
                Objects.requireNonNull(menuBar, "The input MenuBar is null.");
        
        // Let the MenuBar know its window:
        menuBar.setParentWindow(this);
    }
    
    public void setChar(int charX, int charY, char ch) {
        
        windowImpl.setChar(charX, charY, ch);
    }
    
    private final class GlobalWindowMouseListener 
            implements TextUIWindowMouseListener {
        
        private volatile int previousCharX;
        private volatile int previousCharY;
        
        public GlobalWindowMouseListener() {
            
        }
        
        @Override
        public void onMouseClick(MouseEvent event, int charX, int charY) {
            List<AbstractWidget> componentStack = depthBuffer[charY][charX];
            
            if (componentStack.isEmpty()) {
                return;
            }
            
            AbstractWidget eventTargetComponent = 
                    componentStack.get(componentStack.size() - 1);
            
            if (eventTargetComponent.mouseClickListener != null) {
                eventTargetComponent.mouseClickListener.onClick(event, 
                                                                charX, 
                                                                charY);
            }
        }
    }
}
