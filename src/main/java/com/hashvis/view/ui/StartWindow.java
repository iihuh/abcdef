package com.hashvis.view.ui;

import javax.swing.*;

import com.hashvis.controller.StartWindowController;

import java.awt.*;

/**
 * The startup menu window for the Hash Table Visualizer.
 *
 * This is the first screen the user sees. It presents two selection fields:
 * <ol>
 *   <li><b>Data Type</b> — whether keys are Integer or String.</li>
 *   <li><b>Collision Algorithm</b> — one of Linear Probing, Quadratic Probing,
 *       Double Hashing, or Separate Chaining.</li>
 * </ol>
 * The user then clicks "Create Table" to proceed to the main workspace, or
 * "Help" / "Quit" for documentation or to exit.
 *
 * When "Create Table" is clicked, the window hides itself and opens a new
 * {@code JFrame} containing the main workspace (see {@link #showDemo}).
 * The workspace can navigate back to this menu via the back button, which
 * re-shows this window and disposes the workspace frame (see {@link #back}).
 */
public class StartWindow extends JFrame {
  /** Data type selector: index 0 = Integer, index 1 = String. */
  private JComboBox<String> dtype = new JComboBox<>(new String[] { "Integer", "String" });
  /** Collision resolver selector: index matches the resolver enum order. */
  private JComboBox<String> resolvers = new JComboBox<>(new String[] {
      "Linear Probing",
      "Quadratic Probing",
      "Double Hashing",
      "Separate Chaining" });
  /** The main workspace frame, created once when the user clicks "Create Table". */
  private JFrame demoFrame;

  /**
   * Constructs the start window with title, data type selector, collision
   * resolver selector, and buttons for creating a table, viewing help, and
   * quitting.
   *
   * @param controller the controller that handles table creation events
   */
  public StartWindow(StartWindowController controller) {
    setTitle("Hash Table Visualizer - Main Menu");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(500, 600);
    setLocationRelativeTo(null);

    // --- Root panel: centered column layout ---
    // All components are stacked vertically with generous padding.
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

    // Title label
    JLabel lblTitle = new JLabel("Hash Table Visualizer");
    lblTitle.setForeground(Color.BLACK);
    lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
    lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    mainPanel.add(lblTitle);
    mainPanel.add(Box.createVerticalStrut(40));

    // --- Data type selector row ---
    JPanel dtypePanel = new JPanel();
    dtypePanel.setLayout(new BoxLayout(dtypePanel, BoxLayout.X_AXIS));
    dtypePanel.add(new JLabel("Data Type: "));
    dtypePanel.add(dtype);
    dtypePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
    mainPanel.add(dtypePanel);
    mainPanel.add(Box.createVerticalStrut(25));

    // --- Collision resolver selector row ---
    JPanel resolversPanel = new JPanel();
    resolversPanel.setLayout(new BoxLayout(resolversPanel, BoxLayout.X_AXIS));
    resolversPanel.add(new JLabel("Collision Resolving Algorithm: "));
    resolversPanel.add(resolvers);
    resolversPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
    mainPanel.add(resolversPanel);
    mainPanel.add(Box.createVerticalStrut(25));

    // --- Create Table button ---
    // Delegates to the controller which instantiates the chosen resolver,
    // creates the MainWindowController, and opens the workspace.
    mainPanel.add(createMenuButton("Create Table", () -> {
      controller.onCreateTable(dtype.getSelectedIndex() == 1,
          resolvers.getSelectedIndex());
    }));

    // --- Separator and help/quit buttons ---
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

  /**
   * Displays the main visualization window and hides this start window. Creates
   * a new JFrame containing the given main window panel.
   *
   * @param mainWindow the main workspace panel to display in the demo frame
   */
  public void showDemo(JPanel mainWindow) {
    demoFrame = new JFrame("Hash Table Visualizer");
    demoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    demoFrame.setContentPane(mainWindow);
    demoFrame.setSize(1200, 800);
    demoFrame.setLocationRelativeTo(null);
    demoFrame.setVisible(true);
    this.setVisible(false);
  }

  /**
   * Returns to this start window from the demo view. Hides and disposes the
   * demo frame and makes this window visible again.
   */
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
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Confirm Quit",
        JOptionPane.YES_NO_OPTION);
    if (result == JOptionPane.YES_OPTION)
      System.exit(0);
  }
}
