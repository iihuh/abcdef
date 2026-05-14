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

  public void onRun(HashAction action, String key) {
    CollisionResolver resolver = mainCtrl.getResolver();
    Table table = mainCtrl.getTable();
    HashVisualizerView visView = mainCtrl.getVisView();

    List<String> code = resolver.getAlgorithmAndInitalize(action, key, table);
    visView.setPseudocode(code);

    table.reset();
    Timer timer = new Timer(500, null);
    timer.addActionListener(e -> {
      Result r = resolver.nextStep();
      visView.setCurrentLine(r.currentLine());
      if (r.currentLine() == -1)
        timer.stop();
    });
    timer.start();
  }
}
