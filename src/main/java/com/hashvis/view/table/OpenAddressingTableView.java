package com.hashvis.view.table;

import java.awt.*;

import com.hashvis.model.table.Table;

public class OpenAddressingTableView extends TableView {
  private static class WrapLayout extends FlowLayout {
    WrapLayout() {
      super(FlowLayout.LEFT, 10, 10);
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
      synchronized (target.getTreeLock()) {
        int targetWidth = target.getWidth();
        if (targetWidth <= 0)
          targetWidth = 400;

        int x = 0;
        int y = 0;
        int rowHeight = 0;

        for (Component comp : target.getComponents()) {
          if (comp.isVisible()) {
            Dimension d = comp.getPreferredSize();
            // Wrap if the component exceeds the current target width
            if (x + d.width > targetWidth && x > 0) {
              x = 0;
              y += rowHeight + 10;
              rowHeight = 0;
            }
            x += d.width + 10;
            rowHeight = Math.max(rowHeight, d.height);
          }
        }
        // We return exactly the width we were given and the calculated height
        return new Dimension(targetWidth, y + rowHeight + 10);
      }
    }

    @Override
    public void layoutContainer(Container target) {
      synchronized (target.getTreeLock()) {
        int targetWidth = target.getWidth();
        if (targetWidth <= 0)
          targetWidth = 400;

        int x = 10;
        int y = 10;
        int rowHeight = 0;

        for (Component comp : target.getComponents()) {
          if (comp.isVisible()) {
            Dimension d = comp.getPreferredSize();
            // EXACT SAME WRAPPING LOGIC AS preferredLayoutSize
            if (x + d.width > targetWidth && x > 0) {
              x = 10;
              y += rowHeight + 10;
              rowHeight = 0;
            }
            // Set the actual bounds of the component on screen
            comp.setBounds(x, y, d.width, d.height);
            x += d.width + 10;
            rowHeight = Math.max(rowHeight, d.height);
          }
        }
      }
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
      return preferredLayoutSize(target);
    }
  }

  public OpenAddressingTableView(Table table) {
    super(table);
    content.setLayout(new WrapLayout());
    for (int i = 0; i < table.getRows().size(); i++) {
      RowView rowView = new RowView(table.getRows().get(i));
      content.add(rowView);
    }
  }
}
