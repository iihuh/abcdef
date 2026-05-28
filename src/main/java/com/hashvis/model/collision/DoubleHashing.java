package com.hashvis.model.collision;

import java.util.ArrayList;
import java.util.List;

import com.hashvis.model.hashfunc.*;
import com.hashvis.model.table.Table;

/**
 * Double hashing collision resolution strategy. Uses two independent hash
 * functions to compute probe offsets, reducing primary and secondary
 * clustering. The probe sequence is {@code h1(key) + i * h2(key)}.
 */
public class DoubleHashing extends OpenAddressing {
  /** The result of the first hash function. */
  protected Integer hashValue1 = null;
  /** The result of the second hash function. */
  protected Integer hashValue2 = null;
  /** The first hash function. */
  protected HashFunction hashFunc1;
  /** The second hash function. */
  protected HashFunction hashFunc2;

  @Override
  public List<HashFunction> getHashFunctionFields(DataType dataType) {
    HashFunction hf1;
    HashFunction hf2;
    switch (dataType) {
      case INTEGER:
        hf1 = new HashFunctionNumber();
        hf2 = new HashFunctionNumber();
        break;
      case STRING:
        hf1 = new HashFunctionString();
        hf2 = new HashFunctionString();
        break;
      default:
        throw new IllegalArgumentException("Unsupported data type: " + dataType);
    }
    ArrayList<HashFunction> result = new ArrayList<HashFunction>();
    result.add(hf1);
    result.add(hf2);
    return result;
  }

  @Override
  public void setHashFunctionFields(List<HashFunction> hashFunctions) {
    if (hashFunctions.size() != 2)
      throw new IllegalArgumentException("Expected 2 hash functions, but got " + hashFunctions.size());
    hashFunc1 = hashFunctions.get(0);
    hashFunc2 = hashFunctions.get(1);
  }

  @Override
  public List<String> getAlgorithmAndInitalize(HashAction action, String key, Table table) {
    hashValue1 = null;
    hashValue2 = null;
    return defaultInitialize(action, key, table);
  }

  @Override
  protected Result handleBucketSelection() {
    if (probeCount == table.size())
      return handleFinalization();
    currentRow = table.getRow((hashValue1 + probeCount * hashValue2) % table.size());
    probeCount++;
    return new Result("Accessing bucket index " + currentRow.getIndex(), 0);
  }

  @Override
  public Result nextStep() {
    if (hashValue1 == null) {
      hashValue1 = hashFunc1.compute(key, table.size());
      return new Result("Hash value: " + hashValue1, 0);
    }
    if (hashValue2 == null) {
      hashValue2 = hashFunc2.compute(key, table.size());
      if (hashValue2 == 0) {
        hashValue2 = 1;
      }
      return new Result("Hash value: " + hashValue2, 0);
    }
    return loop();
  }
}
