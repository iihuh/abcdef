package com.hashvis.view.table;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.hashvis.model.table.Table;

public class TableView extends JScrollPane {

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

  private JPanel content = new JPanel();

  public TableView(Table table, boolean useSeparateChaining) {
    super();
    content.setBorder(new EmptyBorder(5, 5, 5, 5));
    if (!useSeparateChaining)
      content.setLayout(new WrapLayout());
    else
      content.setLayout(new GridBagLayout());
    for (int i = 0; i < table.getRows().size(); i++) {
      RowView rowView = new RowView(table.getRows().get(i));
      if (useSeparateChaining) {
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
      } else
        content.add(rowView);
    }
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
