package com.hashvis.controller;

import com.hashvis.model.collision.*;
import com.hashvis.view.ui.StartWindow;

public class StartWindowController {
  private StartWindow view;

  public StartWindowController() {
    view = new StartWindow(this);
  }

  public void onCreateTable(boolean isKeyString, int resolverIndex) {
    CollisionResolver resolver = createResolver(resolverIndex);
    if (resolver == null)
      return;
    resolver.getHashFunctionFields(isKeyString ? CollisionResolver.DataType.STRING
        : CollisionResolver.DataType.INTEGER);

    MainWindowController mainCtrl = new MainWindowController(resolver, isKeyString, () -> view.back());
    view.showDemo(mainCtrl.getView());
  }

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
