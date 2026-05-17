package com.hashvis.controller;

import java.util.List;

import com.hashvis.model.collision.CollisionResolver;
import com.hashvis.model.hashfunc.HashFunction;
import com.hashvis.model.table.Table;
import com.hashvis.view.ui.CreateControlPanel;

public class CreateControlPanelController {
  private CreateControlPanel view;
  private MainWindowController mainCtrl;
  private CollisionResolver resolver;

  public CreateControlPanelController(MainWindowController mainCtrl, CollisionResolver resolver) {
    this.mainCtrl = mainCtrl;
    view = new CreateControlPanel(mainCtrl.getResolver(), mainCtrl.getDataType(), this);
    this.resolver = resolver;
  }

  public CreateControlPanel getView() {
    return view;
  }

  public void onCreate(int size, List<HashFunction> hashFuncs) {
    Table newTable = new Table(size);
    resolver.setHashFunctionFields(hashFuncs);
    mainCtrl.setTable(newTable);
    newTable.reset();
    mainCtrl.showControlPanel();
  }

  public void onCancel() {
    mainCtrl.showControlPanel();
  }
}
