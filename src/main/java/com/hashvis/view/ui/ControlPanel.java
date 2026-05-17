package com.hashvis.view.ui;

import javax.swing.*;

import com.hashvis.controller.ControlPanelController;
import com.hashvis.model.collision.CollisionResolver.HashAction;

import java.awt.*;

public class ControlPanel extends JPanel {
  private JButton btnCreateTable = new JButton("Create Table");
  private JTextField txtKey = new JTextField(10);
  private JComboBox<String> cbAction = new JComboBox<>(new String[] { "Insert", "Search", "Delete" });
  private JButton btnRun = new JButton("Run");

  private void makeHorizontalFill(JComponent comp) {
    comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, comp.getPreferredSize().height));
    comp.setAlignmentX(Component.CENTER_ALIGNMENT);
  }

  public ControlPanel(ControlPanelController controller) {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    makeHorizontalFill(btnCreateTable);
    btnCreateTable.addActionListener(e -> controller.onCreateTable());
    makeHorizontalFill(btnRun);
    btnRun.addActionListener(e -> {
      setInputsEnabled(false);
      controller.onRun(
          HashAction.values()[cbAction.getSelectedIndex()],
          txtKey.getText(),
          () -> setInputsEnabled(true));
    });

    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
    inputPanel.add(new JLabel("Key: "));
    inputPanel.add(Box.createHorizontalStrut(5));
    inputPanel.add(txtKey);

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

  public void setInputsEnabled(boolean enabled) {
    btnCreateTable.setEnabled(enabled);
    btnRun.setEnabled(enabled);
    cbAction.setEnabled(enabled);
    txtKey.setEnabled(enabled);
    if (enabled) {
      cbAction.setSelectedIndex(cbAction.getSelectedIndex());
      btnRun.setEnabled(txtKey.getText().length() > 0);
    }
  }
}
