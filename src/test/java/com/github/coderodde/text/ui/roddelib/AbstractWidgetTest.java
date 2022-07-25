package com.github.coderodde.text.ui.roddelib;

import org.junit.Test;

public class AbstractWidgetTest {

    public static class AbstractWidgetImpl extends AbstractWidget {
    
    }
    
    @Test
    public void validWidgetTree() {
        AbstractWidget root = new AbstractWidgetImpl();
        AbstractWidget leftChild1 = new AbstractWidgetImpl();
        AbstractWidget leftChild2 = new AbstractWidgetImpl();
        AbstractWidget rightChild = new AbstractWidgetImpl();
        
        root.addChildren(leftChild1, rightChild);
        leftChild1.addChildren(leftChild2);
    }
    
    @Test(expected = NotWidgetTreeException.class)
    public void throwOnNonTreeWidgetTopology() {
        AbstractWidget root = new AbstractWidgetImpl();
        AbstractWidget rootChild = new AbstractWidgetImpl();
        AbstractWidget rootChildChild = new AbstractWidgetImpl();
        
        root.addChildren(rootChild);
        rootChild.addChildren(rootChildChild);
        rootChildChild.addChildren(root);
    }
}
