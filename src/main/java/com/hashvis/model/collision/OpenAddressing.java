package com.hashvis.model.collision;

import java.util.ArrayList;
import java.util.List;

import com.hashvis.model.hashfunc.*;
import com.hashvis.model.table.*;

public abstract class OpenAddressing extends ActionProcessor {
  protected Integer probeCount = 0;
  protected Integer availableRow = null;

  abstract protected Result handleBucketSelection();

  @Override
  public boolean useSeparateChaining() {
    return false;
  }

  public List<String> defaultInitialize(HashAction action, String key, Table table) {
    initializeActionProcess(action, key, table);
    probeCount = 0;
    availableRow = null;
    return getPseudocode(action);
  }

  @Override
  public Result nextStep() {
    if (hashValue == null)
      return handleHashing();
    return loop();
  }

  public Result loop() {
    if (currentRow == null)
      return handleBucketSelection();
    Item item = currentRow.nextItem();
    if (item == null)
      return handleFinalization();
    int ind = currentRow.getIndex();
    currentRow = null;
    if (item.isGhosted()) {
      if (availableRow == null) {
        availableRow = ind;
        return new Result("Marking bucket index " + ind + " as available", 0);
      }
    } else if (item.getName().equals(key))
      return processFoundItem(item);
    return new Result("Checking item: " + item.getName() + " (No match)", 0);
  }

  protected Result processFoundItem(Item item) {
    if (action == HashAction.INSERT) {
      return new Result("Error: Duplicate key " + key, -1);
    } else if (action == HashAction.DELETE) {
      item.ghost();
      return new Result("Deleted key " + key, -1);
    } else {
      return new Result("Found key " + key, -1);
    }
  }

  protected Result handleFinalization() {
    if (action == HashAction.INSERT) {
      if (availableRow != null) {
        currentRow = table.getRow(availableRow);
      }
      if (currentRow == null) {
        return new Result("Error: Table is full", -1);
      }
      if (currentRow.getItems().size() != 0) {
        currentRow.removeItem(currentRow.getItems().get(0));
      }
      currentRow.addItem(key);
      return new Result("Key not found. Inserted " + key + " into bucket " + currentRow.getIndex(), -1);
    }
    return new Result("Error: Key " + key + " not found in table", -1);
  }
}
