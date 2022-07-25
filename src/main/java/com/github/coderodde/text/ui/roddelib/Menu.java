package com.github.coderodde.text.ui.roddelib;

import java.util.Objects;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public class Menu extends AbstractWidget {

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
}
