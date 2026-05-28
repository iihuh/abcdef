package com.hashvis.model.table;

import java.util.*;

/**
 * Represents a table consisting of a fixed number of rows. The table
 * tracks a single currently selected row and provides methods for
 * navigating between rows and resetting the entire table state.
 */
public class Table {
  private ArrayList<Row> rows = new ArrayList<Row>();
  private int currentRow = -1;

  /**
   * Creates a table with the specified number of rows. Each row is
   * assigned a zero-based index upon creation.
   *
   * @param rows the number of rows in the table; must be at least 1
   * @throws RuntimeException if {@code rows} is less than 1
   */
  public Table(int rows) {
    if (rows < 1)
      throw new RuntimeException("Table must have at least one row");
    for (int i = 0; i < rows; i++)
      this.rows.add(new Row(i));
  }

  /**
   * Retrieves the row at the given index. The index is taken modulo the
   * table size to support circular navigation. The previously selected
   * row is deselected before the requested row becomes selected.
   *
   * @param index the desired row index (cycled through modulo size)
   * @return the selected row
   */
  public Row getRow(int index) {
    index = index % size();
    if (currentRow != -1)
      rows.get(currentRow).unchoose();
    currentRow = index;
    Row row = rows.get(index);
    row.choose();
    return row;
  }

  /**
   * Returns the number of rows in the table.
   *
   * @return the row count
   */
  public int size() {
    return rows.size();
  }

  /**
   * Resets the entire table: deselects the current row and resets every
   * row to its initial state.
   */
  public void reset() {
    currentRow = -1;
    for (Row row : rows)
      row.reset();
  }

  /**
   * Returns an unmodifiable view of all rows in the table.
   *
   * @return a list of all rows
   */
  public List<Row> getRows() {
    return Collections.unmodifiableList(rows);
  }
}
