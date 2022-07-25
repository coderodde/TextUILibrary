package com.github.coderodde.text.ui.roddelib;

import com.github.coderodde.ui.TextUIWindow;
import java.util.Objects;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public class Window extends AbstractWidget {
    
    private static final int DEFAULT_FONT_SIZE = 17;
    
    protected MenuBar menuBar;
    private TextUIWindow windowImpl;
    
    public Window(int width, int height) {
        this(width, height, DEFAULT_FONT_SIZE);
    }
    
    public Window(int width, int height, int fontSize) {
        this.windowImpl = new TextUIWindow(width, height, fontSize);
    }

    public void addMenuBar(MenuBar menuBar) {
        this.menuBar =
                Objects.requireNonNull(menuBar, "The input MenuBar is null.");
    }
    
    public void setChar(int charX, int charY, char ch) {
        
        windowImpl.setChar(charX, charY, ch);
    }
}
