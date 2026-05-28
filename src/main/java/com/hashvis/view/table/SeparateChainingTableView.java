package com.hashvis.view.table;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;

import com.hashvis.model.table.Table;

/**
 * A table view for separate-chaining hash tables.
 *
 * Renders each row vertically using a {@code GridBagLayout}. Each row has
 * two columns:
 * <ol>
 *   <li><b>Column 0</b> — the {@link RowView} (index label + items), anchored
 *       to the left, with no horizontal stretch.</li>
 *   <li><b>Column 1</b> — horizontal glue that absorbs any extra horizontal
 *       space, keeping the row content left-aligned.</li>
 * </ol>
 *
 * In separate chaining, every bucket is visible independently and each row
 * can hold multiple items. This layout ensures rows stack vertically and
 * items within a row stretch horizontally.
 */
public class SeparateChainingTableView extends TableView {
  /**
   * Constructs the view for the given table, creating a row view for each
   * row in the table and laying them out vertically.
   *
   * @param table the table model to visualize
   */
  public SeparateChainingTableView(Table table) {
    super(table);
    content.setLayout(new GridBagLayout());
    for (int i = 0; i < table.getRows().size(); i++) {
      RowView rowView = new RowView(table.getRows().get(i));
      // Column 0: the row view itself (index + items)
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.gridx = 0;
      constraints.gridy = i;
      constraints.anchor = GridBagConstraints.WEST;
      constraints.weightx = 0;     // row view does not stretch horizontally
      constraints.insets = new Insets(0, 0, 10, 10);
      content.add(rowView, constraints);
      // Column 1: horizontal glue to fill remaining width
      constraints.gridx = 1;
      constraints.weightx = 1;     // glue absorbs all extra horizontal space
      content.add(Box.createHorizontalGlue(), constraints);
    }
  }
}
