package com.github.coderodde.text.ui.roddelib;

import com.github.coderodde.text.ui.roddelib.impl.TextUIWindowMouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javafx.scene.paint.Color;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public abstract class AbstractWidget {
    
    private static final Color DEFAULT_FOREGROUND_COLOR = Color.BLACK;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    
    /**
     * The parent widget.
     */
    protected AbstractWidget parentWidget;
    
    /**
     * The offset to the right from the left vertical border of the parent 
     * widget.
     */
    protected int parentOffsetX;
    
    /**
     * The offset downwards from the topmost border of the parent 
     * widget.
     */
    protected int parentOffsetY;
    
    /**
     * The width of this widget in characters.
     */
    protected int width;
    
    /**
     * The height of this widget in characters.
     */
    protected int height;
    
    /**
     * Indicates whether the client can resize this widget by dragging the 
     * borders or corners of this widget.
     */
    protected boolean resizable;
    
    /**
     * Indicates whether the client can move this widget in the main window.
     */
    protected boolean moveable;
    
    /**
     * The list of child widgets.
     */
    protected final List<AbstractWidget> children = new ArrayList<>();
    
    /**
     * The color of the foreground text.
     */
    protected Color foregroundColor = DEFAULT_FOREGROUND_COLOR;
    
    /**
     * The color of the background.
     */
    protected Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
    
    /**
     * The mouse listener.
     */
    protected TextUIWindowMouseListener mouseListener;
    
    /**
     * Adds an array of 
     * @param firstChildWidget  the first widget to add.
     * @param otherChildWidgets the rest widgets to add.
     */
    public void addChildren(AbstractWidget firstChildWidget, 
                            AbstractWidget... otherChildWidgets) {
        List<AbstractWidget> allWidgets = 
                new ArrayList<>(1 + otherChildWidgets.length);
        
        allWidgets.add(firstChildWidget);
        allWidgets.addAll(Arrays.asList(otherChildWidgets));
        children.addAll(allWidgets);
        
        try {
            checkWidgetTreeTopology(allWidgets);
        } catch (NotWidgetTreeException ex) {
            children.removeAll(allWidgets);
            throw ex;
        }
    }

    public void paint() {
        
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getStartX() {
        return parentOffsetX;
    }
    
    public int getStartY() {
        return parentOffsetY;
    }
    
    public Color getForegroundColor() {
        return foregroundColor;
    }
    
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    
    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = 
                Objects.requireNonNull(
                        foregroundColor,
                        "The input foreground color is null.");
    }
    
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = 
                Objects.requireNonNull(
                        backgroundColor,
                        "The input background color is null.");
    }
    
    public void setMouseClickListener(TextUIWindowMouseListener mouseListener) {
        this.mouseListener = mouseListener;
    }
    
    public void setParentOffsetX(int parentOffsetX) {
        this.parentOffsetX = parentOffsetX;
    }
    
    public void setParentOffsetY(int parentOffsetY) {
        this.parentOffsetY = parentOffsetY;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public boolean isResizable() {
        return resizable;
    }
    
    public boolean isMoveable() {
        return moveable;
    }
    
    public boolean containsPoint(int charX, int charY) {
        int startOffsetX = getStartX();
        int startOffsetY = getStartY();
        int endOffsetX = startOffsetX + getWidth();
        int endOffsetY = startOffsetY + getHeight();
        
        return startOffsetX <= charX && charX < endOffsetX &&
               startOffsetY <= charY && charY < endOffsetY;
    }
    
    private void checkWidgetTreeTopology(List<AbstractWidget> widgets) {
        Set<AbstractWidget> visitedSet = new HashSet<>();
        
        for (AbstractWidget widget : widgets) {
            dfs(widget, visitedSet);
        }
    }
    
    private static void dfs(AbstractWidget widget, 
                            Set<AbstractWidget> visitedSet) {
        if (!visitedSet.add(widget)) {
            throw new NotWidgetTreeException();
        }
        
        for (AbstractWidget widgetChild : widget.children) {
            dfs(widgetChild, visitedSet);
        }
    }
    
    protected void setParent(AbstractWidget parentWidget) {
        this.parentWidget = parentWidget; // TODO: Check null?
    }
}
