package org.example;

import javax.swing.*;
import java.awt.*;

public class GMainFrame extends JFrame {
    // components
    private GMenuBar menuBar;
    private GToolBar toolBar;
    private GDrawingPanel drawingPanel;

    // association

    public GMainFrame() {
        // attributes
        this.setLocation(200, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600,400);
        this.setLayout(new BorderLayout());

        // components
        this.menuBar = new GMenuBar();
        this.setJMenuBar(this.menuBar);

        this.toolBar = new GToolBar();
        this.add(toolBar, BorderLayout.NORTH);

        this.drawingPanel = new GDrawingPanel();
        this.add(drawingPanel, BorderLayout.CENTER);

        this.drawingPanel.setToolBar(toolBar);
    }

    // member functions
}
