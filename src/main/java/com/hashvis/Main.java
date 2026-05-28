package com.hashvis;

import com.hashvis.controller.StartWindowController;

/**
 * Entry point of the HashVis application.
 * <p>
 * Initializes and displays the primary start window to the user.
 */
public class Main {
  /**
   * Launches the application by showing the start window.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    new StartWindowController().show();
  }
}
