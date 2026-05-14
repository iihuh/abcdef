package com.hashvis.view.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.hashvis.controller.MainWindowController;

import java.awt.*;

public class MainWindow extends JPanel {
  private JPanel controlPanelArea;
  private JPanel hashTableArea;
  private JPanel pseudoCodeArea;

  public MainWindow(MainWindowController controller) {
    super(new BorderLayout(10, 10));

    JButton backButton = new JButton("Back");
    backButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
    backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    backButton.addActionListener(e -> controller.onBack());

    controlPanelArea = new JPanel();
    controlPanelArea.setLayout(new BoxLayout(controlPanelArea, BoxLayout.Y_AXIS));
    controlPanelArea.setBorder(new EmptyBorder(10, 10, 10, 10));

    pseudoCodeArea = new JPanel(new BorderLayout());

    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.add(backButton);
    leftPanel.add(Box.createVerticalStrut(10));
    leftPanel.add(controlPanelArea);
    leftPanel.add(Box.createVerticalStrut(10));
    leftPanel.add(pseudoCodeArea);

    hashTableArea = new JPanel(new BorderLayout());
    hashTableArea.setBorder(new EmptyBorder(10, 10, 10, 10));

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, hashTableArea);
    splitPane.setDividerLocation(600);
    splitPane.setBorder(null);
    add(splitPane, BorderLayout.CENTER);
  }

  public void replaceControlPanel(JPanel panel) {
    controlPanelArea.removeAll();
    controlPanelArea.add(panel);
    controlPanelArea.revalidate();
    controlPanelArea.repaint();
  }

  public void setHashTableView(Component view) {
    hashTableArea.removeAll();
    hashTableArea.add(view, BorderLayout.CENTER);
    hashTableArea.revalidate();
    hashTableArea.repaint();
  }

  public void setPseudoCodeView(Component view) {
    pseudoCodeArea.removeAll();
    pseudoCodeArea.add(view, BorderLayout.CENTER);
    pseudoCodeArea.revalidate();
    pseudoCodeArea.repaint();
  }
}
