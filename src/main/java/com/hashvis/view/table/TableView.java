package com.hashvis.view.table;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;

import com.hashvis.model.table.Table;

public abstract class TableView extends JScrollPane {
  protected JPanel content = new JPanel();

  public TableView(Table table) {
    super();
    this.setViewportView(content);
    this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    // While View classes shouldn't have any logic, this logic is belong to
    // *rendering*, not *interacting*
    this.getViewport().addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        // Force the internal panel to be exactly the width of the visible area
        Dimension pref = content.getLayout().preferredLayoutSize(content);
        content.setPreferredSize(new Dimension(getViewport().getWidth(), pref.height));
        content.revalidate();
      }
    });
  }
}
