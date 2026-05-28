package com.hashvis.controller;

import java.util.List;

import com.hashvis.model.collision.CollisionResolver;
import com.hashvis.model.hashfunc.HashFunction;
import com.hashvis.model.table.Table;
import com.hashvis.view.ui.CreateControlPanel;

/**
 * Controls the panel for creating a new hash table with custom parameters.
 *
 * Handles user input for table size and hash function selection, builds the
 * new table, and transitions back to the main control panel.
 */
public class CreateControlPanelController {
  private CreateControlPanel view;
  private MainWindowController mainCtrl;
  private CollisionResolver resolver;

  /**
   * Constructs a create-panel controller with the specified main controller
   * and collision resolver.
   *
   * @param mainCtrl the main window controller for navigation and state
   * @param resolver the collision resolution strategy to configure
   */
  public CreateControlPanelController(MainWindowController mainCtrl, CollisionResolver resolver) {
    this.mainCtrl = mainCtrl;
    view = new CreateControlPanel(mainCtrl.getResolver(), mainCtrl.getDataType(), mainCtrl.getTable() != null, this);
    this.resolver = resolver;
  }

  /**
   * Returns the create-control-panel view.
   *
   * @return the {@code CreateControlPanel} for this controller
   */
  public CreateControlPanel getView() {
    return view;
  }

  /**
   * Creates a new hash table with the given size and hash functions, then
   * navigates back to the control panel.
   *
   * @param size      the initial capacity of the new table
   * @param hashFuncs the list of hash functions to use
   */
  public void onCreate(int size, List<HashFunction> hashFuncs) {
    Table newTable = new Table(size);
    resolver.setHashFunctionFields(hashFuncs);
    mainCtrl.setTable(newTable);
    newTable.reset();
    mainCtrl.showControlPanel();
  }

  /**
   * Cancels table creation and returns to the main control panel.
   */
  public void onCancel() {
    mainCtrl.showControlPanel();
  }
}
