package com.hashvis.view.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class HashVisualizerView extends JPanel {
  private int currentLine = -1;
  private List<JLabel> lineLabels = new ArrayList<>();
  private LinePanel linePanel;
  private JLabel statusLabel;

  private static final int GUTTER_WIDTH = 20;

  public HashVisualizerView() {
    super(new BorderLayout());
    setBorder(new EmptyBorder(5, 5, 5, 5));

    JPanel titlePanel = new JPanel();
    titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Pseudocode");
    title.setFont(new Font("SansSerif", Font.BOLD, 14));
    titlePanel.add(title);

    statusLabel = new JLabel("Status: Waiting...");
    statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    titlePanel.add(statusLabel);
    add(titlePanel, BorderLayout.NORTH);

    linePanel = new LinePanel();
    linePanel.setLayout(new BoxLayout(linePanel, BoxLayout.Y_AXIS));
    add(new JScrollPane(linePanel), BorderLayout.CENTER);
  }

  public void setPseudocode(List<String> lines) {
    currentLine = -1;
    lineLabels.clear();
    linePanel.removeAll();
    for (String line : lines) {
      JLabel label = new JLabel(line);
      label.setFont(new Font("Monospaced", Font.PLAIN, 14));
      label.setBorder(new EmptyBorder(3, GUTTER_WIDTH, 3, 5));
      lineLabels.add(label);
      linePanel.add(label);
    }
    linePanel.revalidate();
    linePanel.repaint();
  }

  public void setCurrentLine(int lineIndex) {
    currentLine = lineIndex;
    linePanel.repaint();
  }

  public void setStatus(String status) {
    statusLabel.setText("Status: " + status);
  }

  public void clear() {
    currentLine = -1;
    lineLabels.clear();
    linePanel.removeAll();
    linePanel.revalidate();
    linePanel.repaint();
  }

  private class LinePanel extends JPanel {
    private static final Color TRIANGLE_COLOR = new Color(0, 120, 215);
    private static final int TRI_X = 5;
    private static final int TRI_HALF_W = 5;
    private static final int TRI_HALF_H = 5;

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (currentLine < 0 || currentLine >= lineLabels.size())
        return;
      drawMarker((Graphics2D) g, lineLabels.get(currentLine));
    }

    private void drawMarker(Graphics2D g, JLabel label) {
      Point loc = label.getLocation();
      int centerY = loc.y + label.getHeight() / 2;

      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g.setColor(TRIANGLE_COLOR);

      Polygon triangle = new Polygon();
      triangle.addPoint(TRI_X, centerY - TRI_HALF_H);
      triangle.addPoint(TRI_X + TRI_HALF_W * 2, centerY);
      triangle.addPoint(TRI_X, centerY + TRI_HALF_H);
      g.fill(triangle);
    }
  }
}
