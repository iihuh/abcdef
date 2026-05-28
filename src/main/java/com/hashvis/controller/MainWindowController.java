package com.hashvis.controller;

import javax.swing.JPanel;
import com.hashvis.model.collision.CollisionResolver;
import com.hashvis.model.collision.CollisionResolver.DataType;
import com.hashvis.model.table.Table;
import com.hashvis.view.ui.*;
import com.hashvis.view.table.*;

/**
 * Coordinates the main hash table visualization window and its sub-panels.
 *
 * Manages the top-level view of the application, including the hash table
 * display, the visualizer panel, and transitions between the control panel
 * and the table creation panel. Delegates collision resolution and data type
 * information to sub-controllers.
 */
public class MainWindowController {
  private MainWindow view;
  private CollisionResolver resolver;
  private DataType dataType;
  private Table currentTable;
  private TableView tableView;
  private HashVisualizerView visView;
  private ControlPanelController controlPanelCtrl;
  private Runnable onClose;

  /**
   * Constructs a new main window controller with the specified resolver and
   * key type, and sets up the initial table and sub-controllers.
   *
   * @param resolver    the collision resolution strategy to use
   * @param isKeyString whether keys are strings ({@code true}) or integers
   * @param onClose     the callback to invoke when navigating back
   */
  public MainWindowController(CollisionResolver resolver, boolean isKeyString, Runnable onClose) {
    this.resolver = resolver;
    this.dataType = isKeyString ? DataType.STRING : DataType.INTEGER;
    this.onClose = onClose;
    visView = new HashVisualizerView();

    controlPanelCtrl = new ControlPanelController(this);
    view = new MainWindow(this);
    view.replaceControlPanel(new CreateControlPanelController(this, resolver).getView());
    view.setPseudoCodeView(visView);
  }

  /**
   * Returns the main window panel.
   *
   * @return the {@code JPanel} for the main window
   */
  public JPanel getView() {
    return view;
  }

  /**
   * Navigates back to the start window by invoking the close callback.
   */
  public void onBack() {
    onClose.run();
  }

  /**
   * Returns the collision resolver used by this controller.
   *
   * @return the collision resolver
   */
  public CollisionResolver getResolver() {
    return resolver;
  }

  /**
   * Returns the data type (string or integer) used for keys.
   *
   * @return the data type
   */
  public DataType getDataType() {
    return dataType;
  }

  /**
   * Returns the current hash table.
   *
   * @return the current table
   */
  public Table getTable() {
    return currentTable;
  }

  /**
   * Returns the hash visualizer view displaying pseudocode and status.
   *
   * @return the visualizer view
   */
  public HashVisualizerView getVisView() {
    return visView;
  }

  /**
   * Replaces the current table with a new one and updates the table view
   * according to the resolver's collision strategy.
   *
   * @param newTable the new hash table to display
   */
  public void setTable(Table newTable) {
    currentTable = newTable;
    if (resolver.useSeparateChaining())
      tableView = new SeparateChainingTableView(currentTable);
    else
      tableView = new OpenAddressingTableView(currentTable);
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
