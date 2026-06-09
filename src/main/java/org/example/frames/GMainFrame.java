package org.example.frames;

import javax.swing.*;
import java.awt.*;

public class GMainFrame extends JFrame {
    // components
    private GMenuBar menuBar;
    private GShapeToolBar shapeToolBar;
    private GColorToolBar colorToolBar;
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

        this.shapeToolBar = new GShapeToolBar();
        this.colorToolBar = new GColorToolBar();

        // JPanel에 ToolBar들 한번에 담기
        JPanel toolBarContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        toolBarContainer.add(this.shapeToolBar);
        toolBarContainer.add(this.colorToolBar);
        this.add(toolBarContainer, BorderLayout.NORTH);

        this.drawingPanel = new GDrawingPanel();
        this.add(drawingPanel, BorderLayout.CENTER);

        // association
        this.drawingPanel.associateWith(this.shapeToolBar);
        this.colorToolBar.associateWith(this.drawingPanel);
    }


//    private class ToolButtonActionHandler implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            if(e.)
//        }
//    }
    // member functions
}
