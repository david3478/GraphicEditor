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
        super("GMainFrame");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600,400);
        this.setLayout(new BorderLayout());

        // create aggregation
        this.menuBar = new GMenuBar();
        this.setJMenuBar(this.menuBar);

        this.toolBar = new GToolBar();
        this.add(toolBar, BorderLayout.NORTH);

        this.drawingPanel = new GDrawingPanel();
        this.add(drawingPanel, BorderLayout.CENTER);

        // association
    }

    // member functions
}
