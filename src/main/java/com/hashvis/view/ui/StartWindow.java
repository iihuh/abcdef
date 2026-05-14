package com.hashvis.view.ui;

import javax.swing.*;

import com.hashvis.controller.StartWindowController;

import java.awt.*;

public class StartWindow extends JFrame {
  private JComboBox<String> dtype = new JComboBox<>(new String[] { "Integer", "String" });
  private JComboBox<String> resolvers = new JComboBox<>(new String[] {
      "Linear Probing",
      "Quadratic Probing",
      "Double Hashing",
      "Separate Chaining" });
  private JFrame demoFrame;

  public StartWindow(StartWindowController controller) {
    setTitle("Hash Table Visualizer - Main Menu");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(500, 600);
    setLocationRelativeTo(null);

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

    JLabel lblTitle = new JLabel("Hash Table Visualizer");
    lblTitle.setForeground(Color.BLACK);
    lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
    lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    mainPanel.add(lblTitle);
    mainPanel.add(Box.createVerticalStrut(40));

    JPanel dtypePanel = new JPanel();
    dtypePanel.setLayout(new BoxLayout(dtypePanel, BoxLayout.X_AXIS));
    dtypePanel.add(new JLabel("Data Type: "));
    dtypePanel.add(dtype);
    dtypePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
    mainPanel.add(dtypePanel);
    mainPanel.add(Box.createVerticalStrut(25));

    JPanel resolversPanel = new JPanel();
    resolversPanel.setLayout(new BoxLayout(resolversPanel, BoxLayout.X_AXIS));
    resolversPanel.add(new JLabel("Collision Resolving Algorithm: "));
    resolversPanel.add(resolvers);
    resolversPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
    mainPanel.add(resolversPanel);
    mainPanel.add(Box.createVerticalStrut(25));

    mainPanel.add(createMenuButton("Create Table", () -> {
      controller.onCreateTable(dtype.getSelectedIndex() == 1,
          resolvers.getSelectedIndex());
    }));

    mainPanel.add(Box.createVerticalStrut(40));
    mainPanel.add(new JSeparator());
    mainPanel.add(Box.createVerticalStrut(20));

    mainPanel.add(createMenuButton("Help", this::showHelp));
    mainPanel.add(Box.createVerticalStrut(15));
    mainPanel.add(createMenuButton("Quit", this::confirmQuit));

    add(mainPanel);
  }

  private JButton createMenuButton(String text, Runnable action) {
    JButton btn = new JButton(text);
    btn.setMaximumSize(new Dimension(300, 45));
    btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    btn.setFocusPainted(false);
    btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
    btn.addActionListener(e -> action.run());
    return btn;
  }

  public void showDemo(JPanel mainWindow) {
    demoFrame = new JFrame("Hash Table Visualizer");
    demoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    demoFrame.setContentPane(mainWindow);
    demoFrame.setSize(1200, 800);
    demoFrame.setLocationRelativeTo(null);
    demoFrame.setVisible(true);
    this.setVisible(false);
  }

  public void back() {
    this.setVisible(true);
    if (demoFrame != null) {
      demoFrame.setVisible(false);
      demoFrame.dispose();
      demoFrame = null;
    }
  }

  private void showHelp() {
    String helpText = "<html><body style='width: 300px; font-family: SansSerif;'>"
        + "<b>Hash Table Basics:</b><br>A data structure that maps keys to indices using a hash function.<br><br>"
        + "<b>Collision Strategies:</b><br>"
        + "1. <u>Linear Probing</u>: If a collision occurs, check the next slot (index + 1).<br>"
        + "2. <u>Quadratic Probing</u>: Check slots using a quadratic formula (index + i²).<br>"
        + "3. <u>Double Hashing</u>: Use a second hash function to determine the probe step.<br>"
        + "4. <u>Separate Chaining</u>: Each slot contains a list of all elements that hash to that index."
        + "</body></html>";

    JOptionPane.showMessageDialog(this, helpText, "Help & Documentation", JOptionPane.INFORMATION_MESSAGE);
  }

  private void confirmQuit() {
    System.exit(0);
  }
}
