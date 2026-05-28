package com.hashvis.view.table;

import java.awt.*;

import com.hashvis.model.table.Table;

/**
 * A table view for open-addressing hash tables.
 *
 * Renders rows in a flowing, wrapping layout (left-to-right, top-to-bottom).
 * Because open-addressing stores one item per bucket and probes sequentially,
 * the visual representation is a grid of cells that wrap naturally when they
 * exceed the container width. This is achieved with a custom
 * {@link WrapLayout} manager that mirrors the behavior of a wrapping
 * {@code FlowLayout}.
 *
 * <h3>Why a custom layout?</h3>
 * Swing's built-in {@code FlowLayout} does not wrap components — it just
 * flows them in a single line. The custom {@link WrapLayout} correctly
 * calculates line breaks in both {@code preferredLayoutSize} and
 * {@code layoutContainer}, so components wrap when they would overflow the
 * container width. The {@link com.hashvis.view.table.TableView TableView}'s
 * viewport listener ensures the layout re-calculates wrapping when the
 * window is resized.
 */
public class OpenAddressingTableView extends TableView {
  /**
   * A custom layout manager that wraps components to new lines when they
   * exceed the container width.
   *
   * Unlike Swing's {@code FlowLayout}, this layout:
   * <ul>
   *   <li>Measures each component's preferred size and breaks to a new row
   *       when the cumulative width exceeds the target width.</li>
   *   <li>Tracks the tallest component in each row to set the row height
   *       consistently.</li>
   *   <li>Uses the same wrapping algorithm in both
   *       {@link #preferredLayoutSize} and {@link #layoutContainer}, so the
   *       calculated preferred height matches the actual layout.</li>
   * </ul>
   */
  private static class WrapLayout extends FlowLayout {
    /** Horizontal and vertical gap between components (10px). */
    private static final int GAP = 10;
    /** Fallback container width when the target has not been sized yet. */
    private static final int DEFAULT_WIDTH = 400;

    /** Creates a wrapping flow layout with 10px gaps and left alignment. */
    WrapLayout() {
      super(FlowLayout.LEFT, GAP, GAP);
    }

    @Override
    /**
     * Calculates the preferred layout size by simulating line wrapping at
     * the target container's current width.
     *
     * @param target the container to lay out
     * @return preferred dimensions: target width + calculated wrapped height
     */
    public Dimension preferredLayoutSize(Container target) {
      synchronized (target.getTreeLock()) {
        int targetWidth = target.getWidth();
        if (targetWidth <= 0)
          targetWidth = DEFAULT_WIDTH;

        int x = 0;        // current x position within the row
        int y = 0;        // current y position (top of current row)
        int rowHeight = 0; // tallest component in the current row

        for (Component comp : target.getComponents()) {
          if (comp.isVisible()) {
            Dimension d = comp.getPreferredSize();
            // If this component would overflow the row, wrap to next line
            if (x + d.width > targetWidth && x > 0) {
              x = 0;
              y += rowHeight + GAP;
              rowHeight = 0;
            }
            x += d.width + GAP;
            rowHeight = Math.max(rowHeight, d.height);
          }
        }
        return new Dimension(targetWidth, y + rowHeight + GAP);
      }
    }

    @Override
    /**
     * Lays out the container's components, wrapping them to new lines as
     * needed based on the available target width.
     *
     * @param target the container whose children are to be laid out
     */
    public void layoutContainer(Container target) {
      synchronized (target.getTreeLock()) {
        int targetWidth = target.getWidth();
        if (targetWidth <= 0)
          targetWidth = DEFAULT_WIDTH;

        int x = GAP;     // start with left gap
        int y = GAP;     // start with top gap
        int rowHeight = 0;

        for (Component comp : target.getComponents()) {
          if (comp.isVisible()) {
            Dimension d = comp.getPreferredSize();
            // Same wrapping logic as preferredLayoutSize
            if (x + d.width > targetWidth && x > GAP) {
              x = GAP;
              y += rowHeight + GAP;
              rowHeight = 0;
            }
            // Position the component
            comp.setBounds(x, y, d.width, d.height);
            x += d.width + GAP;
            rowHeight = Math.max(rowHeight, d.height);
          }
        }
      }
    }

    @Override
    /**
     * Returns the minimum layout size, which delegates to the preferred
     * layout size calculation (same wrapping behavior).
     *
     * @param target the container to measure
     * @return the minimum dimensions for the container
     */
    public Dimension minimumLayoutSize(Container target) {
      return preferredLayoutSize(target);
    }
  }

  /**
   * Constructs the view for the given table, creating a row view for each
   * row and adding them to the wrapping layout.
   *
   * @param table the table model to visualize
   */
  public OpenAddressingTableView(Table table) {
    super(table);
    content.setLayout(new WrapLayout());
    for (int i = 0; i < table.getRows().size(); i++) {
      RowView rowView = new RowView(table.getRows().get(i));
      content.add(rowView);
    }
  }
}
