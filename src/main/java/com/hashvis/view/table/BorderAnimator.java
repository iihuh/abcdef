package com.hashvis.view.table;

import javax.swing.Timer;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

/**
 * A generic engine that handles color interpolation over time.
 * It is decoupled from any specific UI component.
 */
public class BorderAnimator {
  private final int duration;
  private final Consumer<Color> colorConsumer; // The "callback" to apply the color
  private Timer timer;

  public BorderAnimator(int duration, Consumer<Color> colorConsumer) {
    this.duration = duration;
    this.colorConsumer = colorConsumer;
  }

  public void animate(Color startColor, Color targetColor) {
    // Stop any existing animation to prevent overlapping/flickering
    if (timer != null && timer.isRunning()) {
      timer.stop();
    }

    final long startTime = System.currentTimeMillis();

    timer = new Timer(16, new ActionListener() { // ~60 FPS
      @Override
      public void actionPerformed(ActionEvent e) {
        long elapsed = System.currentTimeMillis() - startTime;
        float progress = Math.min(1f, (float) elapsed / duration);

        Color currentColor = interpolate(startColor, targetColor, progress);

        // Pass the calculated color back to the View
        colorConsumer.accept(currentColor);

        if (progress >= 1f) {
          timer.stop();
        }
      }
    });
    timer.start();
  }

  private Color interpolate(Color start, Color end, float progress) {
    int r = (int) (start.getRed() + (end.getRed() - start.getRed()) * progress);
    int g = (int) (start.getGreen() + (end.getGreen() - start.getGreen()) * progress);
    int b = (int) (start.getBlue() + (end.getBlue() - start.getBlue()) * progress);
    return new Color(r, g, b);
  }

  public void stop() {
    if (timer != null)
      timer.stop();
  }
}
