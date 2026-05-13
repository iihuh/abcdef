package com.hashvis.view.table;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.*;

import com.hashvis.model.table.Item;
import com.hashvis.model.table.Item.*;

public class ItemView extends JLabel implements ItemListener {

  private AnimatableBorder animBorder = new AnimatableBorder();

  // private Color currentBorderColor = Color.BLACK;

  private BorderAnimator animator = new BorderAnimator(300, color -> {
    this.animBorder.setColor(color);
    this.repaint(); // Trigger the UI refresh
  });

  public ItemView(Item item) {
    super(item.getName());
    this.setOpaque(true);
    item.setListener(this);
    this.setHorizontalAlignment(SwingConstants.CENTER);
    // Setup Compound Border:
    // Outer: Our custom animatable border
    // Inner: Padding (equivalent to setPadding in JavaFX)
    this.setBorder(new CompoundBorder(
        new EmptyBorder(4, 4, 4, 4), // <--- OUTER MARGIN (The gap between boxes)
        new CompoundBorder(
            animBorder, // The white box border
            new EmptyBorder(4, 4, 4, 4) // <--- INNER PADDING (Space inside the box)
        )));
  }

  @Override
  public void stateChanged(ItemState state) {
    switch (state) {
      case GHOSTED:
        animator.animate(animBorder.getColor(), Color.LIGHT_GRAY);
        this.setForeground(Color.LIGHT_GRAY);
        break;
      case NORMAL:
        animator.animate(animBorder.getColor(), Color.BLACK);
        break;
      case SELECTED:
        animator.animate(animBorder.getColor(), Color.RED);
        break;
      case POSTSELECTED:
        animator.animate(animBorder.getColor(), Color.YELLOW);
        break;
    }
  }

}
