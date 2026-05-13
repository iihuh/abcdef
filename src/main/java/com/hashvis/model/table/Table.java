package com.hashvis.model.table;

import java.util.*;

public class Table {
  // Rows in the table
  private ArrayList<Row> rows = new ArrayList<Row>();
  // Currently selected row
  private int currentRow = -1;

  public Table(int rows) {
    for (int i = 0; i < rows; i++)
      this.rows.add(new Row(i));
  }

  public Row getRow(int index) {
    index = index % size();
    if (currentRow != -1)
      rows.get(currentRow).unchoose();
    currentRow = index;
    Row row = rows.get(index);
    row.choose();
    return row;
  }

  public int size() {
    return rows.size();
  }

  public void reset() {
    currentRow = -1;
    for (Row row : rows)
      row.reset();
  }

  public List<Row> getRows() {
    return Collections.unmodifiableList(rows);
  }
}
