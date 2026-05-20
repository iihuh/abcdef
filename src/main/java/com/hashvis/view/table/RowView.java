package com.hashvis.view.table;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.*;

import com.hashvis.model.table.Item;
import com.hashvis.model.table.Row;
import com.hashvis.model.table.Row.*;

public class RowView extends JPanel implements RowListener {

  private JPanel contentPanel = new JPanel();

  public RowView(Row row) {
    super();
    row.setListener(this);

    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.setOpaque(true);

    JLabel indexLabel = new JLabel(Integer.toString(row.getIndex()));
    indexLabel.setPreferredSize(new Dimension(30, 35));
    indexLabel.setMaximumSize(new Dimension(30, 35));
    indexLabel.setHorizontalAlignment(SwingConstants.LEFT);
    this.add(indexLabel);
    JLabel colon = new JLabel(":");
    colon.setHorizontalAlignment(SwingConstants.CENTER);
    this.add(colon);
    this.add(Box.createHorizontalStrut(5));

    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
    this.add(contentPanel);

    // Setup Compound Border:
    // Outer: Our custom animatable border
    // Inner: Padding (equivalent to setPadding in JavaFX)
    setBorderColor(Color.BLACK);
  }

  private void setBorderColor(Color color) {
    this.setBorder(new CompoundBorder(
        new EmptyBorder(4, 4, 4, 4), // <--- OUTER MARGIN (The gap between boxes)
        new CompoundBorder(
            new LineBorder(color, 4),
            new EmptyBorder(4, 4, 4, 4) // <--- INNER PADDING (Space inside the box)
        )));
  }

  @Override
  public void stateChanged(RowState state) {
    switch (state) {
      case SELECTED:
        setBorderColor(Color.BLUE);
        break;
      case POSTSELECTED:
        setBorderColor(Color.GREEN);
        break;
      case NORMAL:
        setBorderColor(Color.BLACK);
        break;
    }
    repaint();
  }

  @Override
  public void itemAdded(Item item) {
    ItemView itemView = new ItemView(item);
    contentPanel.add(itemView);
    revalidate();
    repaint();
  }

  @Override
  public void itemRemoved(Item item) {
    for (int i = 0; i < contentPanel.getComponentCount(); i++) {
      ItemView itemView = (ItemView) contentPanel.getComponent(i);
      if (itemView.getItem() == item) {
        contentPanel.remove(i);
        revalidate();
        repaint();
        break;
      }
    }
  }
}
