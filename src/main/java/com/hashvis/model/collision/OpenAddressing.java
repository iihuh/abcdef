package com.hashvis.model.collision;

import java.util.ArrayList;
import java.util.List;

import com.hashvis.model.hashfunc.*;
import com.hashvis.model.table.*;

/**
 * Abstract base for open addressing collision resolution strategies.
 * Provides the common probing loop, insertion, deletion, and search logic
 * shared by linear probing, quadratic probing, and double hashing.
 */
public abstract class OpenAddressing extends ActionProcessor {
  /** The number of probes performed so far. */
  protected Integer probeCount = 0;
  /** The index of an available (deleted) bucket, if one was found. */
  protected Integer availableRow = null;

  /**
   * Selects the next bucket to probe according to the specific probing
   * strategy.
   *
   * @return the result of the bucket selection step
   */
  abstract protected Result handleBucketSelection();

  @Override
  public boolean useSeparateChaining() {
    return false;
  }

  /**
   * Initializes the resolver for a visualization run with default open
   * addressing state.
   *
   * @param action the hash table action to visualize
   * @param key    the key involved in the operation
   * @param table  the hash table on which the operation is performed
   * @return the list of pseudocode lines describing the algorithm
   */
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

  /**
   * Executes the main probing loop. Inspects the current row's items for
   * a match, handles ghosted (deleted) entries, and delegates to bucket
   * selection or finalization as appropriate.
   *
   * @return the result of the current probing step
   */
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

  /**
   * Processes a found item based on the current action: signals an error on
   * insert, deletes (ghosts) on delete, or reports the key on search.
   *
   * @param item the item whose key matches the search key
   * @return the result of processing the found item
   */
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

  /**
   * Finalizes the operation. On insert, places the key into the first
   * available or empty bucket. On other actions, reports that the key was
   * not found.
   *
   * @return the result of the finalization step
   */
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
