package com.hashvis.view.table;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.*;

import com.hashvis.model.table.Item;
import com.hashvis.model.table.Item.*;

public class ItemView extends JLabel implements ItemListener {

  private Item item;

  public ItemView(Item item) {
    super(item.getName());
    this.item = item;
    this.setOpaque(true);
    item.setListener(this);
    this.setHorizontalAlignment(SwingConstants.CENTER);
    setBorderColor(Color.BLACK);
  }

  public Item getItem() {
    return item;
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
  public void stateChanged(ItemState state) {
    switch (state) {
      case GHOSTED:
        // animator.animate(animBorder.getColor(), Color.LIGHT_GRAY);
        setBorderColor(Color.LIGHT_GRAY);
        this.setForeground(Color.LIGHT_GRAY);
        break;
      case NORMAL:
        setBorderColor(Color.BLACK);
        break;
      case SELECTED:
        setBorderColor(Color.RED);
        break;
      case POSTSELECTED:
        setBorderColor(Color.ORANGE);
        break;
    }
    repaint();
  }

}
