package com.github.coderodde.text.ui.roddelib;

import java.util.Objects;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public class MenuBar extends AbstractWidget {

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
    
//    @Override
//    public void addChildren(AbstractWidget firstWidget, 
//                            AbstractWidget... otherWidgets) {
//        throw new Unsupported
//    }
    
    public void addMenu(Menu menu) {
        addChildren(Objects.requireNonNull(menu, "The input Menu is null."));
    }
}
