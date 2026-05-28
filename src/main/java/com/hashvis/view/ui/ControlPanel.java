package com.hashvis.view.ui;

import javax.swing.*;

import com.hashvis.controller.ControlPanelController;
import com.hashvis.model.collision.CollisionResolver.HashAction;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * A control panel for performing hash table operations.
 *
 * This panel is shown after a table has been created. It provides:
 * <ul>
 *   <li>A <b>Create Table</b> button to return to the table-creation screen.</li>
 *   <li>A <b>key input</b> field and an <b>action selector</b> (Insert /
 *       Search / Delete).</li>
 *   <li>A <b>Run</b> button that triggers the selected operation, running
 *       step-by-step through the algorithm's pseudocode.</li>
 * </ul>
 *
 * The Run button is disabled when the key field is empty. During execution the
 * controller disables all inputs via {@link #setInputsEnabled} and re-enables
 * them via a callback when the algorithm finishes.
 */
public class ControlPanel extends JPanel {
  /** Button to switch back to the table-creation screen. */
  private JButton btnCreateTable = new JButton("Create Table");
  /** Text field for the key value to insert/search/delete. */
  private JTextField txtKey = new JTextField(10);
  /** Selector for the hash table operation (Insert, Search, Delete). */
  private JComboBox<String> cbAction = new JComboBox<>(new String[] { "Insert", "Search", "Delete" });
  /** Button to begin the selected operation. */
  private JButton btnRun = new JButton("Run");

  /**
   * Configures a component to stretch horizontally while keeping its
   * preferred height, and to center itself when the container is wider
   * than the component.
   */
  private void makeHorizontalFill(JComponent comp) {
    comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, comp.getPreferredSize().height));
    comp.setAlignmentX(Component.CENTER_ALIGNMENT);
  }

  /**
   * Constructs the control panel with key input, action selector, and
   * create-table and run buttons. The run button is disabled until a key is
   * entered.
   *
   * @param controller the controller that handles create-table and run actions
   */
  public ControlPanel(ControlPanelController controller) {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    // Create Table button — returns to the configuration screen.
    makeHorizontalFill(btnCreateTable);
    btnCreateTable.addActionListener(e -> controller.onCreateTable());
    makeHorizontalFill(btnRun);
    // Run button — starts the algorithm visualization.
    // Disables all inputs first; the controller re-enables them on completion
    // via the callback (third argument).
    btnRun.addActionListener(e -> {
      setInputsEnabled(false);
      controller.onRun(
          HashAction.values()[cbAction.getSelectedIndex()],
          txtKey.getText(),
          () -> setInputsEnabled(true));
    });
    // Key listener: the Run button is only enabled when a key has been typed.
    txtKey.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        btnRun.setEnabled(txtKey.getText().length() > 0);
      }
    });

    // --- Input row: "Key:" label + text field ---
    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
    inputPanel.add(new JLabel("Key: "));
    inputPanel.add(Box.createHorizontalStrut(5));
    inputPanel.add(txtKey);

    // --- Vertical stack of controls ---
    add(btnCreateTable);
    add(Box.createVerticalStrut(10));
    add(inputPanel);
    add(Box.createVerticalStrut(10));
    add(cbAction);
    add(Box.createVerticalStrut(10));
    add(btnRun);
  }

  @Override
  public Dimension getMaximumSize() {
    return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
  }

  /**
   * Enables or disables all input components in this panel. When re-enabling,
   * the run button is only enabled if the key text field is non-empty.
   *
   * @param enabled true to enable inputs, false to disable them
   */
  public void setInputsEnabled(boolean enabled) {
    btnCreateTable.setEnabled(enabled);
    btnRun.setEnabled(enabled);
    cbAction.setEnabled(enabled);
    txtKey.setEnabled(enabled);
    if (enabled)
      btnRun.setEnabled(txtKey.getText().length() > 0);
  }
}
