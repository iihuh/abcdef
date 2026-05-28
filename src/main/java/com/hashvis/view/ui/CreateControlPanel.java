package com.hashvis.view.ui;

import javax.swing.*;

import com.hashvis.controller.CreateControlPanelController;
import com.hashvis.model.collision.CollisionResolver;
import com.hashvis.model.collision.CollisionResolver.DataType;
import com.hashvis.model.hashfunc.HashFunction;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.*;

/**
 * A control panel for configuring and creating a new hash table.
 *
 * This panel is shown before a table is created (either at startup or when the
 * user clicks "Create Table" from the run panel). It provides:
 * <ul>
 * <li>A <b>Cancel</b> button to go back to the previous screen.</li>
 * <li>A <b>table size</b> field, restricted to numeric input (1&#8211;4
 * digits).</li>
 * <li>One or more <b>hash function</b> fields obtained from the collision
 * resolver. Each field is a {@code CodePane} expression editor that the
 * user can edit directly.</li>
 * <li>A <b>Create Table</b> button that validates all hash functions and
 * then delegates to the controller.</li>
 * </ul>
 *
 * The hash function fields are fetched from the resolver via
 * {@link CollisionResolver#getHashFunctionFields}, which returns model objects
 * whose {@code getView()} method provides the Swing component for each field.
 */
public class CreateControlPanel extends JPanel {
  private JButton btnCancel = new JButton("Cancel");
  /** Table size text field; accepts only numeric input (max 4 digits). */
  private JTextField txtSize = new JTextField(10);
  private JButton btnCreate = new JButton("Create Table");
  /**
   * Hash function models obtained from the resolver; their views are rendered
   * inline.
   */
  private List<HashFunction> hashFuncs = null;

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
   * Constructs the create-table panel with a text field for table size, hash
   * function parameter fields pulled from the resolver, and cancel / create
   * buttons. The size field only accepts numeric input up to 4 digits.
   *
   * @param resolver     the collision resolver whose hash function fields to
   *                     display
   * @param dataType     the selected data type for hash function configuration
   * @param isTableExist whether a table already exists
   * @param controller   the controller that handles cancel and create actions
   */
  public CreateControlPanel(CollisionResolver resolver,
      DataType dataType, boolean isTableExist,
      CreateControlPanelController controller) {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    // --- Table size field with numeric-only input validation ---
    // Overrides processKeyEvent to:
    // - Allow digit keys (up to 4 characters)
    // - Allow Backspace
    // - Consume all other key-typed events (blocking non-numeric input)
    txtSize = new JTextField(10) {
      @Override
      protected void processKeyEvent(KeyEvent ev) {
        if (ev.getID() != KeyEvent.KEY_TYPED) {
          super.processKeyEvent(ev);
          return;
        }
        if (Character.isDigit(ev.getKeyChar())) {
          if (txtSize.getText().length() < 4)
            super.processKeyEvent(ev);
        } else if (ev.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
          super.processKeyEvent(ev);
        }
        ev.consume();
      }
    };

    // Cancel button — returns to the previous screen (e.g., the run panel).
    makeHorizontalFill(btnCancel);
    btnCancel.addActionListener(e -> controller.onCancel());
    btnCancel.setEnabled(isTableExist);
    // Create Table button — validates inputs and delegates to the controller.
    makeHorizontalFill(btnCreate);
    btnCreate.addActionListener(e -> {
      if (!checkFunc())
        return;
      if (txtSize.getText().length() == 0)
        return;
      controller.onCreate(Integer.parseInt(txtSize.getText()), hashFuncs);
    });

    // --- "Size:" label + text field row ---
    JPanel sizePanel = new JPanel();
    sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.X_AXIS));
    sizePanel.add(new JLabel("Size: "));
    sizePanel.add(Box.createHorizontalStrut(5));
    sizePanel.add(txtSize);

    add(btnCancel);
    add(Box.createVerticalStrut(10));
    add(sizePanel);
    add(Box.createVerticalStrut(10));

    // --- Hash function fields ---
    // Each field is a CodePane expression editor. The resolver provides the
    // HashFunction model list; we iterate and wrap each view in a row panel.
    hashFuncs = resolver.getHashFunctionFields(dataType);
    for (int i = 0; i < hashFuncs.size(); i++) {
      JPanel hashFuncPanel = new JPanel();
      hashFuncPanel.setLayout(new BoxLayout(hashFuncPanel, BoxLayout.X_AXIS));
      hashFuncPanel.add(new JLabel("Hash " + (i + 1) + ": "));
      hashFuncPanel.add(Box.createHorizontalStrut(5));
      hashFuncPanel.add(hashFuncs.get(i).getView());
      add(hashFuncPanel);
      add(Box.createVerticalStrut(10));
    }
    add(btnCreate);
  }

  /**
   * Checks whether all configured hash functions are valid.
   *
   * @return true if every hash function is valid, false otherwise
   */
  public boolean checkFunc() {
    for (int i = 0; i < hashFuncs.size(); i++)
      if (!hashFuncs.get(i).isValidHashFunction())
        return false;
    return true;
  }

  @Override
  public Dimension getMaximumSize() {
    return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
  }

}
