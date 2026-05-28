package com.hashvis.view.table;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;

import com.hashvis.model.table.Table;

/**
 * Scrollable container for displaying a hash table visually.
 *
 * Subclasses define how rows are arranged (e.g., vertical grid for separate
 * chaining, wrapping flow for open addressing). The container is a
 * {@code JScrollPane} with a single internal {@link #content} panel that holds
 * the individual {@link RowView} instances.
 *
 * <h3>Viewport resize handling</h3>
 * A {@code ComponentListener} on the scroll pane's viewport re-computes the
 * content panel's preferred width whenever the viewport is resized. This is
 * necessary because the wrapping strategy (used by
 * {@link com.hashvis.view.table.OpenAddressingTableView OpenAddressingTableView})
 * depends on the available width. Without this listener, the content panel
 * would keep its initial preferred width and never re-wrap when the window
 * is resized.
 */
public abstract class TableView extends JScrollPane {
  /**
   * The internal panel that holds the row views.
   * Subclasses configure this panel's layout manager and populate it with
   * {@link RowView} instances.
   */
  protected JPanel content = new JPanel();

  /**
   * Constructs a table view for the given table model and configures the
   * scroll pane policies.
   *
   * @param table the table model to visualize
   */
  public TableView(Table table) {
    super();
    this.setViewportView(content);
    this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

    // --- Viewport resize listener ---
    // When the viewport width changes (e.g., window resize), the content
    // panel's preferred width is updated to fill the viewport, while the
    // preferred height is re-computed from the layout manager (which may
    // re-wrap components when the width changes).
    this.getViewport().addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        Dimension pref = content.getLayout().preferredLayoutSize(content);
        content.setPreferredSize(new Dimension(getViewport().getWidth(), pref.height));
        content.revalidate();
      }
    });
  }
}
