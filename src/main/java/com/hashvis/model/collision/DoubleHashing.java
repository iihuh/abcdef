package com.hashvis.model.collision;

import java.util.ArrayList;
import java.util.List;

import com.hashvis.model.hashfunc.*;
import com.hashvis.model.table.*;

public class DoubleHashing implements CollisionResolver {
  HashFunction hashFunc1;
  HashFunction hashFunc2;

  @Override
  public List<HashFunction> getHashFunctionFields(DataType dataType) {
    switch (dataType) {
      case INTEGER:
        hashFunc1 = new HashFunctionNumber();
        hashFunc2 = new HashFunctionNumber();
        break;
      case STRING:
        hashFunc1 = new HashFunctionString();
        hashFunc2 = new HashFunctionString();
        break;
      default:
        break;
    }
    ArrayList<HashFunction> result = new ArrayList<HashFunction>();
    result.add(hashFunc1);
    result.add(hashFunc2);
    return result;
  }

  // Collision resolution inital data
  HashAction action;
  String key;
  Table table;

  private ArrayList<String> getPseudocode(HashAction action) {
    ArrayList<String> pseudocode = new ArrayList<String>();
    pseudocode.add("TODO: Add pseudocode that reflect actual algorithm well");
    pseudocode.add("The actual language, syntax, ... will be defined later");
    return pseudocode;
  }

  // Algorithm's state machine
  private Integer hashValue1 = null;
  private Integer hashValue2 = null;
  private Integer probeCount = 0;
  private Row currentRow = null;
  private Integer availableRow = null;

  @Override
  public boolean useSeparateChaining() {
    return false;
  }

  @Override
  public List<String> getAlgorithmAndInitalize(HashAction action, String key, Table table) {
    this.action = action;
    this.key = key;
    this.table = table;
    // reset the state machine
    hashValue1 = null;
    hashValue2 = null;
    probeCount = 0;
    currentRow = null;
    availableRow = null;
    return getPseudocode(action);
  }

  @Override
  public Result nextStep() {
    if (hashValue1 == null)
      return handleHashing1();
    if (hashValue2 == null)
      return handleHashing2();
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

  private Result handleHashing1() {
    hashValue1 = hashFunc1.compute(key, table.size());
    return new Result("Hash value: " + hashValue1, 0);
  }

  private Result handleHashing2() {
    hashValue2 = hashFunc2.compute(key, table.size());
    if (hashValue2 == 0)
      hashValue2 = 1;
    return new Result("Hash value: " + hashValue2, 0);
  }

  private Result handleBucketSelection() {
    if (probeCount == table.size())
      return handleFinalization();
    currentRow = table.getRow((hashValue1 + probeCount * hashValue2) % table.size());
    probeCount++;
    return new Result("Accessing bucket index " + currentRow.getIndex(), 0);
  }

  private Result processFoundItem(Item item) {
    if (action == HashAction.INSERT) {
      return new Result("Error: Duplicate key " + key, -1);
    } else if (action == HashAction.DELETE) {
      item.ghost();
      return new Result("Deleted key " + key, -1);
    } else {
      return new Result("Found key " + key, -1);
    }
  }

  private Result handleFinalization() {
    if (action == HashAction.INSERT) {
      if (availableRow != null)
        currentRow = table.getRow(availableRow);
      if (currentRow.getItems().size() != 0)
        currentRow.removeItem(currentRow.getItems().get(0));
      currentRow.addItem(key);
      return new Result("Key not found. Inserted " + key + " into bucket " + currentRow.getIndex(), -1);
    }
    return new Result("Error: Key " + key + " not found in table", -1);
  }
}
