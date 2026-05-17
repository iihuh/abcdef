package com.hashvis.controller;

import javax.swing.JPanel;
import com.hashvis.model.collision.CollisionResolver;
import com.hashvis.model.collision.CollisionResolver.DataType;
import com.hashvis.model.table.Table;
import com.hashvis.view.ui.*;
import com.hashvis.view.table.TableView;

public class MainWindowController {
  private MainWindow view;
  private CollisionResolver resolver;
  private DataType dataType;
  private Table currentTable;
  private TableView tableView;
  private HashVisualizerView visView;
  private ControlPanelController controlPanelCtrl;
  private Runnable onClose;

  public MainWindowController(CollisionResolver resolver, boolean isKeyString, Runnable onClose) {
    this.resolver = resolver;
    this.dataType = isKeyString ? DataType.STRING : DataType.INTEGER;
    this.onClose = onClose;
    currentTable = new Table(10);
    visView = new HashVisualizerView();
    tableView = new TableView(currentTable, resolver.useSeparateChaining());

    controlPanelCtrl = new ControlPanelController(this);
    view = new MainWindow(this);
    view.replaceControlPanel(controlPanelCtrl.getView());
    view.setHashTableView(tableView);
    view.setPseudoCodeView(visView);
  }

  public JPanel getView() {
    return view;
  }

  public void onBack() {
    onClose.run();
  }

  // Shared state accessors for sub-controllers
  public CollisionResolver getResolver() {
    return resolver;
  }

  public DataType getDataType() {
    return dataType;
  }

  public Table getTable() {
    return currentTable;
  }

  public HashVisualizerView getVisView() {
    return visView;
  }

  public void setTable(Table newTable) {
    currentTable = newTable;
    tableView = new TableView(currentTable, resolver.useSeparateChaining());
    view.setHashTableView(tableView);
  }

  void showControlPanel() {
    view.replaceControlPanel(controlPanelCtrl.getView());
  }

  void showCreatePanel() {
    CreateControlPanelController createCtrl = new CreateControlPanelController(this, resolver);
    view.replaceControlPanel(createCtrl.getView());
  }
}
