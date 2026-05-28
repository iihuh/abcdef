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

/**
 * Visual representation of a single hash-table row.
 *
 * The layout is a horizontal row with three visual zones:
 * <ol>
 *   <li><b>Index label</b> (fixed 30&#215;35px) — e.g. "0", "1", "2".</li>
 *   <li><b>Colon separator</b>.</li>
 *   <li><b>Content panel</b> — holds the {@link ItemView} instances for
 *       each element in this row, laid out horizontally.</li>
 * </ol>
 *
 * Row state changes (normal, selected, post-selected) are reflected by the
 * border color using a compound border structure that provides both outer
 * margin (gap between rows) and inner padding (space inside the row box).
 *
 * This class implements {@link RowListener} and registers itself with the
 * model {@link Row} at construction time, so it is notified of item
 * additions, removals, and state changes.
 */
public class RowView extends JPanel implements RowListener {

  /** Panel that holds the individual item views for this row. */
  private JPanel contentPanel = new JPanel();

  /**
   * Constructs a row view for the given row model and registers as a listener
   * for row events.
   *
   * @param row the row model to visualize
   */
  public RowView(Row row) {
    super();
    row.setListener(this);

    // --- Row layout: index : item1 item2 ... ---
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.setOpaque(true);

    // Row index (e.g. "0", "1", ...) — fixed width, left-aligned
    JLabel indexLabel = new JLabel(Integer.toString(row.getIndex()));
    indexLabel.setPreferredSize(new Dimension(30, 35));
    indexLabel.setMaximumSize(new Dimension(30, 35));
    indexLabel.setHorizontalAlignment(SwingConstants.LEFT);
    this.add(indexLabel);
    // ":" separator
    JLabel colon = new JLabel(":");
    colon.setHorizontalAlignment(SwingConstants.CENTER);
    this.add(colon);
    this.add(Box.createHorizontalStrut(5));

    // Content panel for item views — stretches to fill remaining space
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
    this.add(contentPanel);

    // --- Compound border ---
    // Structure:
    //   Outer: EmptyBorder(4,4,4,4)  — margin between adjacent rows
    //   Inner: CompoundBorder(
    //     LineBorder(color, 4)       — colored border line (4px thick)
    //     EmptyBorder(4,4,4,4)       — padding inside the border
    //   )
    // This mirrors "margin + border + padding" from CSS/JavaFX box model.
    setBorderColor(Color.BLACK);
  }

  /** Sets the outer line border to the given color, keeping margin/padding. */
  private void setBorderColor(Color color) {
    this.setBorder(new CompoundBorder(
        new EmptyBorder(4, 4, 4, 4),  // outer margin (gap between rows)
        new CompoundBorder(
            new LineBorder(color, 4), // colored border line
            new EmptyBorder(4, 4, 4, 4) // inner padding (space inside the box)
        )));
  }

  @Override
  /**
   * Updates the row border color to reflect the given state.
   *
   * @param state the new row state
   */
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
  /**
   * Adds a visual item view for the newly added item.
   *
   * @param item the item that was added to the row
   */
  public void itemAdded(Item item) {
    ItemView itemView = new ItemView(item);
    contentPanel.add(itemView);
    revalidate();
    repaint();
  }

  @Override
  /**
   * Removes the visual item view corresponding to the removed item.
   *
   * @param item the item that was removed from the row
   */
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
