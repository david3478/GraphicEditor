package org.example;

import javax.swing.*;

public class GMenuBar extends JMenuBar {
    private GFileMenu fileMenu;

    public GMenuBar() {
        // components
        this.fileMenu = new GFileMenu();
        this.add(fileMenu);
    }
}
