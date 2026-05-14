package com.hashvis.controller;

import com.hashvis.model.table.Table;
import com.hashvis.view.ui.CreateControlPanel;

public class CreateControlPanelController {
  private CreateControlPanel view;
  private MainWindowController mainCtrl;

  public CreateControlPanelController(MainWindowController mainCtrl) {
    this.mainCtrl = mainCtrl;
    view = new CreateControlPanel(mainCtrl.getResolver(), mainCtrl.getDataType(), this);
  }

  public CreateControlPanel getView() {
    return view;
  }

  public void onCreate(int size) {
    Table newTable = new Table(size);
    mainCtrl.setTable(newTable);
    newTable.reset();
    mainCtrl.showControlPanel();
  }

  public void onCancel() {
    mainCtrl.showControlPanel();
  }
}
