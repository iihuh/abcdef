package com.hashvis.view.table;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;

import com.hashvis.model.table.Table;

public class SeparateChainingTableView extends TableView {
  public SeparateChainingTableView(Table table) {
    super(table);
    content.setLayout(new GridBagLayout());
    for (int i = 0; i < table.getRows().size(); i++) {
      RowView rowView = new RowView(table.getRows().get(i));
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.gridx = 0;
      constraints.gridy = i;
      constraints.anchor = GridBagConstraints.WEST;
      constraints.weightx = 0;
      constraints.insets = new Insets(0, 0, 10, 10);
      content.add(rowView, constraints);
      constraints.gridx = 1;
      constraints.weightx = 1;
      content.add(Box.createHorizontalGlue(), constraints);
    }
  }
}
