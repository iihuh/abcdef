package com.hashvis.view.table;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.*;

import com.hashvis.model.table.Row;
import com.hashvis.model.table.Row.*;

public class RowView extends JPanel {

  private AnimatableBorder animBorder = new AnimatableBorder();

  private JPanel contentPanel = new JPanel();

  public RowView(Row row) {
    super();
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
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

}
