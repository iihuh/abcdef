package com.hashvis.controller;

import com.hashvis.model.collision.*;
import com.hashvis.view.ui.StartWindow;

/**
 * Controls the start window where users choose the key type and collision
 * resolution strategy before creating a hash table.
 *
 * Creates the initial configuration and launches the main visualization
 * window.
 */
public class StartWindowController {
  private StartWindow view;

  /**
   * Constructs a start window controller and its associated view.
   */
  public StartWindowController() {
    view = new StartWindow(this);
  }

  /**
   * Creates a new hash table with the specified key type and resolver, and
   * displays the main visualization window.
   *
   * @param isKeyString  whether keys are strings ({@code true}) or integers
   * @param resolverIndex the index of the collision resolver to use
   */
  public void onCreateTable(boolean isKeyString, int resolverIndex) {
    CollisionResolver resolver = createResolver(resolverIndex);
    if (resolver == null)
      return;
    resolver.getHashFunctionFields(isKeyString ? CollisionResolver.DataType.STRING
        : CollisionResolver.DataType.INTEGER);

    MainWindowController mainCtrl = new MainWindowController(resolver, isKeyString, () -> view.back());
    view.showDemo(mainCtrl.getView());
  }

  /**
   * Makes the start window visible.
   */
  public void show() {
    view.setVisible(true);
  }

  private static CollisionResolver createResolver(int index) {
    return switch (index) {
      case 0 -> new LinearProbing();
      case 1 -> new QuadraticProbing();
      case 2 -> new DoubleHashing();
      case 3 -> new SeparateChaining();
      default -> null;
    };
  }
}
