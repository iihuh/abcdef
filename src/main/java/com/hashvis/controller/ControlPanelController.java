package com.hashvis.controller;

import javax.swing.Timer;
import com.hashvis.model.collision.CollisionResolver;
import com.hashvis.model.collision.CollisionResolver.*;
import com.hashvis.model.table.Table;
import com.hashvis.view.ui.ControlPanel;
import com.hashvis.view.ui.HashVisualizerView;

import java.util.List;

/**
 * Controls the main control panel where users run hash table operations.
 *
 * Provides handlers for creating a new table and running hash actions such
 * as insert, search, and delete. Communicates with the main window controller
 * and the collision resolver to execute algorithm steps with animation.
 */
public class ControlPanelController {
  private ControlPanel view;
  private MainWindowController mainCtrl;

  /**
   * Constructs a control panel controller linked to the given main window
   * controller.
   *
   * @param mainCtrl the main window controller for navigation and state
   */
  public ControlPanelController(MainWindowController mainCtrl) {
    this.mainCtrl = mainCtrl;
    view = new ControlPanel(this);
  }

  /**
   * Returns the control panel view.
   *
   * @return the {@code ControlPanel} for this controller
   */
  public ControlPanel getView() {
    return view;
  }

  /**
   * Navigates to the table creation panel.
   */
  public void onCreateTable() {
    mainCtrl.showCreatePanel();
  }

  /**
   * Executes a hash table action with step-by-step animation.
   *
   * Initializes the resolver with the given action and key, displays the
   * corresponding pseudocode, and runs algorithm steps on a timer while
   * updating the visualizer view.
   *
   * @param action   the hash action to perform (insert, search, or delete)
   * @param key      the key to operate on
   * @param callback a callback invoked after each animation step
   */
  public void onRun(HashAction action, String key, Runnable callback) {
    CollisionResolver resolver = mainCtrl.getResolver();
    Table table = mainCtrl.getTable();
    HashVisualizerView visView = mainCtrl.getVisView();

    List<String> code = resolver.getAlgorithmAndInitalize(action, key, table);
    visView.setPseudocode(code);

    table.reset();
    Timer timer = new Timer(500, null);
    timer.addActionListener(e -> {
      Result r;
      try {
        r = resolver.nextStep();
      } catch (RuntimeException e1) {
        r = new Result(e1.getMessage(), -1);
      }
      visView.setCurrentLine(r.currentLine());
      visView.setStatus(r.message());
      if (r.currentLine() == -1)
        timer.stop();
      callback.run();
    });
    timer.start();
  }
}
