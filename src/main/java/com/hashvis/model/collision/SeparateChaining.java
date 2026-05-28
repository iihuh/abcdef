package com.hashvis.model.collision;

import java.util.ArrayList;
import java.util.List;

import com.hashvis.model.hashfunc.*;
import com.hashvis.model.table.*;

/**
 * Separate chaining collision resolution strategy. Each bucket in the hash
 * table holds a chain of items; collisions are resolved by appending to the
 * chain at the computed bucket index.
 */
public class SeparateChaining extends ActionProcessor {
  /** The current item being examined during chain traversal. */
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

  /**
   * Selects the bucket at the computed hash index.
   *
   * @return the result of the bucket selection step
   */
  private Result handleBucketSelection() {
    currentRow = table.getRow(hashValue);
    return new Result("Accessing bucket index " + hashValue, 0);
  }

  /**
   * Traverses the chain of items in the current bucket, checking each item
   * for a match and reporting the result.
   *
   * @return the result of the traversal step
   */
  private Result handleTraversal() {
    if (currentItem == null)
      currentItem = currentRow.nextItem();

    if (currentItem == null) {
      return handleFinalization();
    }

    if (currentItem.getName().equals(key))
      return processFoundItem();

    Item itemToHighlight = currentItem;
    currentItem = null;
    return new Result("Checking item: " + itemToHighlight.getName() + " (No match)", 0);
  }

  /**
   * Processes a found item based on the current action: signals an error on
   * insert, removes on delete, or reports the key on search.
   *
   * @return the result of processing the found item
   */
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

  /**
   * Finalizes the operation. On insert, appends the key to the current
   * bucket's chain. On other actions, reports that the key was not found.
   *
   * @return the result of the finalization step
   */
  private Result handleFinalization() {
    if (action == HashAction.INSERT) {
      currentRow.addItem(key);
      return new Result("Key not found. Inserted " + key + " into bucket " + hashValue, -1);
    }
    return new Result("Error: Key " + key + " not found in table", -1);
  }
}
