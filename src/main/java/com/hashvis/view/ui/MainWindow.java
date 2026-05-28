package com.hashvis.view.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.hashvis.controller.MainWindowController;

import java.awt.*;

/**
 * The main content panel for the hash table visualization workspace.
 *
 * The layout is divided into three functional areas:
 * <ol>
 *   <li><b>Control area</b> (top-left) — buttons for creating tables, entering
 *       keys, and running insert/search/delete operations, rendered via a
 *       sub-panel that the controller can swap out.</li>
 *   <li><b>Pseudocode area</b> (bottom-left) — a {@link HashVisualizerView}
 *       that shows the current algorithm's pseudocode and marks the active
 *       line during execution.</li>
 *   <li><b>Hash table area</b> (right) — a visual representation of the table
 *       rows rendered by a subclass of {@code TableView}.</li>
 * </ol>
 * A horizontal {@code JSplitPane} separates the left panel (controls +
 * pseudocode) from the right panel (table view). The controller calls
 * {@link #replaceControlPanel}, {@link #setHashTableView}, and
 * {@link #setPseudoCodeView} to swap contents dynamically.
 */
public class MainWindow extends JPanel {
  private JPanel controlPanelArea;
  private JPanel hashTableArea;
  private JPanel pseudoCodeArea;

  /**
   * Constructs the main window layout with a back button, a control panel area,
   * a pseudocode area, and a hash table area arranged in a horizontal split
   * pane.
   *
   * @param controller the controller that handles back-navigation events
   */
  public MainWindow(MainWindowController controller) {
    super(new BorderLayout(10, 10));

    // --- Back button (top of the left column) ---
    // The back button returns the user to the type/resolver selection menu.
    // Its width is stretchable (MAX_VALUE) but its height is fixed at 25px
    // so that it occupies minimal vertical space.
    JButton backButton = new JButton("Back");
    backButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
    backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    backButton.addActionListener(e -> controller.onBack());

    // --- Control panel area (middle of the left column) ---
    // This is the dynamic section of the left column. The controller swaps
    // between the "Create Table" panel (with size/hash function fields) and
    // the "Run" panel (with key input / action selector).
    controlPanelArea = new JPanel();
    controlPanelArea.setLayout(new BoxLayout(controlPanelArea, BoxLayout.Y_AXIS));
    controlPanelArea.setBorder(new EmptyBorder(10, 10, 10, 10));

    // --- Pseudocode area (bottom of the left column) ---
    // Contains a HashVisualizerView that displays the algorithm's pseudocode
    // with a marker on the current line during step-by-step execution.
    pseudoCodeArea = new JPanel(new BorderLayout());

    // --- Left column: stacked vertically using BoxLayout Y_AXIS ---
    // Three sections stacked top-to-bottom: back button, control panel,
    // pseudocode area. The pseudocode area gets extra vertical space
    // (it is resizable), while the control panel is at its preferred height.
    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.add(backButton);
    leftPanel.add(Box.createVerticalStrut(10));
    leftPanel.add(controlPanelArea);
    leftPanel.add(Box.createVerticalStrut(10));
    leftPanel.add(pseudoCodeArea);

    // --- Hash table area (right side) ---
    // The visual representation of the table, rendered by a TableView
    // subclass (SeparateChainingTableView or OpenAddressingTableView).
    hashTableArea = new JPanel(new BorderLayout());
    hashTableArea.setBorder(new EmptyBorder(10, 10, 10, 10));

    // --- Horizontal split pane ---
    // Left panel is fixed at 600px, right (hash table) takes the remainder.
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, hashTableArea);
    splitPane.setDividerLocation(600);
    splitPane.setBorder(null);
    add(splitPane, BorderLayout.CENTER);
  }

  /**
   * Replaces the contents of the control panel area with the given panel.
   *
   * @param panel the new control panel to display
   */
  public void replaceControlPanel(JPanel panel) {
    controlPanelArea.removeAll();
    controlPanelArea.add(panel);
    controlPanelArea.revalidate();
    controlPanelArea.repaint();
  }

  /**
   * Sets the component displayed in the hash table view area.
   *
   * @param view the component to show in the hash table area
   */
  public void setHashTableView(Component view) {
    hashTableArea.removeAll();
    hashTableArea.add(view, BorderLayout.CENTER);
    hashTableArea.revalidate();
    hashTableArea.repaint();
  }

  /**
   * Sets the component displayed in the pseudocode area.
   *
   * @param view the component to show in the pseudocode area
   */
  public void setPseudoCodeView(Component view) {
    pseudoCodeArea.removeAll();
    pseudoCodeArea.add(view, BorderLayout.CENTER);
    pseudoCodeArea.revalidate();
    pseudoCodeArea.repaint();
  }
}
