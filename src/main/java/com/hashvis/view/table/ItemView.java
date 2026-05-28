package com.hashvis.view.table;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.*;

import com.hashvis.model.table.Item;
import com.hashvis.model.table.Item.*;

/**
 * Visual representation of a single hash-table item.
 *
 * Each item is displayed as a {@code JLabel} with the item's name, wrapped
 * in a compound border (margin + colored line + padding) similar to the
 * {@link RowView} border scheme. The border and text color change to reflect
 * the item's state (normal, selected, post-selected, or ghosted).
 *
 * This class implements {@link ItemListener} and registers itself with the
 * model {@link Item} at construction time.
 *
 * @see RowView
 */
public class ItemView extends JLabel implements ItemListener {

  /** The model item this view represents. */
  private Item item;

  /**
   * Constructs an item view for the given item and registers as a listener
   * for item events.
   *
   * @param item the item model to visualize
   */
  public ItemView(Item item) {
    super(item.getName());
    this.item = item;
    this.setOpaque(true);
    item.setListener(this);
    this.setHorizontalAlignment(SwingConstants.CENTER);
    setBorderColor(Color.BLACK);
  }

  /**
   * Returns the model item associated with this view.
   *
   * @return the backing item model
   */
  public Item getItem() {
    return item;
  }

  /**
   * Sets the outer line border color, keeping the same margin and padding
   * as the compound border structure used by {@link RowView}.
   */
  private void setBorderColor(Color color) {
    this.setBorder(new CompoundBorder(
        new EmptyBorder(4, 4, 4, 4), // outer margin (gap between items)
        new CompoundBorder(
            new LineBorder(color, 4), // colored border line
            new EmptyBorder(4, 4, 4, 4) // inner padding (space inside the box)
        )));
  }

  @Override
  /**
   * Updates the item border and text color to reflect the given state.
   *
   * @param state the new item state
   */
  public void stateChanged(ItemState state) {
    switch (state) {
      case GHOSTED:
        // Future enhancement: animated transition to LIGHT_GRAY color.
        // Currently sets color directly without transition.
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
