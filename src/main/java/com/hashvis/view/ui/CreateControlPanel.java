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

public class CreateControlPanel extends JPanel {
  private JButton btnCancel = new JButton("Cancel");
  private JTextField txtSize = new JTextField(10);
  private JButton btnCreate = new JButton("Create Table");
  private List<HashFunction> hashFuncs = null;

  private void makeHorizontalFill(JComponent comp) {
    comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, comp.getPreferredSize().height));
    comp.setAlignmentX(Component.CENTER_ALIGNMENT);
  }

  // TODO: Accept DataType parameter here when hash function fields are added back
  public CreateControlPanel(CollisionResolver resolver,
      DataType dataType,
      CreateControlPanelController controller) {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

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

    makeHorizontalFill(btnCancel);
    btnCancel.addActionListener(e -> controller.onCancel());
    makeHorizontalFill(btnCreate);
    btnCreate.addActionListener(e -> {
      if (!checkFunc())
        return;
      if (txtSize.getText().length() == 0)
        return;
      controller.onCreate(Integer.parseInt(txtSize.getText()), hashFuncs);
    });

    JPanel sizePanel = new JPanel();
    sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.X_AXIS));
    sizePanel.add(new JLabel("Size: "));
    sizePanel.add(Box.createHorizontalStrut(5));
    sizePanel.add(txtSize);

    add(btnCancel);
    add(Box.createVerticalStrut(10));
    add(sizePanel);
    add(Box.createVerticalStrut(10));

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
