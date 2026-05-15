package com.hashvis.model.collision;

import java.util.ArrayList;
import java.util.List;

import com.hashvis.model.hashfunc.*;
import com.hashvis.model.table.*;

public class SeparateChaining extends ActionProcessor {
  private Item currentItem = null;
  @Override
  public boolean useSeparateChaining() {
    return true;
  }

  @Override
  public List<String> getAlgorithmAndInitalize(HashAction action, String key, Table table) {
    currentItem = null;
    initializeActionProcess(action, key, table);
    return getPseudocode(action);
  }

  @Override
  public Result nextStep() {
    if (hashValue == null)
      return handleHashing();
    if (currentRow == null)
      return handleBucketSelection();
    return handleTraversal();
  }

  // Resolution steps
  private Result handleBucketSelection() {
    currentRow = table.getRow(hashValue);
    return new Result("Accessing bucket index " + hashValue, 0);
  }

  private Result handleTraversal() {
    // If we don't have an item yet, or we just finished one, get the next
    if (currentItem == null)
      currentItem = currentRow.nextItem();

    if (currentItem == null) {
      return handleFinalization();
    }

    // Check if this is the item we are looking for
    if (currentItem.getName().equals(key))
      return processFoundItem();

    // Otherwise, prepare to move to the next item in the next call
    Item itemToHighlight = currentItem;
    currentItem = null; // Reset so next call to handleTraversal calls nextItem()
    return new Result("Checking item: " + itemToHighlight.getName() + " (No match)", 0);
  }
  private Result processFoundItem() {
    if (action == HashAction.INSERT) {
      return new Result("Error: Duplicate key " + key, -1);
    } else if (action == HashAction.DELETE) {
      currentRow.removeItem(currentItem);
      return new Result("Deleted key " + key, -1);
    } else {
      return new Result("Found key " + key, -1);
    }
  }
  private Result handleFinalization() {
    if (action == HashAction.INSERT) {
      currentRow.addItem(key);
      return new Result("Key not found. Inserted " + key + " into bucket " + hashValue, -1);
    }
    return new Result("Error: Key " + key + " not found in table", -1);
  }
}
