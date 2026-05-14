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

public class RowView extends JPanel implements RowListener {

  private AnimatableBorder animBorder = new AnimatableBorder();

  private BorderAnimator animator = new BorderAnimator(300, color -> {
    this.animBorder.setColor(color);
    this.repaint(); // Trigger the UI refresh
  });

  private JPanel contentPanel = new JPanel();
  private ArrayList<Item> items = new ArrayList<Item>();

  public RowView(Row row) {
    super();
    row.setListener(this);

    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.setOpaque(true);

    JLabel indexLabel = new JLabel(Integer.toString(row.getIndex()));
    indexLabel.setPreferredSize(new Dimension(30, 35));
    indexLabel.setMaximumSize(new Dimension(30, 35));
    indexLabel.setHorizontalAlignment(SwingConstants.LEFT);
    this.add(indexLabel);
    JLabel colon = new JLabel(":");
    colon.setHorizontalAlignment(SwingConstants.CENTER);
    this.add(colon);
    this.add(Box.createHorizontalStrut(5));

    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
    this.add(contentPanel);

    // Setup Compound Border:
    // Outer: Our custom animatable border
    // Inner: Padding (equivalent to setPadding in JavaFX)
    this.setBorder(new CompoundBorder(
        new EmptyBorder(4, 4, 4, 4), // <--- OUTER MARGIN (The gap between boxes)
        new CompoundBorder(
            animBorder, // The white box border
            new EmptyBorder(4, 4, 4, 4) // <--- INNER PADDING (Space inside the box)
        )));
  }

  @Override
  public void stateChanged(RowState state) {
    switch (state) {
      case SELECTED:
        animBorder.setColor(Color.BLUE);
        break;
      case POSTSELECTED:
        animBorder.setColor(Color.GREEN);
        break;
      case NORMAL:
        animBorder.setColor(Color.BLACK);
        break;
    }
    repaint();
  }

  @Override
  public void itemAdded(Item item) {
    ItemView itemView = new ItemView(item);
    items.add(item);
    contentPanel.add(itemView);
    revalidate();
    repaint();
  }

  @Override
  public void itemRemoved(Item item) {
    for (int i = 0; i < contentPanel.getComponentCount(); i++) {
      if (items.get(i).equals(item)) {
        contentPanel.remove(i);
        items.remove(i);
        revalidate();
        repaint();
        break;
      }
    }
  }
}
