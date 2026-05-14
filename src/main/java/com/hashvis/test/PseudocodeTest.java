package com.hashvis.test;

import com.hashvis.view.ui.HashVisualizerView;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PseudocodeTest {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      List<String> pseudocode = List.of(
          "hash = hashFunction(key) % size",
          "probe = 0",
          "while table[hash] is occupied:",
          "  if table[hash] == key:",
          "    return found",
          "  probe++",
          "  hash = (hash + 1) % size",
          "  if probe == size:",
          "    return not found",
          "table[hash] = key",
          "return inserted");

      JFrame frame = new JFrame("Pseudocode Test");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(500, 400);
      frame.setLayout(new BorderLayout());

      HashVisualizerView visView = new HashVisualizerView();
      visView.setPseudocode(pseudocode);
      frame.add(visView, BorderLayout.CENTER);
      frame.setVisible(true);

      // new Timer(1000, e -> {
      // int next = visView.getCurrentLine() + 1;
      // if (next >= pseudocode.size())
      // next = -1;
      // visView.setCurrentLine(next);
      // }).start();
    });
  }
}
