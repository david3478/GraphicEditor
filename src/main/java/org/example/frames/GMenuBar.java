package org.example.frames;

import org.example.menus.GFileMenu;

import javax.swing.*;

public class GMenuBar extends JMenuBar {
    private GFileMenu fileMenu;

    public GMenuBar() {
        // components
        this.fileMenu = new GFileMenu();
        this.add(fileMenu);
    }
}
