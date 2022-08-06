package com.github.coderodde.text.ui.roddelib;

import com.github.coderodde.text.ui.roddelib.menu.Menu;
import com.github.coderodde.text.ui.roddelib.menu.MenuBar;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestApp extends Application {

    private final Window window = new Window(14, 20);
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        MenuBar menuBar = new MenuBar();
        menuBar.addMenu(new Menu("File"));
        menuBar.addMenu(new Menu("Edit"));
        menuBar.addMenu(new Menu("View"));
        menuBar.addMenu(new Menu("Tools"));
        
        window.addMenuBar(menuBar);
        
        Group root = new Group();
        root.getChildren().add(window.getTextCanvas());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        window.paint();
    }
}
