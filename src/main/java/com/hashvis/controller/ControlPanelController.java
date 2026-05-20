package com.hashvis.controller;

import javax.swing.Timer;
import com.hashvis.model.collision.CollisionResolver;
import com.hashvis.model.collision.CollisionResolver.*;
import com.hashvis.model.table.Table;
import com.hashvis.view.ui.ControlPanel;
import com.hashvis.view.ui.HashVisualizerView;

import java.util.List;

public class ControlPanelController {
  private ControlPanel view;
  private MainWindowController mainCtrl;

  public ControlPanelController(MainWindowController mainCtrl) {
    this.mainCtrl = mainCtrl;
    view = new ControlPanel(this);
  }

  public ControlPanel getView() {
    return view;
  }

  public void onCreateTable() {
    mainCtrl.showCreatePanel();
  }

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
