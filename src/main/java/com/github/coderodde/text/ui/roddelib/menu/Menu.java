package com.github.coderodde.text.ui.roddelib.menu;

import com.github.coderodde.text.ui.roddelib.AbstractWidget;
import java.util.Objects;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public class Menu extends AbstractWidget {
    
    private final String menuText;
    private boolean hovered = false;
    
    public Menu(String menuText) {
        Objects.requireNonNull(menuText, "The input menuText is null.");
        
        if (menuText.isBlank()) {
            throw new IllegalArgumentException("The input menuText is blank.");
        }
        
        this.menuText = menuText;
    }
    
    @Override
    public String toString() {
        return "[Menu = '" + menuText + "']";
    }
    
    public boolean contanisPoint(int charX, int charY) {
        int parentOffsetXBegin = parentWidget.getStartX();
        int parentOffsetYBegin = parentWidget.getStartY();
        int parentOffsetXEnd = parentOffsetXBegin + getWidth();
        int parentOffsetYEnd = parentOffsetYBegin + getHeight();
        
        return parentOffsetXBegin <= charX && charX < parentOffsetXEnd &&
               parentOffsetYBegin <= charY && charY < parentOffsetYEnd;
    }
    
    public String getMenuText() {
        return menuText;
    }
    
    @Override
    public int getWidth() {
        return menuText.length();
    }

    public void add(Menu nestedMenu) {
        addChildren(
                Objects.requireNonNull(
                        nestedMenu,
                        "The input nestedMenu is null."));
    }
    
    public void add(MenuItem menuItem) {
        addChildren(
                Objects.requireNonNull(
                        menuItem,
                        "The input menuItem is null."));
    }
    
    public void add(MenuSeparator separator) {
        Objects.requireNonNull(separator, "The input separator is null.");
        
        if (children.contains(separator)) {
            // Copy construct another separator. We don't do duplicates.
            MenuSeparator newMenuSeparator = new MenuSeparator(separator);
            newMenuSeparator.positionIndexInParentMenu = getMenuItemCount();
            children.add(newMenuSeparator);
        } else {
            separator.setPositionIndexInMenu(getMenuItemCount());
            children.add(separator);
        }
    }
    
    public int getMenuItemCount() {
        return children.size();
    }
    
    public boolean hovered() {
        return hovered;
    }
    
    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }
}
